import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author vulst
 */
public class ApplicationServerControllerImpl extends UnicastRemoteObject implements ApplicationServerController {

    private Hashing pswd = new Hashing();
    SQLiteController sqlitecontroller;
    Registry databaseRegistry;
    Registry applicationRegistry;
    ResultSet rs;
    User user;
    Lobby lobby;
    private Integer sessionToken;

    private void connectDbServer() {
        try {
            databaseRegistry = LocateRegistry.getRegistry("localhost", 9430);
            sqlitecontroller = (SQLiteController) databaseRegistry.lookup("DatabaseServer");
            System.out.println("ApplicationServer established a connection with the DatabaseServer.");
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
        try {
            applicationRegistry = LocateRegistry.getRegistry("localhost", 7290);
            databaseRegistry = LocateRegistry.getRegistry("localhost", 9430);
            sqlitecontroller = (SQLiteController) databaseRegistry.lookup("DatabaseServer");
            System.out.println("ApplicationServer established a connection with the DatabaseServer.");
        } catch (NotBoundException ex) {
            Logger.getLogger(ApplicationServerMain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AccessException ex) {
            Logger.getLogger(ApplicationServerMain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(ApplicationServerMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean login(String username, String password) throws RemoteException {
        if (sqlitecontroller == null) {
            connectDbServer();
        }
        try {
            user = sqlitecontroller.getUser(username);
            if (user == null) {
                return false;
            }
            byte[] saltdb = null;
            byte[] hashdb = null;

            saltdb = user.getSalt();
            hashdb = user.getHash();
            Timestamp date = user.getTime();
            char[] pass = password.toCharArray();
            if (pswd.isExpectedPassword(pass, saltdb, hashdb)) {
                sqlitecontroller.createSessionToken(username);
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
    public synchronized int joinGame(Player player, long unoGameId) throws RemoteException {

        System.out.println("Player " + player.getName() + "joined in on game with id " + unoGameId + ".");
        UnoGame unoGame = lobby.getUnoGame(unoGameId);
        notifyAll();
        return unoGame.addPlayer(player);
    }

    @Override
    public List<UnoGame> getAllUnoGames() throws RemoteException {
        return lobby.getUnoGameList();
    }

    @Override
    public synchronized void addUnoGame(String name, int numberOfPlayers) {
        UnoGame unoGame = new UnoGame(numberOfPlayers, name);
        System.out.println("New UnoGame created with id " + unoGame.getId() + ".");
        try {
            String nameRemoteObject = "UnoGame" + unoGame.getId();
            applicationRegistry.rebind(nameRemoteObject, new ApplicationServerGameImp(unoGame, this));
        } catch (RemoteException e) {
            e.printStackTrace();

        }
        lobby.addUnoGameToList(unoGame);
        notifyAll();
    }

    @Override
    public synchronized List<UnoGame> subscribe() {
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return lobby.getUnoGameList();

    }

    public boolean register(String username, String password) throws RemoteException {
        if (sqlitecontroller == null) {
            connectDbServer();
        }
        char[] pass = password.toCharArray();
        byte[] salt = pswd.getNextSalt();
        byte[] hashedPass = pswd.hash(pass, salt);
        //todo
        if (sqlitecontroller.addNewUserToAllDatabases(username, salt, hashedPass)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setScore(int score, String username) throws RemoteException {
        //verhoog score
        if (databaseRegistry == null) {
            connectDbServer();
        }

        try {
            sqlitecontroller.setScore(score, username);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<User> getBestPlayers() throws RemoteException {
        if (databaseRegistry == null) {
            connectDbServer();
        }
        try {
            return sqlitecontroller.getBestPlayers();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public synchronized void endOfGame(UnoGame unoGame) {
        lobby.removeUnoGameFromList(unoGame);
        try {
            if (applicationRegistry == null) {
                applicationRegistry.unbind("UnoGame" + unoGame.getId());
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        notifyAll();
    }

    @Override
    public void logout(String username) throws RemoteException{
        sqlitecontroller.logout(username);
    }

    @Override
    public Integer getSessionToken(String username)  throws RemoteException{
        return sqlitecontroller.getSessionToken(username);
    }
    @Override
    public boolean checkSession(Integer sessionToken, String username) throws RemoteException{
        try {
            user = sqlitecontroller.getUser(username);
            this.sessionToken = sqlitecontroller.getSessionToken(username);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (user == null) {
            return false;
        }

        Timestamp date = user.getTime();
        //check sessiontoken and time
        if(this.sessionToken == sessionToken){
            //deze moet het zijn
            Timestamp timestamp1 = new Timestamp(new Date().getTime() + (1000 * 60 * 60 * 24));
            if(user.getTime().after(timestamp1)){
                return false;
            }
        }
        return true;

    }

    @Override
    public ArrayList<Picture> getCards(){
        //hier kan je checken al het een speciale dag is en dan de juiste kaarten opvragen
        try {
            return sqlitecontroller.getCards("standard");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    return null;
    }

}