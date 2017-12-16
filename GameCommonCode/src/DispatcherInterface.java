import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by Benoit on 15/11/17.
 */
public interface DispatcherInterface extends Remote{
    List<Integer> getAllDatabases() throws RemoteException;

    List<Integer> getAllApplicationServers() throws RemoteException;

    Integer whichDatabaseServerToConnect() throws RemoteException;

    Integer whichApplicationServerToConnect(Integer failedServer) throws RemoteException;
}
