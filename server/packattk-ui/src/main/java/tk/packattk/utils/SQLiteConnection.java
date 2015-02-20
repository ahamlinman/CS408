package tk.packattk.utils;

import java.sql.*;

/**
* Created by Evan on 2/15/2015.
*/
public class SQLiteConnection {
    Connection conn = null;
    public static Connection dbConnector()
    {
        try
        {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:purdue.db");
            return conn;
        } catch ( Exception e )
        {
            return null;
        }
    }
}
