package tk.packattk;

import java.net.*;

/**
 * Created by Cris on 2/12/2015.
 */
public class Server {
    protected final static int port = 1994; // port?
    private static ServerSocket socket;
    private static Socket connection;

    public static void main(String[] args) {
        // code to set up the server will go in here
    }

    public int checkLogin(String username, String password) {
        // do some database stuff to verify username + password combo

        // if there's a problem return a 1

        return 0;
    }
}
