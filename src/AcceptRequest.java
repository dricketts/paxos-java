import java.io.Serializable;

public class AcceptRequest implements Serializable {
    
    private ProposalNum n;
    private Object value;
    
    public AcceptRequest(ProposalNum n, Object value) {
        this.n = n;
        this.value = value;
    }
    
    public ProposalNum getProposalNum() {
        return n;
    }
    
    public Object getValue() {
        return value;
    }
}