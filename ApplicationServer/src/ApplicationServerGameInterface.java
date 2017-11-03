import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Benoit on 3/11/17.
 */
public interface ApplicationServerGameInterface extends Remote {
    public boolean playCard(Player player, Card card) throws RemoteException;
    public Message subscribe(UnoGame unoGame) throws RemoteException;
    public Card drawCard() throws RemoteException;
}
