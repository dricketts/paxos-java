import java.io.Serializable;

public class Promise implements Serializable {
    
    private ProposalNum n;
    private ProposalNum highest;
    private Object value;
    
    public Promise(ProposalNum n, ProposalNum highest, Object value) {
        this.n = n;
        this.highest = highest;
        this.value = value;
    }
    
    public ProposalNum getProposalNum() {
        return n;
    }
    
    public ProposalNum getHighest() {
        return highest;
    }
    
    public Object getValue() {
        return value;
    }
}