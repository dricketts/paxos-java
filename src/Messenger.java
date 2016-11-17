/*
 * An interface for communicating between agents (of type A).
 * Sends values of type V.
 */
public interface Messenger<A,V> {
    
    public void sendPromise(A agent, ProposalNum n, ProposalNum highest, V value);
    
    public void broadcastPrepare(ProposalNum n);
    
}