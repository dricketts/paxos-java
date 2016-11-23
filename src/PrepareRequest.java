import java.io.Serializable;

public class PrepareRequest implements Serializable {
    
    private ProposalNum n;
    
    public PrepareRequest(ProposalNum n) {
        this.n = n;
    }
    
    public ProposalNum getProposalNum() {
        return n;
    }
}