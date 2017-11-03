import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;


/**
 *
 * @author vulst
 */
public interface SQLiteController extends Remote{
    public User getUser(String username) throws ClassNotFoundException, SQLException, RemoteException;
    public boolean isLogin(String username, String pass) throws SQLException, ClassNotFoundException, RemoteException;
    public boolean newUser(String username, byte[] salt, byte[] hashedpassword)throws RemoteException;
    public void createSessionToken(String username)throws RemoteException;


}
