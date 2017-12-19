import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by Benoit on 26/11/17.
 */
public interface ApplicationToApplication extends Remote{
    void notifyNewApplicationServer(Integer port) throws RemoteException;
    void notifyNewUnoGame(UnoGame unoGame, int portNr) throws RemoteException;

    void notifyRemoveUnoGame(UnoGame unoGame) throws RemoteException;

    void addUnoGameOnAllServers(UnoGame unoGame) throws RemoteException;

    void removeUnoGameOnAllServers(UnoGame unoGame) throws RemoteException;

    void failedApplicationSever(Integer applicationToApplication) throws RemoteException;

    List<UnoGame> getOwnUnoGames() throws RemoteException;
}
