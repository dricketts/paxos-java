import java.util.*;

/*
 * An instance of proposer agents from the Paxos protocol.
 * Parameterized by the types of agents (A).
 */
public abstract class Proposer<A> {
    
    // Highest proposal number 
    private ProposalNum currentProposalNum;
    // Set of acceptors who have sent promises for the current proposal
    private Set<A> promised;
    // Value of highest number proposal from all acceptors who have sent
    // promises for the current proposal, or a default value if no promising
    // acceptors have accepted a value
    private Object proposalValue;
    // Highest number proposal number from all acceptors who have sent
    // promises for the current proposal, or null if there is no such proposal
    private ProposalNum highestNumFromPromised;
    // Size of a quorum
    private final int quorumSize;
    
    public Proposer(String uid, int quorumSize) {
        currentProposalNum = new ProposalNum(0, uid);
        promised = new HashSet<A>();
        this.quorumSize = quorumSize;
    }
    
    /*
     * Implements Phase 1(a) of the protocol.
     */
    public synchronized void prepareRequest(Object defaultValue) {
        promised.clear();
        proposalValue = defaultValue;
        highestNumFromPromised = null;
        currentProposalNum = currentProposalNum.increment();
        broadcastPrepare(currentProposalNum);
    }
    
    /*
     * Implements Phase 2(a) of the protocol.
     */
    public synchronized void prepareResponse(A agent, ProposalNum n, ProposalNum highest, Object value) {
        if (!n.equals(currentProposalNum) || promised.contains(agent)) {
            // old or duplicated promise - ignore
            return;
        }
        
        promised.add(agent);
        if (highest != null &&
            (highestNumFromPromised == null ||
                highest.greaterThan(highestNumFromPromised))) {
            proposalValue = value;
        }
        
        if (promised.size() >= quorumSize) {
            broadcastAccept(promised, currentProposalNum, proposalValue);
        }
    }
    
    public abstract void broadcastPrepare(ProposalNum n);
    
    public abstract void broadcastAccept(Set<A> agents, ProposalNum n, Object value);
    
}