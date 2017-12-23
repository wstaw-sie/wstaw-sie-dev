package drawer.com.bean;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

//import javax.mail.Message;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;


import javax.mail.Message.RecipientType;

import wstaw.sie.model.entity.User;

public class Mail {

	public static final String SEND_SUCCESS = "send-success";
	public static final String SEND_FAIL = "send-fail";
	
	private static Logger logger  = Logger.getLogger(Mail.class);
	
	String host;
	int PORT = 465;
	Properties properties=null;
	Session session=null;
	Authenticator authenticator=null;

	String USERNAME;
	String PASSWORD;
	String applicationURL;
	
	Pair people;
	
	public Mail(String mailUser, String mailPass, String mailHost, String applicationURL) {
		USERNAME=mailUser;
		PASSWORD=mailPass;
		host=mailHost;
		this.applicationURL = applicationURL;
		
		properties = null;
		setProperties();
		session = null;
		session = Session.getInstance(properties, authenticator);
	}
	
	public Mail(Pair _people, String mailUser, String mailPass, String mailHost, String applicationURL){
		this(mailUser, mailPass, mailHost, applicationURL);
		people=_people;
	}
	
	public String sendMailWithNewPassword(User user, String password) {

		String status = SEND_FAIL;
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(USERNAME));
			message.addRecipient(RecipientType.TO, new InternetAddress(user.getEmail()));
			message.setSubject("Wstaw się!");
			message = createMessageWithNewPassword(user, password, message);
			Transport.send(message);
			status = SEND_SUCCESS;
		} catch (MessagingException mex) {
			mex.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	private MimeMessage createMessageWithNewPassword(User user, String password, MimeMessage message)
			throws MessagingException, FileNotFoundException, IOException {
		
		String msg = readMessageTemplate("mail/passwordChanged.html");				
		msg = msg.replace("{newPassword}", password);
		msg = msg.replace("{application_url}", applicationURL);
		
		message.setContent(msg, "text/html; charset=UTF-8");
		return message;
	}
	
	public String sendMail(){
	
		String status="send-fail";
	      try{
	         MimeMessage message = new MimeMessage(session);
	         message.setFrom(new InternetAddress(USERNAME));
	         message.addRecipient(RecipientType.TO, new InternetAddress(people.one.address));
	         
	         message.setSubject("Wstaw się!");
	         message=createMessage(people.two, people.one, message);

	         Transport.send(message); 
	         
	         if(people.two!=people.one){
	         
		         message = null;
		         message = new MimeMessage(session);
		         message.setFrom(new InternetAddress(USERNAME));
		         message.addRecipient(RecipientType.TO, new InternetAddress(people.two.address));
		         
		         message.setSubject("Wstaw się!");
		         message=createMessage(people.one, people.two, message);
	
		         Transport.send(message);
		         message=null;
	         }
	         status="send-success";
	      }catch (MessagingException mex) {
	         mex.printStackTrace();
	      }
	      return status;
	}	
	
	private MimeMessage createMessage(Person p1, Person p2, MimeMessage message){

		String msg="";
		if(p1!=p2){			
			if(p1.intencja != null && p1.intencja.length()>0){				
				msg = readMessageTemplate("mail/z_intencja.html");												
			}else{				
				msg = readMessageTemplate("mail/bez_intencji.html");					
			}
		}else{
			msg = readMessageTemplate("mail/za_da.html");
		}		
		msg=msg.replace("{name}", p1.firstNameBasic);
		msg=msg.replace("{second_name}", p1.secondNameBasic);
		msg=msg.replace("{email}", p1.address);
		msg=msg.replace("{what_for}", p1.intencja != null ? p1.intencja : "");
		msg=msg.replace("{text}", p1.tekst);
		msg=msg.replace("{application_url}", applicationURL);
		
		try {
			message.setContent(msg, "text/html; charset=UTF-8");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		msg=null;
		return message;
	}

	private String readMessageTemplate(String templatePath) {
		String msg = null;
		
		try {
			ClassLoader classLoader = Mail.class.getClassLoader();
			InputStream is = new DataInputStream(classLoader.getResourceAsStream(templatePath));
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		    StringBuilder out = new StringBuilder();
		    String line;
		    while ((line = reader.readLine()) != null) {
		        out.append(line);
		    }
		    msg=out.toString();
		    reader.close();				
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return msg;
	}
	
	private Properties setProperties(){
		properties = System.getProperties();
		properties.setProperty("mail.smtp.host", host);
        properties.put("mail.smtp.port", PORT);
        properties.put("mail.smtp.ssl.enable", true);
        properties.put("mail.smtp.auth", true);
        
        authenticator = null;
        authenticator = new Authenticator() {
                private PasswordAuthentication pa = new PasswordAuthentication(USERNAME, PASSWORD);
                @Override
                public PasswordAuthentication getPasswordAuthentication() {
                    return pa;
                }
            };
            
        return properties;
	}
}
