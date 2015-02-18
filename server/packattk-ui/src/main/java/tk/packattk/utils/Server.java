package tk.packattk.utils;

import java.io.IOException;
import java.net.*;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;

/**
 * Created by Cris on 2/12/2015.
 */
public class Server {
    protected final static int SERVERPORT = 1994; // port?
    private static ServerSocket serverSocket;
    private static Socket socket;

    private static BufferedReader br;
    private static BufferedWriter bw;

    public static void main(String[] args) {
        // code to set up the server will go in here maybe
        try {
            serverSocket = new ServerSocket(SERVERPORT);

            // this code should be in a loop/create new threads?
            socket = serverSocket.accept();

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }
        catch (IOException e) {
            System.out.println("IO Exception: " + e.toString());
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        // thread stuff?
    }

    public void executeRequest(String buffer) {
        if (buffer.substring(0, 5).equals("LOGIN")) {
            try {
				if(checkLoginMessage(buffer)) {
					bw.write("SUCCESS");
				} else {
					bw.write("FAILURE");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
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
