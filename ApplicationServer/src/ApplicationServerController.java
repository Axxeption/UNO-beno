import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ApplicationServerController extends Remote {

    public boolean login(String username, String password) throws RemoteException;

    public void addUnoGame(String name, int numberOfPlayers) throws RemoteException;

    public List<UnoGame> subscribe() throws  RemoteException;

    public boolean register(String username, String password) throws RemoteException;

    public int joinGame(Player player,long unoGameId) throws RemoteException;

    public List<UnoGame> getAllUnoGames() throws RemoteException;

}