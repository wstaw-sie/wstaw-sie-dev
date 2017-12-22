package drawer.com.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import drawer.com.bean.DatabaseConnector;
import drawer.com.bean.DatabaseManager;
import drawer.com.bean.Mail;
import drawer.com.bean.Pair;
import drawer.com.bean.Person;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Draw {

	private static Logger logger  = Logger.getLogger(Draw.class);
	
	@Value("${mail.user}")
	private String mailUser;
	@Value("${mail.pass}")
	private String mailPass;
	@Value("${mail.host}")
	private String mailHost;
	@Value("${application.url}")
	private String applicationURL;
	
	private int frequency = 2; //częstotliwość dozwolonych powtórzeń, w tygodniach, możliwe powtórzenie po frequency tygodniach
	private List<Person> people=null;	//list of people in a database	<input>
	private List<Pair> pary=null;		//list of pairs for the week	<output>
	private List<String> teksty=null;	//list of quotations

	@Autowired
	private DatabaseConnector dbConnector;
	
	
	
	public List<Pair> play(){
		logger.info("Drawer - play method");
		//create connection		   
		while(!dbConnector.isConnected()){
			dbConnector.connect();
		}
		logger.info("Drawer - get data from database");
		//get data from database
		getPeople(dbConnector);		   
		   
		//initialize result array
		pary=new ArrayList<Pair>();
		   
		//cope with odd number of people
		if(people.size()%2==1){
			logger.info("Drawer - we have odd number of people");
			Random randDel = new Random();			   
			int num2del = randDel.nextInt(people.size());
			   
			while(0==people.get(num2del).getOmadla() || notYet(0, people.get(num2del).getHistory())){
				num2del = randDel.nextInt(people.size());
			}
			   
			Pair halfPair=new Pair(people.get(num2del), people.get(num2del));
			pary.add(halfPair);
			people.remove(num2del);
		}	
		   		   		   
		int count=0;
		int noUno=0; //początek losowania. zawsze losuję partnera dla obiektu z listy na pozycji noUno
		   
		//start drawing
		while(!people.isEmpty()){
			//to avoid never ending loops...
			if(count>10){
			   undo();	//removes previous pair - probably a reason of the endless loop...
			   count=0;
			}
			count++;			
			   
			//get random number
			Random rand = new Random();
			int num = rand.nextInt(people.size() - 1) + 1;			   
			   
			//check if it is allowed number and save a pair
			if(num!=noUno && people.get(num).getId()!=people.get(noUno).getOmadla() && !notYet(people.get(num).getId(), people.get(noUno).getHistory())){
				Pair para=new Pair(people.get(noUno), people.get(num));			
				pary.add(para);
			   
				people.remove(num);
				people.remove(noUno);
				count=0;				   				   				   
			}
		}	   
		//send emails
		logger.info("Drawer - sending emails!");
		send(dbConnector);
		dbConnector.close();	
		return pary;
	   }
	   
	   private void undo(){
		   //get rid of changes in history
		   people.get(0).history=people.get(0).getHistoryStat();
		   
		   //remove pair and add them to people list with updated history (forbidden numbers)
		   if(pary.size()>0){
			   pary.get(pary.size()-1).one.history.add(pary.get(pary.size()-1).two.getId());
			   pary.get(pary.size()-1).two.history.add(pary.get(pary.size()-1).one.getId());
			   people.add(pary.get(pary.size()-1).one);
			   people.add(pary.get(pary.size()-1).two);
			   pary.remove(pary.size()-1);
		   }
	   }
	   
	   private String send(DatabaseConnector freeMysql){
		  
		   String status="send-fail";
		   logger.info("Petla Mail start");
		   for(Pair para : pary){
			   //update history
			   updateDatabase(freeMysql, para.one.getId(), para.two.getId(), para.one.getOmadla(), para.two.getOmadla());//* 
			   logger.info("przed wyslaniem pary");
			   Mail wiadomosc=new Mail(para, mailUser, mailPass, mailHost, applicationURL);
			   status=wiadomosc.sendMail();
			   logger.info("Wyslano pare maili");
			   wiadomosc=null;
		   }
		   return status;
	   }
	   
	   //sprawdza czy użytkonik z historią hist może dostać wstawiennika idx 
	   private static boolean notYet(long idx, List<Integer> hist){
		   
		   for(int idxH :hist){			   
			   if(idxH==idx) return true;			   
		   }

		   return false;
	   }
	   
	   private void getPeople(DatabaseConnector freeMysql){
		   logger.info("Drawer - get people method");
		   people=new ArrayList<Person>();
		   teksty=new ArrayList<String>();
		   
		   DatabaseManager getUsers= new DatabaseManager(freeMysql);
		   DatabaseManager getHistory= new DatabaseManager(freeMysql);
		   DatabaseManager getQuotes= new DatabaseManager(freeMysql);
		   
		   getQuotes.selectQuery("select * from texts");
		   
		   try{
		   
		   while(getQuotes.result.next()){
			   teksty.add(getQuotes.result.getString("text"));
		   }
		   getQuotes.close();
		   
		   getUsers.selectQuery("select * from users");
		   
			      java.sql.Date maxdata=null; //data najwcześniejszego wstawiennika w historii wszystkich użytkoników, data nadawniejszego losowania
			      int maxHist=0; //maksymalna długość historii w bazie, sprawdzam historie wszystkich użytkowników, znajduję najdłuższą
	      
			      //tworzę tabelę użytkowników z informacjami potrzebnymi do losowania
			      logger.info("Drawer - we create table of users with needed info for drawing");
			      while(getUsers.result.next()){
			    	  List<Integer> userHistory= new ArrayList<Integer>();
			    	  
					  getHistory.selectQuery("select * from archive where first_prayer="+getUsers.result.getInt("id"));

			    	  //tworznie historii
					  logger.info("Drawer - history");
					  while (getHistory.result.next()){
						  logger.info("Drawer - history - next");
						  java.sql.Date tmpDate = getHistory.result.getDate("date");
		    		  
			    		  if(maxdata==null || tmpDate.before(maxdata))
			    			  maxdata=tmpDate;
		    		  
			    		  userHistory.add(getHistory.result.getInt("second_prayer"));
			    		  logger.info("Drawer - user history - we added: "+ getHistory.result.getInt("second_prayer"));
			    	  }
		    	  
			    	  //losowanie tekstu
					  logger.info("Drawer - drawing of text message");
			    	  
			    	  Random randT = new Random();
			    	  int tekstID = randT.nextInt(teksty.size());
			    	  
			    	  //obiekt uzytkownika
			    	  Person person=new Person(
			    			  getUsers.result.getInt("id"),
			    			  getUsers.result.getString("first_name"),
			    			  getUsers.result.getString("last_name"),
			    			  getUsers.result.getString("email"),
			    			  getUsers.result.getString("intention"),
			    			  getUsers.result.getInt("pray_for"),
				   			  userHistory,			   
				   			  teksty.get(tekstID)
			   			  );
			    	  logger.info("Drawer - person is: \n"+ person.toString());
			    	  logger.info("Drawer - max history is:" +maxHist +" and user history is: "+ userHistory.size());
			    	  
			    	  if(maxHist<userHistory.size())
			    		  maxHist=userHistory.size();
			    	  
			    	  people.add(person);
			      }

			      //usuwam najstarsze dane w historii, jeśli to konieczne
			      if(maxHist==(frequency-1)){
			    	  DatabaseManager editHistory= new DatabaseManager(freeMysql);
			    	  logger.info("Drawer - deleting from archive");
			    	  logger.info("MAXDATA IS: maxdata");
			    	  editHistory.updateQuery("delete from archive where date= "+maxdata+";");
			    	  editHistory.close();
			    	  logger.info("Drawer - deletion ended");
			      }

			      getUsers.close();
			      getHistory.close();
			   }
			   catch(Exception e) {	 
				   System.out.println(e.getMessage()+" exc");
			   }		   
	   }
	   
	   private void updateDatabase(DatabaseConnector freeMysql, int id1, int id2, int num1, int num2){
		   logger.info("Drawer - update database method, id1 is "+id1+"id2 is "+id2);
		   DatabaseManager updateDB=new DatabaseManager(freeMysql);
		   
		   if(id1==id2){
			   id2=0;
			   num2=0;
			   
			   
			   updateDB.updateQuery("update users set pray_for = null where id = "+id1+";");
			   updateDB.updateQuery("insert into  archive values ("+id1+","+num1+", NOW());");
		   }else{
	
		   		updateDB.updateQuery("update users set pray_for = "+id2+" where id = "+id1+";");
		   		updateDB.updateQuery("insert into  archive values ("+id1+","+num1+", NOW());");		      
		      
		   		updateDB.updateQuery("update users set pray_for = "+id1+" where id = "+id2+";");
			    updateDB.updateQuery("insert into  archive values ("+id2+","+num2+", NOW());");		      			      		    				     
		   }		   
	   }
}
