package tk.packattk.utils;

/**
 * Created by Cris on 2/11/2015.
 */
import java.util.ArrayList;

public class Person {
    private String pid;														// Purdue ID number
    private String lastName;												// last name
    private String firstName;												// first name
    private String location;												// current residence hall
    private ArrayList<Package> packages = new ArrayList<Package>();			// stores the list of packages associated with person in the system
    private int numPackages;												// number of packages intended for person in system
    private boolean isAdmin;												// true if person is an admin, false otherwise

    /*
     * Constructor for Person class.
     */
    public Person(String pid, String lastName, String firstName, String location, int numPackages, boolean isAdmin) {
        this.pid = pid;
        this.lastName = lastName;
        this.firstName = firstName;
        this.location = location;
        this.numPackages = numPackages;
        this.isAdmin = isAdmin;
    }

    /*
     * Does stuff
     */
    public Package getPackage(String tracking) {
        for (Package p : packages) {
            if (p.getTracking().equals(tracking))
                return p;
        }

        return null;
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
}

