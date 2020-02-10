package application;

/**
 * DBConnect class is to initialize and connect database
 */

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnect {

	private final String DBDRIVER = "com.mysql.cj.jdbc.Driver";
	private final String DBURL = "jdbc:mysql://127.0.0.1:3306/db?serverTimezone=UTC";
	private final String DBUSER = "root";
	private final String DBPASSWORD = "965180nyzNYZ/";
	private Connection conn = null;

	public DBConnect() {
		try {
			Class.forName(DBDRIVER);
			this.conn = DriverManager.getConnection(DBURL, DBUSER, DBPASSWORD);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

//Gain DB connection
	public Connection getConnection() {
		return this.conn;
	}

//Close DB connection
	public void close() {
		try {
			this.conn.close();
			// System.out.println("close");

		} catch (Exception e) {
		}
	}

}
