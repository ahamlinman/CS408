package tk.packattk.utils;

/**
 * Created by Cris on 2/11/2015.
 */

public class Person {
    private String pid;														// Purdue ID number
    private String lastName;												// last name
    private String firstName;												// first name
    private String location;												// current residence hall
    private int numPackages;												// number of packages intended for person in system
    private boolean isAdmin;												// true if person is an admin, false otherwise
    private String username;
    private String password;

    /*
     * Constructor for Person class.
     */
    public Person(String pid, String lastName, String firstName, String location, int numPackages, boolean isAdmin, String username, String password) {
        this.pid = pid;
        this.lastName = lastName;
        this.firstName = firstName;
        this.location = location;
        this.numPackages = numPackages;
        this.isAdmin = isAdmin;
        this.username = username;
        this.password = password;
    }
    
    public Person(Person p) {
    	if(p == null)
    		return;
    	
    	this.pid = p.pid;
    	this.lastName = p.lastName;
    	this.firstName = p.firstName;
    	this.location = p.location;
    	this.numPackages = p.numPackages;
    	this.isAdmin = p.isAdmin;
    	this.username = p.username;
    	this.password = p.password;
    }

    /*
     * Getters and setters
     */
    public void setPid(String pid) {
        this.pid = pid;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setNumPackages(int numPackages) {
        this.numPackages = numPackages;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPid() {
        return pid;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLocation() {
        return location;
    }

    public int getNumPackages() {
        return numPackages;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

