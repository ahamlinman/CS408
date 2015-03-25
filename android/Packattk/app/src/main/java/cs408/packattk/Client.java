package cs408.packattk;

import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import java.net.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Cris on 2/12/2015.
 */
public class Client {
    private static final String API_ENDPOINT_URL = "http://data.cs.purdue.edu:7225/api";
    private static final int CONNECTION_TIMEOUT = 10000; // 10 seconds

    private final static String LOGIN = "LOGIN";
    private final static String ADDUSER = "ADDUSER";
    private final static String GETPACKAGES = "GETPACKAGES";
    private final static String ADDPACKAGE = "ADDPACKAGE";

    Thread clientThread = null;

    public void run() {

    }

    public void enableStrictMode()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
    }

    public Client() {
    }

    private final String TAG = "Client";

    /*
    * NOTE - SUPER IMPORTANT!!!
    * These methods MUST be called on a new thread. Android does not let you do networking on the
    * main thread. This method assumes it's being called from a thread other than main and just
    * sets up the connection and I/O objects.
    */

    /**
     * Initialize a new HTTP client with default parameters
     *
     * @return The initialized client
     */
    private HttpClient initClient() {
        HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), CONNECTION_TIMEOUT);
        HttpConnectionParams.setConnectionTimeout(client.getParams(), CONNECTION_TIMEOUT);

        enableStrictMode();

        return client;
    }

    /**
     * Create a string from an InputStream
     *
     * This uses a clever trick: It tokenizes the string based on the delimiter for
     * the beginning of a string. Thus, it effectively tokenizes the string into one
     * long token.
     *
     * Based on http://stackoverflow.com/a/5445161
     *
     * @param is The input stream
     * @return A string containing the content of the stream
     */
    private String inputStreamToString(InputStream is) {
        Scanner scanner = new Scanner(is);
        Scanner tokenizer = scanner.useDelimiter("\\A");
        String str = tokenizer.hasNext() ? tokenizer.next() : "";
        scanner.close();
        return str;
    }

    /**
     * Send a command to the server for execution and return the server response
     *
     * This method serves as an abstraction to send a message to the server and return
     * exactly what the server sends back.
     *
     * @param command The command to execute (Databox format)
     * @return The server's response, or an empty string if the response could not be retrieved
     */
    private String executeServerCommand(String command) {
        HttpClient client = initClient();
        HttpPost request = new HttpPost();

        try {
            Log.v(TAG, "Executing command: " + command);
            request.setURI(new URI(API_ENDPOINT_URL));
            request.setEntity(new StringEntity(command, "UTF-8"));

            HttpResponse response = client.execute(request);

            if(response.getStatusLine().getStatusCode() != 200) {
                return ""; // An error of some sort occurred
            }

            return inputStreamToString(response.getEntity().getContent());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Sends an ADDUSER message to the server to add a new user to the database
     *
     * @param username
     * @param password
     * @return 0 on success and 1 on failure
     */
    public int addUser(String username, String password, String name){
        String command = ADDUSER + " " + username + " " + password + " " + name + "\n";
        String response = executeServerCommand(command);

        if(response.trim().equals("FAILURE"))
            return 1;
        if(response.trim().equals("SUCCESS"))
            return 0;

        return 1;
    }

    /**
     * Sends a LOGIN message to the server to check if the specified username/password pair exists.
     *
     * LOGIN messages will appear as follows:
     * LOGIN<sp>[username]<sp>[password]<\n>
     *
     * @param username - username to check
     * @param password - password to check
     *
     * @return 0 on success, 1 on failure
     */
    public int checkLogin(String username, String password) {
        String command = LOGIN + " " + username + " " + password + "\n";
        String response = executeServerCommand(command);
        Log.v(TAG, "Login response: " + response);

        return 1;

        if(response.trim().equals("FAILURE"))
            return 0;
        else if(response.trim().equals("SUCCESSUSER"))
            return 1;
        else if(response.trim().equals("SUCCESSADMIN"))
            return 2;

        return 0;
    }

    /**
     * Gets the list of packages belonging to a particular user and parses them into a list
     * @param username
     * @return
     */
    public List getPackages(String username){
        String command = GETPACKAGES + " " + username;
        List<String> packageList = new ArrayList<String>();
        String response = executeServerCommand(command);

        Log.v(TAG,"List Response" + response);

        //Parse the response and put the items in the packageList
        if(response.trim()!="EMPTY"){
            char c;
            int i=0;
            while((c=response.charAt(i))!='*'){
                String packageInfo="";
                while((c=response.charAt(i))!='\n'){
                    packageInfo+=c;
                    i++;
                }
                packageList.add(packageInfo);
                i++;
            }
        }
        return packageList;
    }

    /**
     * Add a package into the system.  Return 1 upon success, 0 on failure
     * @param username
     * @param packageId
     * @param location
     * @return
     */
     public int addPackage(String username, String packageId, String location){
        String command = ADDPACKAGE + " " + username + " " + packageId + " " + location;
        String response=executeServerCommand(command);
        Toast toast = Toast.makeText(getApplicationContext(),"un: " + username + " pid: " + packageId + " location: " + location,Toast.LENGTH_SHORT);
        toast.show();
        //if(response.trim()=="SUCCESS")
            return 1;
        //return 0;
    }

}
