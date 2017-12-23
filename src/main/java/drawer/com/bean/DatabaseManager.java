package drawer.com.bean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DatabaseManager{
	
	Connection dbconnection=null;	
	Statement statement=null;
	public ResultSet result=null;
	public ArrayList<String> columns=null;
	
	public DatabaseManager(DatabaseConnector connector){ 

		dbconnection = connector.dbconnection;
	}
	
	public void selectQuery(String query){
		result=null;
		try {
			statement=dbconnection.createStatement();
			result=statement.executeQuery(query);
			
			java.sql.ResultSetMetaData rsmd = result.getMetaData();
			int columnCount = rsmd.getColumnCount();
			columns=new ArrayList<String>();
			
			// The column count starts from 1
			for (int i = 1; i < columnCount + 1; i++ ) {
			  String name = rsmd.getColumnName(i);
			  columns.add(name);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void updateQuery(String query){

		try {
			statement=dbconnection.createStatement();
			statement.executeUpdate(query);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void close(){
		
		try {
			if(result!=null){
				result.close();
			}
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}