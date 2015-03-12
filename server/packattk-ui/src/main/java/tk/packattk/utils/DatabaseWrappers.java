package tk.packattk.utils;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Evan on 2/15/2015.
 *
 * This is the way that Java interacts with the database! Yay!
 */
public class DatabaseWrappers
{

	public static boolean checkLogin(String username, String password) throws SQLException
	{   //Checks to see if the person logging in is in the database.
		Connection conn = SQLiteConnection.dbConnector();
//		Statement stmt = conn.createStatement();
//		String sql = "SELECT * FROM people WHERE username='" + username +
//				"' AND password='" + password + "';";
        String sql = "SELECT * FROM people WHERE username=? AND password=?';";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, username);
        stmt.setString(2, password);
		ResultSet result = stmt.executeQuery();
		boolean exists = result.next(); //True if the result has a match, false if no match found
		stmt.close();
		conn.close();
		return exists;
	}

	public static boolean addPerson(Person p)
	{   //Adds the person p to the database.
		try
		{
			Connection conn = SQLiteConnection.dbConnector();
//			Statement stmt = conn.createStatement();
			int isAdmin = (p.getIsAdmin() ? 1 : 0);
//			String sql = "INSERT INTO people(" +
//					"pid, lastName, firstName," +
//					"location, packages, numPackages, " +
//					"isAdmin, username, password)" +
//					"VALUES('" +
//					p.getPid()          + "', '" +
//					p.getLastName()     + "', '" +
//					p.getFirstName()    + "', '" +
//					p.getLocation()     + "', " +
//					"',' , 0, " +      // Insert the string ',' as the packages and 0 for numPackages
//					isAdmin             + ", '" +
//					p.getUsername()     + "', '" +
//					p.getPassword()     + "');";
            String sql = "INSERT INTO people(" +
					"pid, lastName, firstName," +
					"location, packages, numPackages, " +
					"isAdmin, username, password)" +
					"VALUES(?,?,?,?,',',0,"+isAdmin+",?,?);";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, p.getPid());
            stmt.setString(2, p.getLastName());
            stmt.setString(3, p.getFirstName());
            stmt.setString(4, p.getLocation());
            stmt.setString(5, p.getUsername());
            stmt.setString(6, p.getPassword());
            stmt.executeUpdate();
			stmt.close();
			conn.close();
			return true;
		} catch (Exception e)
		{
			//Print error somewhere?
		}
		return false;
	}

	public static Person getPerson(String pid)
	{   //Gets the information for the person with the id pid, given.
		try
		{
			Connection conn = SQLiteConnection.dbConnector();
//			Statement stmt = conn.createStatement();
//			ResultSet result = stmt.executeQuery("SELECT * FROM people WHERE " +
//					"pid='" + pid + "';" );
            String sql = "SELECT * FROM people WHERE pid=?;";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, pid);
            ResultSet result = stmt.executeQuery();
			if (!result.next())
			{
				stmt.close();
				conn.close();
				return null;
			}
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

	public static Person getPersonByUsername(String username)
	{   //Gets the information for the person with the username given.
		try
		{
			Connection conn = SQLiteConnection.dbConnector();
//			Statement stmt = conn.createStatement();
//			ResultSet result = stmt.executeQuery("SELECT * FROM people WHERE " +
//					"username='" + username + "';" );

            String sql = "SELECT * FROM people WHERE username=?;";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet result = stmt.executeQuery();

			if (!result.next())
			{
				stmt.close();
				conn.close();
				return null;
			}
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

	public static boolean addPackage(Package p)
	{   //Adds a new package to the database when scanned in.
		try
		{
			Connection conn = SQLiteConnection.dbConnector();
//			Statement stmt = conn.createStatement();
//			//First, add the package
//			String sql = "INSERT INTO packages(" +
//					"name, tracking, location," +
//					"destination, student, admin, time)" +
//					"VALUES ('" +
//					p.getName()             + "', '" +
//					p.getTracking()         + "', '" +
//					p.getLocation()         + "', '" +
//					p.getDestination()      + "', '" +
//					p.getStudent().getPid() + "', '" +
//					p.getAdmin().getPid()   + "', '" +
//					p.getTime()             + "');";
            String sql = "INSERT INTO packages(" +
					"name, tracking, location," +
					"destination, student, admin, time)" +
					"VALUES (?,?,?,?,?,?,'" + p.getTime() + "');";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, p.getName());
            stmt.setString(2, p.getTracking());
            stmt.setString(3, p.getLocation());
            stmt.setString(4, p.getDestination());
            stmt.setString(5, p.getStudent().getPid());
            stmt.setString(6, p.getAdmin().getPid());

			stmt.executeUpdate();
			//Next, look up the person and add the package to their list.
//			ResultSet result = stmt.executeQuery( "SELECT * FROM people WHERE " +
//					"pid='" + p.getStudent().getPid() + "';" );
            sql = "SELECT * FROM people WHERE pid=?;";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,p.getStudent().getPid());
            ResultSet result = stmt.executeQuery();

			if (!result.next())
			{
				stmt.close();
				conn.close();
				return false;
			}
			String packages = result.getString("packages");
			int numPackages = result.getInt("numPackages");
			packages += p.getTracking() + ","; //Add the tracking number to the list of packages
			numPackages += 1; //Increase the number of packages by one
//			sql = "UPDATE people SET " +
//					"packages='" + packages + "', " +
//					"numPackages='" + numPackages + "' " +
//					"WHERE pid='" + p.getStudent().getPid() + "';";
            sql = "UPDATE people SET " +
					"packages=?', " +
					"numPackages=? " +
					"WHERE pid=?;";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, packages);
            stmt.setInt(2, numPackages);
            stmt.setString(3, p.getStudent().getPid());
			stmt.executeUpdate();
			stmt.close();
			conn.close();
			return true;
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public static Package getPackageInfo(String trackingNum)
	{   //Gets the information about a package given the tracking number.
		try
		{
			Connection conn = SQLiteConnection.dbConnector();
//			Statement stmt = conn.createStatement();
//			ResultSet result = stmt.executeQuery("SELECT * FROM packages WHERE " +
//					"tracking='" + trackingNum + "';");

            String sql = "SELECT * FROM packages WHERE tracking=?;";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, trackingNum);
            ResultSet result = stmt.executeQuery();


			if (!result.next())
			{
				stmt.close();
				conn.close();
				return null;
			}
			String studentId =  result.getString("student");
			String adminId = result.getString("admin");
			ResultSet result2 = stmt.executeQuery("SELECT * FROM people WHERE " +
					"pid='" + studentId + "';");
			ResultSet result3 = stmt.executeQuery("SELECT * FROM people WHERE " +
					"pid='" + adminId + "';" );
			if (!result2.next()){
				stmt.close();
				conn.close();
				return null;
			}
			Person student = new Person(
					result2.getString("pid"),
					result2.getString("lastName"),
					result2.getString("firstName"),
					result2.getString("location"),
					result2.getInt("numPackages"),
					result2.getInt("isAdmin") == 1,  //isAdmin is stored as an int in the database. So it must be converted to boolean
					result2.getString("username"),
					result2.getString("password"));
			if (!result3.next())
			{
				stmt.close();
				conn.close();
				return null;
			}
			Person admin = new Person(
					result.getString("pid"),
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
					student,
					admin,
					result.getInt("time"));
			stmt.close();
			conn.close();
			return p;
		} catch (Exception e)
		{
			//Print error somewhere?
		}
		return null;
	}

	public static boolean removePackage(Package p)
	{   //Uses a package's tracking number to remove it from the database.
		try
		{
			Connection conn = SQLiteConnection.dbConnector();
//			Statement stmt = conn.createStatement();
//			//Update the student (remove tracking number, decrement numPackages)
//			ResultSet result = stmt.executeQuery( "SELECT * FROM people WHERE " +
//					"pid='" + p.getStudent().getPid() + "';" );

            String sql = "SELECT * FROM people WHERE pid=?;";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, p.getStudent().getPid());
            ResultSet result = stmt.executeQuery();




			if (!result.next())
			{
				stmt.close();
				conn.close();
				return false;
			}
			String packages = result.getString("packages");
			int numPackages = result.getInt("numPackages");
			packages = packages.replace(p.getTracking() + ",", ""); //Delete the tracking number to the list of packages
			numPackages -= 1; //Decrease the number of packages by one
//			sql = "UPDATE people SET " +
//					"packages='" + packages + "', " +
//					"numPackages='" + numPackages + "' " +
//					"WHERE pid=" + p.getStudent().getPid() + ";";

            sql = "UPDATE people SET packages='" + packages + "', " +
                    "numPackages='" + numPackages + "' " +
                    "WHERE pid=?;";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, p.getStudent().getPid());



			stmt.executeUpdate();
			//Delete the package
			//sql = "DELETE FROM packages WHERE tracking='" + p.getTracking() +"';";
            sql = "DELETE FROM packages WHERE tracking=?;";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, p.getTracking());


			stmt.executeUpdate();
			stmt.close();
			conn.close();
			return true;
		} catch (Exception e)
		{
			//Print error somewhere?
		}
		return false;
	}

	public static ArrayList<Package> getPackages(Person p)
	{   //Gets the packages for the person provided
		ArrayList<Package> packages = new ArrayList<Package>();
		try
		{
			Connection conn = SQLiteConnection.dbConnector();
			//Statement stmt = conn.createStatement();
//			ResultSet result = stmt.executeQuery("SELECT * FROM packages WHERE " +
//					"student='" + p.getPid() + "';" );

            String sql = "SELECT * FROM packages WHERE student=?;";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, p.getPid());
            ResultSet result = stmt.executeQuery();


			if (!result.next())
			{
				stmt.close();
				conn.close();
				return null;
			}

			do {
				Package curPackage = new Package(
						result.getString("name"),
						result.getString("tracking"), 
						result.getString("location"),
						result.getString("destination"),
						getPerson(result.getString("student")),
						getPerson(result.getString("admin")),
						result.getLong("time"));
				packages.add(curPackage);
			} while(result.next());

			stmt.close();
			conn.close();
			return packages;
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static ArrayList<Person> getPeople()
	{   //Gets the information for all of the people
		ArrayList<Person> people = new ArrayList<Person>();
		try
		{
			Connection conn = SQLiteConnection.dbConnector();
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery("SELECT * FROM people;");
			while(result.next()) {
				Person p = new Person(
						result.getString("pid"),
						result.getString("lastName"),
						result.getString("firstName"),
						result.getString("location"),
						result.getInt("numPackages"),
						result.getInt("isAdmin") == 1,  //isAdmin is stored as an int in the database. So it must be converted to boolean
						result.getString("username"),
						result.getString("password"));
				people.add(p);
			}
			stmt.close();
			conn.close();
			return people;
		} catch (Exception e)
		{
			//Print error somewhere?
		}
		return null;
	}

	public static ArrayList<Package> getOldPackages(long time)
	{   //Gets the packages older than the time given
		ArrayList<Package> packages = new ArrayList<Package>();
		ArrayList<String> packageList = new ArrayList<String>();
		try
		{
			Connection conn = SQLiteConnection.dbConnector();
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery("SELECT * FROM packages WHERE " +
					"time >=" + time + " ORDER BY time;" );
			while(result.next())
				packageList.add(result.getString("tracking"));
			stmt.close();
			conn.close();
			for(String trackingNum : packageList)
				packages.add(getPackageInfo(trackingNum));
			return packages;
		} catch (Exception e)
		{
			//Print error somewhere?
		}
		return null;
	}
}
