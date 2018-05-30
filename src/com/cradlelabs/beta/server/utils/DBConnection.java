package com.cradlelabs.beta.server.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.google.appengine.api.utils.SystemProperty;

public class DBConnection {
	private static Connection con;
	private static final String MYSQL_DRIVER = "jdbc:mysql://";
	private static DBConnection dbc = new DBConnection();
	private static int counter;

	private DBConnection(){
	}

	public static Connection getConnection(){
		if(counter == 0){
			dbc.establishConnection();
			counter ++;
		}
		
		try {
			if(con.isClosed()){
				dbc.establishConnection();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return con;
	}

	private void establishConnection(){

		String url = "";

		try {
			if (SystemProperty.environment.value() ==
					SystemProperty.Environment.Value.Production) {
				// Load the class that provides the new "jdbc:google:mysql://" prefix.
				Class.forName("com.mysql.jdbc.GoogleDriver");
				url = "jdbc:google:mysql://election-app-pinksheet:gen-db/election2?user=root&autoReconnect=true";

			} else {
				// Local MySQL instance to use during development.
				Class.forName("com.mysql.jdbc.Driver");
				url = "jdbc:mysql://127.0.0.1:3306/election2_test?user=root&autoReconnect=true";

			}
			
			//For local testing purpose
//			Class.forName("com.mysql.jdbc.Driver");
//			url = "jdbc:mysql://127.0.0.1:3306/election2_test?user=root&autoReconnect=true";
			
			con = (Connection)DriverManager.getConnection(url);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

	}
}