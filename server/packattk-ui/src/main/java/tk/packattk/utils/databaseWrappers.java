package tk.packattk.utils;
import java.sql.*;

/**
 * Created by Evan on 2/15/2015.
 *
 * This is the way that Java interacts with the database! Yay!
 */
public class databaseWrappers
{
    public static void createTables()
    {// YO DOGS, ONLY RUN THIS IF YOU WANNA NUKE EVERYTHING...
        try
        {
            Connection conn = sqliteConnection.dbConnector();
            Statement stmt = conn.createStatement();
            String sql = "DROP TABLE IF EXISTS people";
            stmt.executeUpdate(sql);
            sql = "DROP TABLE IF EXISTS packages";
            stmt.executeUpdate(sql);
            sql = "create table people(" +
                    "pid            varchar(255), " +
                    "lastName       varchar(255), " +
                    "firstName      varchar(255), " +
                    "location       varchar(255), " +
                    "packages       varchar(255), " +
                    "numPackages    integer, " +
                    "isAdmin        integer)";
            stmt.executeUpdate(sql);
            sql = "create table packages(" +
                    "name           varchar(255), " +
                    "tracking       varchar(255), " +
                    "location       varchar(255), " +
                    "destination    varchar(255), " +
                    "student        varchar(255), " +
                    "admin          varchar(255))";
            stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
        } catch (Exception e)
        {
            //Print error somewhere?
        }
    }

    public static void addPerson(Person p)
    {
        try
        {
            Connection conn = sqliteConnection.dbConnector();
            //TODO: Evan: Finish this function

            conn.close();
        } catch (Exception e)
        {
            //Print error somewhere?
        }
    }

    public static Person getPerson(String pid)
    {
        try
        {
            Connection conn = sqliteConnection.dbConnector();
            //TODO: Evan: Finish this function

            conn.close();
        } catch (Exception e)
        {
            //Print error somewhere?
        }
        return null;
    }

    public static void addPackage(Package p)
    {
        try
        {
            Connection conn = sqliteConnection.dbConnector();
            //TODO: Evan: Finish this function

            conn.close();
        } catch (Exception e)
        {
            //Print error somewhere?
        }
    }

    public static Package getPackageInfo(String trackingNum)
    {
        try
        {
            Connection conn = sqliteConnection.dbConnector();
            //TODO: Evan: Finish this function

            conn.close();
        } catch (Exception e)
        {
            //Print error somewhere?
        }
        return null;
    }

    public static void removePackage(Package p)
    {
        try
        {
            Connection conn = sqliteConnection.dbConnector();
            //TODO: Evan: Finish this function

            conn.close();
        } catch (Exception e)
        {
            //Print error somewhere?
        }
    }

    public static void updatePackage(Package p)
    {
        try
        {
            Connection conn = sqliteConnection.dbConnector();
            //TODO: Evan: Finish this function

            conn.close();
        } catch (Exception e)
        {
            //Print error somewhere?
        }
    }
}
