package edu.unsw.streaming.dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 * 
 * @author Charagh Jethnani
 *used as a Connection Factory for the database
 */
public class JDBC {
	public static Connection getConnection(){
		try {

			Class.forName("org.postgresql.Driver");

		} catch (ClassNotFoundException e) {

			System.out.println("Where is your PostgreSQL JDBC Driver? "
					+ "Include in your library path!");
			e.printStackTrace();

		}

		Connection connection = null;

		try {

			connection = DriverManager.getConnection(
					"jdbc:postgresql://localhost/postgres", "postgres",
					"123456");

		} catch (SQLException e) {

			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();

		}

		if (connection != null) {
		} else {
			System.out.println("Failed to make connection!");
		}
		
		return connection;
	}
}
