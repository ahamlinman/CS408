package tk.packattk.utils;

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
        if (command.substring(0, 5).equals("LOGIN")) {
			if(checkLoginMessage(command)) {
				return "SUCCESS";
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
        String credentials = msg.substring(msg.indexOf(" ") + 1);
        String username = credentials.substring(0, credentials.indexOf(" "));
        String password = credentials.substring(credentials.indexOf(" ") + 1);

        return checkLogin(username, password);
    }
    
    /**
     * TODO: Move this check to the database wrapper file
     * Check login credentials
     * @param username The username
     * @param password The password
     * @return Boolean true/false whether the credentials are valid
     */
    public boolean checkLogin(String username, String password) {
    	// do some database stuff to verify username + password combo
        
        // For now, just use a hardcoded username/password
        return username.equals("user") && password.equals("password");
    }
}
