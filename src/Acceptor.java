/*
 * An instance of an acceptor agent from the Paxos protocol.
 */
public class Acceptor<V> {
    
    // Highest numbered proposal accepted by this Acceptor
    private ProposalNum highestAccepted;
    // Highest numbered prepare request to which this Acceptor has responded
    private ProposalNum highestPrepare;
    // Accepted value
    private V value;
    
    /*
     * Implements Phase 1(b) of the protocol. 
     */
    public synchronized boolean prepareRequest(ProposalNum proposalNum) {
        if (proposalNum.greaterThan(highestPrepare)) {
            highestPrepare = proposalNum;
            return true;
        } else {
            return false;
        }
    }
    
    /*
     * Implements Phase 2(b) of the protocol.
     */
    public synchronized void acceptRequest(ProposalNum proposalNum, V value) {
        if (proposalNum.greaterThanOrEqual(highestPrepare)) {
            this.value = value;
            highestAccepted = proposalNum;
        }
    }
}
