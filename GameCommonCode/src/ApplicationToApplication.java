import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Benoit on 26/11/17.
 */
public interface ApplicationToApplication extends Remote{
    void notifyNewApplicationServer(Integer port) throws RemoteException;
    void notifyNewUnoGame(UnoGame unoGame, int portNr) throws RemoteException;

    void updateAllApplicationServers(UnoGame unoGame) throws RemoteException;
}