package cs408.packattk;

/**
 * Created by Cris on 2/11/2015.
 *
 * Package - say some stuff here
 */
public class Package {
    private String name;			// personal name given to a package (for human purposes)
    private String tracking;		// tracking number of the package
    private String location;		// current location of the package
    private String destination;		// desired destination of the package
    private Person student;			// student the package is addressed to
    private Person admin;			// admin who most recently scanned the package
    private long date;
    /*
     * Constructor for the Package class.
     */
    public Package(String name, String tracking, String location, String destination, Person student, Person admin, long date) {
        this.name = name;
        this.tracking = tracking;
        this.location = location;
        this.destination = destination;
        this.student = student;
        this.admin = admin;
        this.date = date;
    }

    /*
     * Determines if the package is where it should be.
     *
     * @return true if the package is at the correct location, false otherwise
     */
    public boolean atCorrectLocation() {
        return (location.equals(destination));
    }

    /*
     * Getters and setters
     */
    public void setName(String name) {
        this.name = name;
    }

    public void setTracking(String tracking) {
        this.tracking = tracking;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setStudent(Person student) {
        this.student = student;
    }

    public void setAdmin(Person admin) {
        this.admin = admin;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getTracking() {
        return tracking;
    }

    public String getLocation() {
        return location;
    }

    public String getDestination() {
        return destination;
    }

    public Person getStudent() {
        return student;
    }

    public Person getAdmin() {
        return admin;
    }

    public long getDate() {
        return date;
    }
}

