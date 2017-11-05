import javafx.collections.ObservableList;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author vulst
 */
public class SQLiteControllerImpl extends UnicastRemoteObject implements SQLiteController {

    public SQLiteControllerImpl() throws RemoteException {
    }

    private static Connection con;
    private static boolean hasData = false;

    public void getdbConnection() throws ClassNotFoundException, SQLException {
        System.out.println("Connect to db");
        Class.forName("org.sqlite.JDBC");
        String path = System.getProperty("user.dir");
        String fileSeparator = System.getProperty("file.separator");
        con = DriverManager.getConnection("jdbc:sqlite:" + path + fileSeparator + "DatabaseServer" + fileSeparator + "userdb.sqlite"); //name of db make here
    }

    @Override
    public User getUser(String username) throws ClassNotFoundException, SQLException, RemoteException {
        if (con == null) {
            System.out.println("make connection");
            getdbConnection();
        }

        PreparedStatement prep = null;
        ResultSet res = null;
        String query = "SELECT * FROM uno_player where username = ?";
        User user;

        try {
            prep = con.prepareStatement(query);
            prep.setString(1, username);
            res = prep.executeQuery();

            if (res.next()) {
                System.out.println("res: " + res);
                user = new User(username, res.getBytes("salt"), res.getBytes("hash"), res.getInt("sessiontoken"), res.getTimestamp("time"));
                return user;
            } else {
//                System.out.println("in de verkeerde else");
                return null;
            }
        } catch (Exception e) {
            System.out.println(e);
            return null;
        } finally {
            prep.close();
            res.close();

        }
    }

    @Override
    public boolean isLogin(String username, String pass) throws SQLException, ClassNotFoundException, RemoteException {
        if (con == null) {
            getdbConnection();
            System.out.println("geen connectie...");
        }

        PreparedStatement prep = null;
        ResultSet res = null;
        String query = "SELECT * FROM uno_player where username = ? and password = ?";

        try {
            prep = con.prepareStatement(query);
            prep.setString(1, username);
            prep.setString(2, pass);
            res = prep.executeQuery();

            if (res.next()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println(e);
            return false;
        } finally {
            prep.close();
            res.close();

        }
    }

    @Override
    public boolean newUser(String username, byte[] salt, byte[] hashedpassword) throws RemoteException {
        try {
            if (con == null) {
                System.out.println("make connection to db");
                getdbConnection();
            }
            PreparedStatement prep = con.prepareStatement("INSERT INTO uno_player values(?,?,?,?,?,?,?);");
            prep.setString(2, username);
            prep.setBytes(3, hashedpassword);
            prep.setBytes(4, salt);
            prep.setInt(5, 0);
            prep.setTimestamp(6, null);
            prep.setInt(7, 0);

            prep.execute();
            return true;
        } catch (SQLException sqle) {
            System.out.println(sqle);
            return false;
        } catch (ClassNotFoundException classe) {
            System.out.println(classe);
            return false;
        }
    }

    @Override
    public void createSessionToken(String username) throws RemoteException {
        PreparedStatement prep;
        try {
            Random rand = new Random();
            int n = rand.nextInt(50000);
            prep = con.prepareStatement("UPDATE uno_player SET sessiontoken = ?, time = ?  WHERE username = ?;");
            prep.setInt(1, n);
            prep.setTimestamp(2, new java.sql.Timestamp(System.currentTimeMillis()));
            prep.setString(3, username);
//            java.sql.Time time = new java.sql.Time(Calendar.getInstance().getTime().getTime());
//            prep.setTimestamp(6, new java.sql.Timestamp(0));
//            prep.setTime(6, time);
            prep.execute();
        } catch (SQLException ex) {
            Logger.getLogger(SQLiteControllerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void setScore(int score, String username) throws RemoteException{
        //eerst de score opvragen en dan zetten
        PreparedStatement prep = null;
        try {
            prep = con.prepareStatement("UPDATE uno_player SET score = score + ? WHERE username = ?; ");
            prep.setInt(1, score);
            prep.setString(2, username);
            prep.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<User> getBestPlayers() throws RemoteException{
        if (con == null) {
            System.out.println("Connection was null, make connection");
            try {
                getdbConnection();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        PreparedStatement prep = null;
        ResultSet res = null;
        String query = "SELECT username, score FROM uno_player ORDER BY score DESC LIMIT 50 ";
        User user;
        ArrayList<User> bestPlayersList = new ArrayList<>();

        try {
            prep = con.prepareStatement(query);
            res = prep.executeQuery();

            while(res.next()){
                user = new User(res.getString("username"), res.getInt("score"));
                bestPlayersList.add(user);
            }
            return bestPlayersList;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        } finally {
            try {
                prep.close();
                res.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
