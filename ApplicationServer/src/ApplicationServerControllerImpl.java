import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    private SQLiteController sqlitecontroller;
    private Registry applicationRegistry;
    private DispatcherInterface dispatcher;
    private User user;
    private Lobby lobby;
    private Integer sessionToken;
    private ApplicationToApplication applicationToApplication;
    private int thisApplicationServerPortNr;
    private String monthCached;
    private ArrayList<Picture> cardList;




    public ApplicationServerControllerImpl(Lobby lobby, int portNr) throws RemoteException {
        this.lobby = lobby;
        this.thisApplicationServerPortNr = portNr;
        this.monthCached = "13";
        try {
            Registry dispatcherRegistry = LocateRegistry.getRegistry("localhost", 9450);
            dispatcher = (DispatcherInterface) dispatcherRegistry.lookup("Dispatcher");
            int databasePortNr = dispatcher.whichDatabaseServerToConnect();
            applicationRegistry = LocateRegistry.getRegistry("localhost", thisApplicationServerPortNr);
            Registry databaseRegistry = LocateRegistry.getRegistry("localhost", databasePortNr);
            sqlitecontroller = (SQLiteController) databaseRegistry.lookup("DatabaseServer");
            System.out.println("ApplicationServer established a connection with the DatabaseServer " + databasePortNr +".");
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

    public synchronized void addOtherUnoGame(UnoGame unoGame){
        lobby.addOtherUnoGame(unoGame);
        notifyAll();
    }

    public synchronized void removeOtherUnoGame(UnoGame unoGame){
        lobby.removeOtherUnoGame(unoGame.getId(), unoGame.getApplicationServerGameInterface());
        notifyAll();
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
        return lobby.getAllUnoGameList();
    }

    public List<UnoGame> getOwnUnoGames(){
        return lobby.getUnoGameList();
    }

    @Override
    public int getNrOfOwnGames() throws RemoteException{
        return lobby.getUnoGameList().size();
    }

    public void addOtherUnoGames(List<UnoGame> unoGames){
        lobby.addOtherUnoGames(unoGames);
    }

    @Override
    public synchronized void addUnoGame(String name, int numberOfPlayers) {
        UnoGame unoGame = new UnoGame(numberOfPlayers, name);
        System.out.println("New UnoGame created with id " + unoGame.getId() + ".");
        try {
            String nameRemoteObject = "UnoGame" + unoGame.getId();
            ApplicationServerGameInterface applicationServerGameInterface = new ApplicationServerGameImp(unoGame, this);
            applicationRegistry.rebind(nameRemoteObject, applicationServerGameInterface);
            unoGame.setApplicationServerGameInterface(applicationServerGameInterface);
            unoGame.setApplicationServerController(this);
            applicationToApplication.addUnoGameOnAllServers(unoGame);
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
        return lobby.getAllUnoGameList();

    }

    public boolean register(String username, String password) throws RemoteException {
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


        try {
            sqlitecontroller.setScoreOnAllDatabases(score, username);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<User> getBestPlayers() throws RemoteException {

        try {
            return sqlitecontroller.getBestPlayers();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public synchronized void endOfGame(UnoGame unoGame) throws RemoteException{
        lobby.removeUnoGameFromList(unoGame);
        notifyAll();
        try {
            applicationToApplication.removeUnoGameOnAllServers(unoGame);
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
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM");
        LocalDate localDate = LocalDate.now();
        System.out.println("current month:" + dtf.format(localDate)); //2016/11/16
        try {
            if(monthCached.equals(dtf.format(localDate))){
                System.out.println("It was cached");
                return cardList;
            }else {
                if (dtf.format(localDate).equals("12") || dtf.format(localDate).equals("01")) {
                    System.out.println("Christmass time baby");
                    cardList = sqlitecontroller.getCards("christmas");
                    monthCached = dtf.format(localDate);
                    return cardList;
                } else {
                    cardList = sqlitecontroller.getCards("standard");
                    monthCached = dtf.format(localDate);
                    return cardList;
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    return null;
    }

    public void setApplicationToApplication(ApplicationToApplication applicationToApplication){
        this.applicationToApplication = applicationToApplication;
    }

}