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
            sql = "CREATE TABLE people(" +
                    "pid            varchar(255), " +
                    "lastName       varchar(255), " +
                    "firstName      varchar(255), " +
                    "location       varchar(255), " +
                    "packages       varchar(255), " +
                    "numPackages    integer, " +
                    "isAdmin        integer), "  +
                    "username       varchar(255), " +
                    "password       varchar(255);";
            stmt.executeUpdate(sql);
            sql = "CREATE TABLE packages(" +
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

    public static boolean checkLogin(String username, String password)
    {   //Checks to see if the person logging in is in the database.
        try
        {
            Connection conn = sqliteConnection.dbConnector();
            //TODO: Evan: Finish this function

            conn.close();
            return true;
        } catch (Exception e)
        {
            //Print error somewhere?
        }
        return false;
    }

    public static void addPerson(Person p)
    {   //Adds the person p to the database.
        try
        {
            Connection conn = sqliteConnection.dbConnector();
            Statement stmt = conn.createStatement();
            int isAdmin = (p.getIsAdmin() ? 1 : 0);
            String sql = "INSERT INTO people(" +
                    "pid, lastName, firstName," +
                    "location, packages, numPackages, " +
                    "isAdmin, username, password)" +
                    "VALUES(" +
                    p.getPid()          + ", " +
                    p.getLastName()     + ", " +
                    p.getFirstName()    + ", " +
                    p.getLocation()     + ", " +
                    "', ' , 0, " +              // Insert the string ', ' as the packages and 0 for numPackages
                    isAdmin             + ", " +
                    p.getUsername()     + ", " +
                    p.getPassword()     + ");";
            stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
        } catch (Exception e)
        {
            //Print error somewhere?
        }
    }

    public static Person getPerson(String pid)
    {   //Gets the information for the person with the id pid, given.
        try
        {
            Connection conn = sqliteConnection.dbConnector();
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("SELECT * FROM people WHERE " +
                    "pid=" + pid + ";" );
            Person p = new Person(
                    result.getString("pid"),
                    result.getString("lastName"),
                    result.getString("firstName"),
                    result.getString("location"),
                    result.getInt("numPackages"),
                    result.getInt("isAdmin") == 1,  //isAdmin is stored as an int in the database. So it must be converted to boolean
                    result.getString("username"),
                    result.getString("password"));
            stmt.close();
            conn.close();
            return p;
        } catch (Exception e)
        {
            //Print error somewhere?
        }
        return null;
    }

    public static void addPackage(Package p)
    {   //Adds a new package to the database when scanned in.
        try
        {
            Connection conn = sqliteConnection.dbConnector();
            Statement stmt = conn.createStatement();
            //First, add the package
            String sql = "INSERT INTO packages(" +
                    "name, tracking, location" +
                    "destination, student, admin" +
                    "VALUES(" +
                    p.getName()         + "," +
                    p.getTracking()     + "," +
                    p.getLocation()     + "," +
                    p.getDestination()  + "," +
                    p.getStudent().getPid() + "," +
                    p.getAdmin().getPid()   + ";";
            stmt.executeUpdate(sql);
            //Next, look up the person and add the package to their list.
            ResultSet result = stmt.executeQuery( "SELECT * FROM people WHERE " +
                    "pid=" + p.getStudent().getPid() + ";" );
            String packages = result.getString("packages");
            int numPackages = result.getInt("numPackages");
            packages += p.getTracking() + ", "; //Add the tracking number to the list of packages
            numPackages += 1; //Increase the number of packages by one
            sql = "UPDATE people SET " +
                    "packages=" + packages + ", " +
                    "numPackages=" + numPackages + " " +
                    "WHERE pid=" + p.getStudent().getPid() + ";";
            stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
        } catch (Exception e)
        {
            //Print error somewhere?
        }
    }

    public static Package getPackageInfo(String trackingNum)
    {   //Gets the information about a package given the tracking number.
        try
        {
            Connection conn = sqliteConnection.dbConnector();
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("SELECT * FROM packages WHERE " +
                    "tracking=" + trackingNum + ";" );
            String studentId =  result.getString("student");
            String adminId = result.getString("admin");
            ResultSet result2 = stmt.executeQuery("SELECT * FROM people WHERE " +
                    "pid=" + studentId + ";" );
            ResultSet result3 = stmt.executeQuery("SELECT * FROM people WHERE " +
                    "pid=" + adminId + ";" );
            Person student = new Person(result.getString("pid"),
                    result2.getString("lastName"),
                    result2.getString("firstName"),
                    result2.getString("location"),
                    result2.getInt("numPackages"),
                    result2.getInt("isAdmin") == 1,  //isAdmin is stored as an int in the database. So it must be converted to boolean
                    result2.getString("username"),
                    result2.getString("password"));
            Person admin = new Person(result.getString("pid"),
                    result3.getString("lastName"),
                    result3.getString("firstName"),
                    result3.getString("location"),
                    result3.getInt("numPackages"),
                    result3.getInt("isAdmin") == 1,  //isAdmin is stored as an int in the database. So it must be converted to boolean
                    result3.getString("username"),
                    result3.getString("password"));
            Package p = new Package(
                    result.getString("name"),
                    result.getString("tracking"),
                    result.getString("location"),
                    result.getString("destination"),
                    student, admin);
            stmt.close();
            conn.close();
            return p;
        } catch (Exception e)
        {
            //Print error somewhere?
        }
        return null;
    }

    public static void removePackage(Package p)
    {   //Uses a package's tracking number to remove it from the database.
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
    {   //Looks up the package's tracking number and modifies the matching package.
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
