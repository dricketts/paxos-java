/*
 * Represents the address of a network node.
 */
public class Address {
    
    private String hostName;
    private int port;
    
    public Address(String hostName, int port) {
        this.hostName = hostName;
        this.port = port;
    }
    
    public boolean equals(Address a) {
        return hostName == a.hostName && port == a.port;
    }
    
    public String getHostName() {
        return hostName;
    }
    
    public int getPort() {
        return port;
    }
    
}