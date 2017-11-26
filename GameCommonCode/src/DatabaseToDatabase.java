import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by Benoit on 25/11/17.
 */
public interface DatabaseToDatabase extends Remote{
    void notifyNewDatabase(Integer port) throws RemoteException;
    void addNewUser(String username, byte[] salt, byte[] hashedpassword) throws RemoteException;
}
