package persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

//class to manage the connection to the database

public class DBConnection {
	
	
	private static String url;
	private static String user;
	private static String password;
	private static Connection connection;
	
	//retrieve db.url db.user and db.password from properties file
	private static void retrieveDBCredentials() throws IOException {
		Properties props = new Properties();
		InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("config/db.properties.txt");
//				new FileInputStream("/config/db.properties.txt");
			props.load(input);
			url = props.getProperty("db.url");
			user = props.getProperty("db.user");
			password = props.getProperty("db.password");
	}
	
	//check if there already is a connection
	//if there is return that connection
	//if there is no connection, establish connection and return it
	public static Connection getConnection() throws SQLException, IOException {
		if(connection == null || connection.isClosed()) {
				retrieveDBCredentials();
				connection = DriverManager.getConnection(url, user, password);
		}
		return connection;
	}
	
	//close connection
	public static void closeConnection() {
		try {
			if(connection != null && !connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	 

	
}
