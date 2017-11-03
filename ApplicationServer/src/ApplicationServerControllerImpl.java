import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vulst
 */
public class ApplicationServerControllerImpl extends UnicastRemoteObject implements ApplicationServerController {

    private Hashing pswd = new Hashing();
    SQLiteController impl;
    Registry myRegistry;
    ResultSet rs;
    User user;
    Lobby lobby;

    private void connectDbServer() {
        System.out.println("Connect from applicationserver to db");
        try {
            myRegistry = LocateRegistry.getRegistry("localhost", 9430);
            impl = (SQLiteController) myRegistry.lookup("DatabaseService");
            System.out.println("Connected to db");
        } catch (NotBoundException ex) {
            Logger.getLogger(ApplicationServerMain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AccessException ex) {
            Logger.getLogger(ApplicationServerMain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(ApplicationServerMain.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public ApplicationServerControllerImpl(Lobby lobby) throws RemoteException {
        this.lobby = lobby;
    }

    @Override
    public boolean login(String username, String password) throws RemoteException {
        if (impl == null) {
            connectDbServer();
        }
        try {
            user = impl.getUser(username);
            if (user == null) {
                return false;
            }
            byte[] saltdb = null;
            byte[] hashdb = null;

            saltdb = user.getSalt();
            hashdb = user.getHash();
            Timestamp date = user.getTime();
            System.out.println(date);
            char[] pass = password.toCharArray();
            if (pswd.isExpectedPassword(pass, saltdb, hashdb)) {
                impl.createSessionToken(username);
                return true;
            } else {
                return false;
            }
        } catch (ClassNotFoundException ex) {
            System.out.println(ex);
            return false;
        } catch (SQLException ex) {
            System.out.println(ex);
            return false;
        }

    }

    @Override
    public synchronized boolean joinGame(Player player,UnoGame unoGame) throws RemoteException {

        System.out.println("A new player joined: " + player.getName());
        lobby.getUnoGameList();
        notifyAll();
        return unoGame.addPlayer(player);
    }

    @Override
    public List<UnoGame> getAllUnoGames() throws RemoteException {
        return lobby.getUnoGameList();
    }

    @Override
    public synchronized void addUnoGame(UnoGame unoGame){
        lobby.addUnoGameToList(unoGame);
        notifyAll();
    }

    @Override
    public synchronized List<UnoGame>  subscribe(){
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return lobby.getUnoGameList();

    }

    public boolean register(String username, String password) throws RemoteException {
        connectDbServer();
        char[] pass = password.toCharArray();
        byte[] salt = pswd.getNextSalt();
        byte[] hashedPass = pswd.hash(pass, salt);
        //todo
        if (impl.newUser(username, salt, hashedPass)) {
            return true;
        } else {
            return false;
        }
    }
}