/*
 * An instance of an acceptor agent from the Paxos protocol.
 * Parameterized by the types of agents (A) and chosen values (V).
 */
public class Acceptor<A,V> {
    
    // Highest numbered prepare request to which this Acceptor has responded
    private ProposalNum highestPrepare;
    // Highest numbered proposal this Acceptor has accepted
    private ProposalNum highestAccepted;
    // Accepted value
    private V value;
    // Class for sending messages to other agents
    private Messenger messenger;
    
    /*
     * Creates a new acceptor with the given messenger to communicate
     * with other agents.
     */
    public Acceptor(Messenger messenger) {
        this.messenger = messenger;
    }
    
    /*
     * Implements Phase 1(b) of the protocol. 
     */
    public synchronized void prepareRequest(ProposalNum proposalNum, A agent) {
        if (highestPrepare == null || proposalNum.greaterThan(highestPrepare)) {
            highestPrepare = proposalNum;
            messenger.sendPromise(agent, proposalNum, highestAccepted, value);
        }
    }
    
    /*
     * Implements Phase 2(b) of the protocol.
     */
    public synchronized void acceptRequest(ProposalNum proposalNum, V value) {
        if (highestPrepare == null || proposalNum.greaterThanOrEqual(highestPrepare)) {
            this.value = value;
        }
    }
}
