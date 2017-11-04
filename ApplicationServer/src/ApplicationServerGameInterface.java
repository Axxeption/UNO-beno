import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Benoit on 3/11/17.
 */
public interface ApplicationServerGameInterface extends Remote {
    public boolean playCard(int playerId, Card card) throws RemoteException;
    public Message subscribe(int playerId) throws RemoteException;
    public boolean drawCard(int playerId) throws RemoteException;
}
