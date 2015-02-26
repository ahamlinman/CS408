package tk.packattk.api;

import java.sql.SQLException;

import tk.packattk.utils.DatabaseWrappers;

/**
 * Created by Cris on 2/12/2015.
 */
public class APIServer {

	/**
	 * Take an API request, execute the proper command, and return a response
	 * @param command The API command to execute
	 * @return A response for the client
	 * @author Cris, Alex
	 */
	public String executeRequest(String command) {
		command = command.trim();
		System.out.println("Received command: " + command);

		if (command.startsWith("LOGIN")) {
			if(checkLoginMessage(command)) {
				return "SUCCESSUSER";
			} else {
				return "FAILURE";
			}
		}
		return "INVALID";
	}

	/**
	 * Check a LOGIN message and verify the username/password
	 * @param msg The LOGIN message to check
	 * @return Whether the login was correct (true) or not (false)
	 * @author Cris, Alex
	 */
	public boolean checkLoginMessage(String msg) {
		if (msg.length() == 0)
			return false;

		String credentials = msg.substring(msg.indexOf(" ") + 1);
		System.out.println("Credentials: " + credentials);
		String username = credentials.substring(0, credentials.indexOf(" "));
		System.out.println("Username: " + username);
		String password = credentials.substring(credentials.indexOf(" ") + 1);
		System.out.println("Password: " + password);

		try {
			return DatabaseWrappers.checkLogin(username, password);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
   public boolean addNewUser( String msg){
       if(msg.length() == 0)
           return false;
       String credentials = msg.substring(msg.indexOf(" ") + 1);
       System.out.println("Credentials: " + credentials);
       String username = credentials.substring(0, credentials.indexOf(" "));
       System.out.println("Username: " + username);
       credentials = credentials.substring(credentials.indexOf(" ")+1);
       String password = credentials.substring(0,credentials.indexOf(" ") + 1);
       System.out.println("Password: " + password);
       credentials = credentials.substring(credentials.indexOf(" ")+1);
       String firstName = credentials.substring(0, credentials.indexOf(" "));
       System.out.println("Firstname: " + firstName);
       credentials = credentials.substring(credentials.indexOf(" ")+1);
       String lastName = credentials.substring(0, credentials.indexOf(" "));
       System.out.println("Lastname: " + lastName);
       credentials = credentials.substring(0,credentials.indexOf(" ")+1);
       boolean isadmin = Boolean.parseBoolean(credentials.substring(0,credentials.indexOf(" ")));
       System.out.println("isAdmin: " + isadmin);
       credentials = credentials.substring(0,credentials.indexOf(" ")+1);
       String hall = credentials.substring(0,credentials.indexOf(" "));
       System.out.println("Location: " + hall);
       String idnum = credentials.substring(credentials.indexOf(" ")+1);
       System.out.println("ID: " + idnum);
       Person newuser = new Person(idnum, lastName,firstName,hall,0, isadmin);
       try {
           return DatabaseWrappers.addUser(newUser);
       } catch (SQLException e) {
           e.printStackTrace();
           return false;
       }
   }

    /*GETPACKAGES username*/
   public boolean getPakages( String msg ){
       if(msg.length() == 0 ){
           return false;
       }

       String userid = msg.substring(credentials.indexOf(" ")+1);
       System.out.println("Userid: " + userid);
       try{
           //return DatabaseWrappers.getPackages(userid);
           return true; // will be removed once getPackage() is done in database
       } catch (SQLException e) {
           e.printStackTrace();
           return false;
       }
   }


}
