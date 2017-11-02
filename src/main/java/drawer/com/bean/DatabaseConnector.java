package drawer.com.bean;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConnector {

	@Value("${db.url}")
	private String dbUrl;
	
	@Value("${db.username}")
	private String dbUsername;
	
	@Value("${db.password}")
	private String dbPassword;
	
	@Value("${db.driver}")
	private String dbDriver;

	Connection dbconnection=null;
	
	public boolean connect(){
		try {
			Class.forName(dbDriver);
			if(!dbUrl.contains("?"))
			{
				dbconnection = DriverManager.getConnection(dbUrl + "?" + "user=" + dbUsername + "&password=" + dbPassword);
			}else
			{
				dbconnection = DriverManager.getConnection(dbUrl + "&user=" + dbUsername + "&password=" + dbPassword);
			}
		} catch (ClassNotFoundException e) {				
			e.printStackTrace();
		} catch(SQLException e){
			e.printStackTrace();
		}
		return isConnected();
	}
	
	public void close(){
		try {
			dbconnection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean isConnected(){
		try {
			return !dbconnection.isClosed();
		}
		catch(NullPointerException e){
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
