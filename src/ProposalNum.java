import java.io.Serializable;

/*
 * Implements unique proposal numbers for Paxos.
 */
public class ProposalNum implements Serializable {
    
    private int num;
    private String uid;
    
    public ProposalNum(int num, String uid) {
        this.num = num;
        this.uid = uid;
    }
    
    /*
     * Returns a negative, zero, or positive result if this proposal number
     * is less than, equal to, or greater than rhs.
     */
    public int compare(ProposalNum rhs) {
        if (equals(rhs)) {
            return 0;
        } else if (num < rhs.num || (num == rhs.num && uid.compareTo(rhs.uid) < 0)) {
            return -1;
        } else {
            return 1;
        }
    }
    
    /*
     * Returns true iff this is greater than rhs.
     */
    public boolean greaterThan(ProposalNum rhs) {
        return compare(rhs) > 0;
    }
    
    /*
     * Returns true iff this is greater than or equal to rhs.
     */
    public boolean greaterThanOrEqual(ProposalNum rhs) {
        return compare(rhs) >= 0;
    }
    
    /*
     * Returns true iff this proposal number equals rhs.
     */
    public boolean equals(ProposalNum rhs) {
        return num == rhs.num && uid.equals(rhs.uid);
    }
    
    /*
     * Returns next highest proposal number for the given uid
     */
    public ProposalNum increment() {
        // TODO: handle integer overflow
        return new ProposalNum(num + 1, uid);
    }
    
    public String toString() {
        return uid + "," + num;
    }
    
}
