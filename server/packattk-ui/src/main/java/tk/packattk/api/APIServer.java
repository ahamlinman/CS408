package tk.packattk.api;

import java.sql.SQLException;
import java.util.ArrayList;

import tk.packattk.utils.DatabaseWrappers;

import tk.packattk.utils.Package;
import tk.packattk.utils.Person;

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
        if(command.startsWith("ADDUSER")) {
            if(addNewUser(command)) {
                return "SUCCESSUSER";
            } else {
                return "FAILURE";
            }
        }
        if (command.startsWith("GETPACKAGES")) {
            if(getPackages(command)!= null) {
                return getPackages(command);
            } else {
                return "FAILURE";
            }
        }
        if (command.startsWith("ListAllPackages")) {
            if(!(listAllPackages(command)).equals("FAILURE")) {
                return listAllPackages(command);
            } else {
                return "FAILURE";
            }
        }
        if (command.startsWith("ListOldPackages")) {
            if(!(listOldPackages(command)).equals("FAILURE")) {
                return listOldPackages(command);
            } else {
                return "FAILURE";
            }
        }
        if (command.startsWith("REMOVEPACKAGE")) {
            if(removeAPackage(command)) {
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

    /*ADDUSER username password firstname lastname isAdmin pid hall */
   public boolean addNewUser( String msg){
       if(msg.length() == 0)
           return false;
       String credentials = msg.substring(msg.indexOf(" ") + 1);
       System.out.println("Credentials: " + credentials);
       String username = credentials.substring(0, credentials.indexOf(" "));
       System.out.println("Username: " + username);
       credentials = credentials.substring(credentials.indexOf(" ")+1);
       String password = credentials.substring(0,credentials.indexOf(" "));
       System.out.println("Password: " + password);
       credentials = credentials.substring(credentials.indexOf(" ")+1);
       String firstName = credentials.substring(0, credentials.indexOf(" "));
       System.out.println("Firstname: " + firstName);
       credentials = credentials.substring(credentials.indexOf(" ")+1);
       String lastName = credentials.substring(0, credentials.indexOf(" "));
       System.out.println("Lastname: " + lastName);
       credentials = credentials.substring(credentials.indexOf(" ")+1);
       boolean isadmin = Boolean.parseBoolean(credentials.substring(0, credentials.indexOf(" ")));
       System.out.println("isAdmin: " + isadmin);
       credentials = credentials.substring(credentials.indexOf(" ")+1);
       String pid = credentials.substring(0,credentials.indexOf(" "));
       System.out.println("PID: " + pid);
       String hall= credentials.substring(credentials.indexOf(" ")+1);
       System.out.println("Location: " + hall);
       /*Person(String pid, String lastName, String firstName, String location, int numPackages, boolean isAdmin, String username, String password)*/
       Person newuser = new Person(pid, lastName, firstName, hall, 0, isadmin, username, password);
       return DatabaseWrappers.addPerson(newuser);
   }

    /*GETPACKAGES pid
    * PackageName\t trackingNumber \t location \n
*/
   public String getPackages( String msg ){
       if(msg.length() == 0 ){
           return "NOPACKAGES";
       }

       String userid = msg.substring(msg.indexOf(" ")+1);
       System.out.println("Userid: " + userid);
       ArrayList<Package> packages = DatabaseWrappers.getPackages(DatabaseWrappers.getPersonByUsername(userid));
	   String packageList ="";
       //Person p1 =
       if(packages == null) return "EMPTY";
	   for(Package p: packages){
	       packageList = packageList+ p.getName()+"\t"+
	               p.getTracking()+"\t"+
	               p.getLocation()+"\n";
	   }
	   packageList += "*"; //Indicate the end of the packages list

	   return packageList;
   }
    /*ListAllPackages adminid*/
    public String listAllPackages( String msg ){
        if(msg.length() == 0 ){
            return "NOPACKAGES";
        }

        String adminid = msg.substring(msg.indexOf(" ")+1);
        System.out.println("Adminid: " + adminid);
        Person administrator = DatabaseWrappers.getPerson(adminid);
        if(administrator== null || !administrator.getIsAdmin())
            return "FAILURE";
        String packageList =null;
        ArrayList<Person> people = DatabaseWrappers.getPeople();
		
		if(people == null) {
			return "FAILURE";
		}
		
		for(Person user: people){
		    ArrayList<Package> packages = DatabaseWrappers.getPackages(user);
		    if(packages == null) {
		    	return "FAILURE";
		    }
		    
		    for(Package p: packages){
		        packageList = packageList+ p.getName()+"\t"+
		                p.getTracking()+"\t"+ p.getTime()+"\t"+
		                p.getLocation()+"\n";
		    }
		    packageList += "#"; //Indicate the end of the packages list
		}
		packageList += "*";
		return packageList;
    }
    /*ListOldPackages time*/
    public String listOldPackages( String msg ){
        if(msg.length() == 0 ){
            return "NOPACKAGES";
        }

        long time = Long.parseLong(msg.substring(msg.indexOf(" ")+1));
        String packageList =null;
        ArrayList<Package> packages = DatabaseWrappers.getOldPackages(time);
		
		if(packages == null) {
			return "EMPTY";
		}
		
		for(Package p: packages){
		    packageList = packageList+ p.getName()+"\t"+
		            p.getTracking()+"\t"+ p.getTime()+"\t"+
		            p.getLocation()+"\n";
		}
		packageList += "#"; //Indicate the end of the packages list
		return packageList;
    }
    /*ADDPACKAGE  packageId	trackingNumber adminid firstname lastname date location\n*/
    public boolean addPackages( String msg ){
        if(msg.length() == 0 ){
            return false;
        }

        String credentials = msg.substring(msg.indexOf(" ") + 1);
        String packageId = credentials.substring(0,credentials.indexOf(" "));
        System.out.println("packageID: " + packageId);

        credentials = credentials.substring(credentials.indexOf(" ")+1);
        String trackingNumber = credentials.substring(0,credentials.indexOf(" "));
        System.out.println("TrackingNumber: " + trackingNumber);

        credentials = credentials.substring(credentials.indexOf(" ")+1);
        String adminID = credentials.substring(0,credentials.indexOf(" "));
        System.out.println("AdminID: " + adminID);

        credentials = credentials.substring(credentials.indexOf(" ")+1);
        String firstName = credentials.substring(0,credentials.indexOf(" "));
        System.out.println("firstName: " + firstName);

        credentials = credentials.substring(credentials.indexOf(" ")+1);
        String lastName = credentials.substring(0,credentials.indexOf(" "));
        System.out.println("lastName: " + lastName);

        credentials = credentials.substring(credentials.indexOf(" ")+1);
        String sdate = credentials.substring(0,credentials.indexOf(" "));
        long date = Long.parseLong(sdate);
        System.out.println("Date: " + date);

        String destination = credentials.substring(credentials.indexOf(" ")+1,credentials.indexOf("\n"));
        System.out.println("Destination: " + destination);
        /*Package(String name, String tracking, String location, String destination, Person student,
         Person admin, long date) */
        Person user = DatabaseWrappers.getPersonByName(firstName, lastName);
        Person admin = DatabaseWrappers.getPersonByUsername(adminID);
        if(user!= null && admin != null && admin.getIsAdmin()){
            Package p = new Package(packageId, trackingNumber,admin.getLocation(),destination,user,admin,date);
            return DatabaseWrappers.addPackage(p);
        }
        else return false;
    }

    /*REMOVEPACKAGE trackingNum*/
    public boolean removeAPackage( String msg ){
        if(msg.length() == 0 ){
            return false;
        }

        String trackingNumber = msg.substring(msg.indexOf(" ")+1);
        System.out.println("TrackingNumber: " + trackingNumber);
        Package p = DatabaseWrappers.getPackageInfo(trackingNumber);
		if(p!= null)
		    return DatabaseWrappers.removePackage(p);
		else
			return false;
    }
    /* TODO: Update package,
    *need to specify which variable need to be modified*/
}
