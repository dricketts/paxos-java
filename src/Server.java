import java.net.*;
import java.io.*;

/*
 * An instance of an acceptor agent from the Paxos protocol.
 */
public class Server {
    
    private int portNumber;
    
    /*
     * Create a server listening on the given port number.
     */
    public Server(int portNumber) {
        this.portNumber = portNumber;
    }
    
    /*
     * Start the server (acceptor).
     */
    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            while (true) {
                Socket socket = serverSocket.accept();
                PrintWriter out =
                    new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
                //new AcceptorThread(in, out).start();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            System.exit(-1);
        }
    }
}
