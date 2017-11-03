import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ApplicationServerController extends Remote {

    public boolean login(String username, String password) throws RemoteException;

    public void addUnoGame(UnoGame unoGame) throws RemoteException;

    public List<UnoGame> subscribe() throws  RemoteException;

    public boolean register(String username, String password) throws RemoteException;

    public boolean joinGame(Player player,UnoGame unoGame) throws RemoteException;

    public List<UnoGame> getAllUnoGames() throws RemoteException;

}