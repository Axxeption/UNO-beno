import com.sun.org.apache.regexp.internal.RE;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface ApplicationServerController extends Remote {

    public boolean login(String username, String password) throws RemoteException;

    int getNrOfOwnGames() throws RemoteException;

    public void addUnoGame(String name, int numberOfPlayers) throws RemoteException;

    public List<UnoGame> subscribe() throws  RemoteException;

    public boolean register(String username, String password) throws RemoteException;

    public int joinGame(Player player,long unoGameId) throws RemoteException;

    public List<UnoGame> getAllUnoGames() throws RemoteException;

    void setScore(int score, String username) throws RemoteException;
    ArrayList<User> getBestPlayers() throws RemoteException;

    void endOfGame(UnoGame unoGame) throws RemoteException;

    public void logout(String username) throws RemoteException;

    Integer getSessionToken(String username)  throws RemoteException;

    boolean checkSession(Integer sessionToken, String username) throws RemoteException;

    public ArrayList<Picture> getCards() throws RemoteException;
}