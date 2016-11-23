/*
 * An instance of an acceptor agent from the Paxos protocol.
 * Parameterized by the types of agents (A) and chosen values (V).
 */
public abstract class Acceptor<A> {
    
    // Highest numbered prepare request to which this Acceptor has responded
    private ProposalNum highestPrepare;
    // Highest numbered proposal this Acceptor has accepted
    private ProposalNum highestAccepted;
    // Accepted value
    private Object value;
    
    /*
     * Implements Phase 1(b) of the protocol. 
     */
    public synchronized void prepareRequest(ProposalNum proposalNum, A agent) {
        if (highestPrepare == null || proposalNum.greaterThan(highestPrepare)) {
            highestPrepare = proposalNum;
            sendPromise(agent, proposalNum, highestAccepted, value);
        }
    }
    
    /*
     * Implements Phase 2(b) of the protocol.
     */
    public synchronized void acceptRequest(ProposalNum proposalNum, Object value) {
        if (highestPrepare == null || proposalNum.greaterThanOrEqual(highestPrepare)) {
            this.value = value;
            System.out.println("Value accepted: " + this.value);
        }
    }
    
    public abstract void sendPromise(A agent, ProposalNum n, ProposalNum highest, Object value);
    
}
