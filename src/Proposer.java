/*
 * An instance of proposer agents from the Paxos protocol.
 * Parameterized by the types of agents (A) and chosen values (V).
 */
public class Proposer<A,V> {
    
    // Highest proposal number 
    private ProposalNum currentProposalNum;
    // Set of acceptors who have sent promises for the current proposal
    private Set<A> promised;
    // Value of highest number proposal from all acceptors who have sent
    // promises for the current proposal, or a default value if no promising
    // acceptors have accepted a value
    private V proposalValue;
    // Highest number proposal number from all acceptors who have sent
    // promises for the current proposal, or null if there is no such proposal
    private ProposalNum highestNumFromPromised;
    // Class for sending messages to other agents
    private Messenger messenger;
    // Size of a quorum
    private final int quorumSize;
    
    public Proposer(Messenger messenger, String uid, int quorumSize) {
        currentProposalNum = new ProposalNum(0, uid);
        promised = new HashSet<A>();
        this.messenger = messenger;
        this.quorumSize = quorumSize;
    }
    
    /*
     * Implements Phase 1(a) of the protocol.
     */
    public void prepareRequest(V defaultValue) {
        promised.clear();
        proposalValue = defaultValue;
        highestNumFromPromised = null;
        currentProposalNum = currentProposalNum.increment();
        messenger.broadcastPrepare(currentProposalNum);
    }
    
    /*
     * Implements Phase 2(a) of the protocol.
     */
    public void prepareResponse(A agent, ProposalNum n, ProposalNum highest, V value) {
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
    
}