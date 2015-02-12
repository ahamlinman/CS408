package database;
import java.sql.*;
import javax.mail.*;

public class User {
	
	
	/*The following String vars are just for testing 
	 * since all those information will be available when the server.java is ready*/
	
	String insertUser ="Insert" ;
	String Email = "Email";
	/* Variables for testing purpose  */
	
	/**Insert user to the database with username, password, and email address. 
	 * Return true when the user account is created, otherwise return false
	 */
	  public boolean insertUser(Connection c, String userName,String ps, String email){
		  try {
			  Class.forName("org.sqlite.JDBC");
			  c = DriverManager.getConnection("jdbc:sqlite:packattk.db");
			  Statement stmt = c.createStatement();
			  ResultSet rs =stmt.executeQuery("SELECT * FROM useraccount WHERE username ='"+userName+"'");
			  if(rs.next()){
				  System.out.println("User name exists!\n");
				  return false;
			  }
			  String sql = "INSERT INTO USERACCOUNT (USERNAME,PASSWORD,EMAIL) " +
                  "VALUES ('"+userName+"','"+ps+"','"+email+"')"; 
		      stmt.executeUpdate(sql);
		      c.commit();
		      emailUser(email);
		      
		  }catch ( Exception e ) {
			   System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			   System.exit(0);
		}   
		return false;
	  }
	  
	/**Email user notifications.
	* Return true when the user account is created, otherwise return false
	*/
	  public boolean emailUser(String emailAddr){

		  return false;
	  }
	  
	  public static void main( String args[] )
	  {
		String ex = "Insert";
		String user = "hungry:(";
		String psword = "foooodddd";
		String email = "ji43@purdue.edu";
		User u = new User();
	    Connection c = null;
	    try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:packattk.db");
	      System.out.println("Opened database successfully");
	      Statement stmt = c.createStatement();
	      String sql = "CREATE TABLE USERACCOUNT " +
	                   "(USERNAME     CHAR(50)    NOT NULL," +
	                   " PASSWORD     CHAR(20)    NOT NULL, " + 
	                   " EMAIL		  TEXT		  NOT NULL)"; 
	      stmt.executeUpdate(sql);
	      String sql1 = "INSERT INTO USERACCOUNT (USERNAME,PASSWORD,EMAIL) " +
                  "VALUES ('Squirrel','haha:)','ji43@purdue.edu');"; 
	      stmt.executeUpdate(sql);
	      if(ex.equals("insertUser")){
	    	  u.insertUser(c, user,psword,email);
	      }
	      stmt.close();
	      c.close();
	      
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    System.out.println("Records created successfully");
	  }
}
