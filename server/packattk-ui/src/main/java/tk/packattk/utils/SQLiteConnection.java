package tk.packattk.utils;

import java.sql.*;

/**
 * Helper class to get a connection to the database
 *
 * Created by Evan on 2/15/2015.
 * Modified by Alex on 2/20/2015.
 */
public class SQLiteConnection {
	Connection conn = null;
	static boolean tablesChecked = false;

	public static Connection dbConnector()
	{
		// Ensure that we can connect to the database.
		// If not, break the application immediately.
		Connection conn;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:purdue.db");
		} catch (SQLException|ClassNotFoundException e) {
			throw new RuntimeException(e);
		}

		// If we haven't done this yet, check to ensure that
		// the required tables exist. If not, create them.
		if (!tablesChecked) {
			try {
				System.out.println("Checking table existence...");
				Statement stmt = conn.createStatement();
				stmt.executeQuery("SELECT 1 FROM people, packages;");
				stmt.close();
				tablesChecked = true;
			} catch (SQLException e) {
				System.out.println("INITIALIZING DATABASE!");
				createTables(conn);
				tablesChecked = true;
			}
		}

		return conn;
	}

	private static void createTables(Connection conn)
	{// YO DOGS, ONLY RUN THIS IF YOU WANNA NUKE EVERYTHING...
		try {
			Statement stmt = conn.createStatement();
			String sql = "DROP TABLE IF EXISTS people;";
			stmt.executeUpdate(sql);
			sql = "DROP TABLE IF EXISTS packages;";
			stmt.executeUpdate(sql);
			sql = "CREATE TABLE people(" +
					"pid            varchar(255), " +
					"lastName       varchar(255), " +
					"firstName      varchar(255), " +
					"location       varchar(255), " +
					"packages       varchar(255), " +
					"numPackages    integer, " +
					"isAdmin        integer, "  +
					"username       varchar(255), " +
					"password       varchar(255));";
			stmt.executeUpdate(sql);
			sql = "CREATE TABLE packages(" +
					"name           varchar(255), " +
					"tracking       varchar(255), " +
					"location       varchar(255), " +
					"destination    varchar(255), " +
					"student        varchar(255), " +
					"admin          varchar(255));";
			//TODO: Add check-in time
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			// Need to fix the initial SQL
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
