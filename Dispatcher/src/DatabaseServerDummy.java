/**
 * Created by Benoit on 26/11/17.
 */
public class DatabaseServerDummy {
    private Integer port;
    private int nrOfConnections;

    public DatabaseServerDummy(Integer port) {
        this.port = port;
        nrOfConnections = 0;
    }

    public Integer getPort() {
        return port;
    }

    public int getNrOfConnections() {
        return nrOfConnections;
    }

    public void incrementNrOfConnections(){
        nrOfConnections ++;
    }

}
