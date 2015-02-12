package tk.packattk;

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
        if (buffer.substring(0, 5).equals("LOGIN"))
            checkLogin(buffer);
    }

    public int checkLogin(String msg) {
        String credentials = msg.substring(msg.indexOf(" ") + 1);
        String username = credentials.substring(0, credentials.indexOf(" "));
        String password = credentials.substring(credentials.indexOf(" ") + 1);

        // do some database stuff to verify username + password combo

        try {
            bw.write("SUCCESS");
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

        return 0;
    }
}
