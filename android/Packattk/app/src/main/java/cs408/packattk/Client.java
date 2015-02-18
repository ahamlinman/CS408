package cs408.packattk;

import java.net.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;

/**
 * Created by Cris on 2/12/2015.
 */
public class Client {
    private static final int SERVERPORT = 1994; // port?
    private final static String SERVER = "tracy-central address goes here";

    private final static String LOGIN = "LOGIN";
    private final static int MSG_SIZE = 64;

    private String username = "";

    private InputStream is;
    private OutputStream os;
    private InputStreamReader isr;
    private OutputStreamWriter osw;
    private BufferedReader br;
    private BufferedWriter bw;
    private int bytesRead;
    private char[] msg = new char[MSG_SIZE];

    public Client(String username) {
        this.username = username;
    }

    /*
    * NOTE - SUPER IMPORTANT!!!
    * This method HAS to be called on a new thread. Android does not let you do networking on the
    * main thread. This method assumes it's being called from a thread other than main and just
    * sets up the connection and I/O objects.
    */
    private int initSocket() {
        try {
            // Initialize socket connection
            Socket socket = new Socket(SERVER, SERVERPORT);

            // Set up the I/O objects
            is = socket.getInputStream();
            os = socket.getOutputStream();
            isr = new InputStreamReader(is);
            osw = new OutputStreamWriter(os);
            br = new BufferedReader(isr);
            bw = new BufferedWriter(osw);

            return 0;
        }
        catch (IOException e) {
            System.out.println("IO Exception: " + e.toString());
            e.printStackTrace();
            return 1;
        }
        catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
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
        if (initSocket() == 1)
            return 1;

        try {
            // Write a checkLogin message to the server
            bw.write(LOGIN, 0, LOGIN.length());
            bw.write(" " + username + " " + password + "\n", 0, username.length() +
                    password.length() + 3);
            bw.flush();

            // Read socket until it's closed to determine response from server
            bytesRead = br.read(msg, 0, MSG_SIZE);
            while (bytesRead != -1) {
                if (new String(msg).trim().equals("SUCCESS"))           // credentials verified
                    return 0;
                else if (new String(msg).trim().equals("FAILURE"))      // credentials not found
                    return 1;
                bytesRead = br.read(msg, 0, MSG_SIZE);
            }

            return 1;
        }
        catch (IOException e) {
            System.out.println("IO Exception: " + e.toString());
            e.printStackTrace();
            return 1;
        }
        catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

}
