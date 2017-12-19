import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 *
 * @author vulst
 */
public interface SQLiteController extends Remote{
    public User getUser(String username) throws ClassNotFoundException, SQLException, RemoteException;
    public boolean addNewUserToAllDatabases(String username, byte[] salt, byte[] hashedpassword)throws RemoteException;
    public void createSessionToken(String username)throws RemoteException;
    public void setScoreOnAllDatabases(int score, String username) throws RemoteException;
    ArrayList<User> getBestPlayers() throws RemoteException;
    void logout(String username) throws RemoteException;
    Integer getSessionToken(String username)  throws RemoteException;
    ArrayList<Picture> getCards(String event) throws RemoteException;
}
