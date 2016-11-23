import java.net.*;
import java.io.*;

public class AcceptorServer extends Acceptor<ObjectOutput> {
    
    private int portNumber;
    
    /*
     * Create a server listening on the given port number.
     */
    public AcceptorServer(int portNumber) {
        super();
        this.portNumber = portNumber;
    }
    
    /*
     * Start the server (acceptor).
     */
    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            while (true) {
                System.out.println("Acceptor server listening...");
                Socket socket = serverSocket.accept();
                System.out.println("Connection received");
                ObjectOutput out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInput in = new ObjectInputStream(socket.getInputStream());
                new Thread(new AcceptorStreamReader(in, out)).start();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            System.exit(-1);
        }
    }
    
    private class AcceptorStreamReader implements Runnable {
        
        private ObjectInput in;
        private ObjectOutput out;
        
        public AcceptorStreamReader(ObjectInput in, ObjectOutput out) {
            this.in = in;
            this.out = out;
        }
        
        public void run() {
            while (true) {
                try {
                    Object msg = in.readObject();
                    if (msg instanceof PrepareRequest) {
                        System.out.println("PrepareRequest received");
                        PrepareRequest req = (PrepareRequest) msg;
                        prepareRequest(req.getProposalNum(), out);
                    } else if (msg instanceof AcceptRequest) {
                        System.out.println("AcceptRequest received");
                        AcceptRequest req = (AcceptRequest) msg;
                        acceptRequest(req.getProposalNum(), req.getValue());
                    }
                    // TODO: log other message types for debugging
                } catch (ClassNotFoundException e) {
                    System.out.println("Ill-formed stream " + e);
                    break;
                } catch (IOException e) {
                    System.out.println("Error reading from stream " + e);
                    break;
                }
            }
        }
    }
    
    public void sendPromise(ObjectOutput agent, ProposalNum n, ProposalNum highest, Object value) {
        try {
            agent.writeObject(new Promise(n, highest, value));
        } catch (IOException e) {
            System.out.println("Error writing to stream " + e);
        }
    }
    
    public static void main(String[] args) {
	    System.out.println("Launching acceptor server...");
	    AcceptorServer server = new AcceptorServer(4000);
	    server.start();
    }
    
}