import java.net.*;
import java.io.*;
import java.util.*;

/*
 * The network implementation of a Proposer agent
 */
public class ProposerServer extends Proposer<Address> {
    
    private Set<Address> acceptorAddrs;
    private Map<Address, ObjectOutput> acceptorOutStreams;
    
    public ProposerServer(Set<Address> acceptorAddrs, String uid, int quorumSize) {
        super(uid, quorumSize);
        this.acceptorAddrs = acceptorAddrs;
        acceptorOutStreams = new HashMap<Address, ObjectOutput>();
    }
    
    public void start() {
        for (Address addr : acceptorAddrs) {
            try {
                Socket socket = new Socket(addr.getHostName(), addr.getPort());
                acceptorOutStreams.put(addr, new ObjectOutputStream(socket.getOutputStream()));
                ObjectInput in = new ObjectInputStream(socket.getInputStream());
                new Thread(new ProposerStreamReader(in, addr)).start();
            } catch (IOException e) {
                System.err.println("Error connecting to acceptor at " +
                                    addr.getHostName() + ":" + addr.getPort());
                System.err.println(e);
                System.exit(-1);
            }
        }
        System.out.println("Proposer listening on standard in...");
        Scanner sc = new Scanner(System.in);
        int val = sc.nextInt();
        System.out.println("Read value, about to propose " + val);
        prepareRequest(val);
    }
    
    public void broadcastPrepare(ProposalNum n) {
        PrepareRequest msg = new PrepareRequest(n);
        for (ObjectOutput out : acceptorOutStreams.values()) {
            try {
                out.writeObject(msg);
            } catch (IOException e) {
                System.out.println("Error writing to stream " + e);
            }
        }
    }
    
    public void broadcastAccept(Set<Address> agents, ProposalNum n, Object value) {
        AcceptRequest msg = new AcceptRequest(n, value);
        for (Address addr : agents) {
            if (acceptorOutStreams.containsKey(addr)) {
                try {
                    acceptorOutStreams.get(addr).writeObject(msg);
                } catch (IOException e) {
                    System.out.println("Error writing to stream " + e);
                }
            }
            // TODO: error handling if output stream missing
        }
    }
    
    private class ProposerStreamReader implements Runnable {
        
        private ObjectInput in;
        private Address addr;
        
        public ProposerStreamReader(ObjectInput in, Address addr) {
            this.in = in;
            this.addr = addr;
        }
        
        public void run() {
            while (true) {
                try {
                    Object msg = in.readObject();
                    if (msg instanceof Promise) {
                        System.out.println("Promise received");
                        Promise promise = (Promise) msg;
                        prepareResponse(addr, promise.getProposalNum(),
                            promise.getHighest(), promise.getValue());
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
    
    public static void main(String[] args) {
	    System.out.println("Launching proposer server...");
	    Set<Address> acceptorAddrs = new HashSet<Address>();
	    acceptorAddrs.add(new Address("localhost", 4000));
	    ProposerServer server = new ProposerServer(acceptorAddrs, "p1", 1);
	    server.start();
    }
    
}