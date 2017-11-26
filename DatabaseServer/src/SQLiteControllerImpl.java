import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author vulst
 */
public class SQLiteControllerImpl extends UnicastRemoteObject implements SQLiteController {

    private int portNr;
    private DatabaseToDatabaseImpl databaseToDatabaseImpl;

    public SQLiteControllerImpl(int portNr) throws RemoteException {
        this.portNr = portNr;
        try {
            getdbConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Connection con;
    private static boolean hasData = false;

    public void getdbConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        String path = System.getProperty("user.dir");
        String fileSeparator = System.getProperty("file.separator");
        con = DriverManager.getConnection("jdbc:sqlite:" + path + fileSeparator + "DatabaseServer" + fileSeparator + "userdb" + portNr + ".sqlite"); //name of db make here
        System.out.println("DatabaseServer established a connection with the DataBase.");
    }

    @Override
    public User getUser(String username) throws ClassNotFoundException, SQLException, RemoteException {
        if (con == null) {
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
//            prep.close();
//            res.close();

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

    public boolean addNewUserSingle(String username, byte[] salt, byte[] hashedpassword){
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
    public boolean addNewUserToAllDatabases(String username, byte[] salt, byte[] hashedpassword) throws RemoteException {
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

            databaseToDatabaseImpl.updateAllDatabases(username, salt, hashedpassword);

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
    public void setScoreOnAllDatabases(int score, String username) throws RemoteException{
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
        databaseToDatabaseImpl.updateAllDatabases(score, username);
    }

    public void setScoreSingle(int score, String username) throws RemoteException{
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

    public void logout(String username) throws RemoteException {
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
        try {
            prep = con.prepareStatement("UPDATE uno_player SET sessiontoken = ? WHERE username = ?; ");
            prep.setInt(1, 0);
            prep.setString(2, username);
            prep.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Integer getSessionToken(String username)  throws RemoteException{
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
        String query = "SELECT sessiontoken FROM uno_player WHERE username = ?  ";


        try {
            prep = con.prepareStatement(query);
            prep.setString(1, username);
            res = prep.executeQuery();
            Integer sessiontoken;
            while(res.next()){
                sessiontoken = res.getInt("sessiontoken");
                return sessiontoken;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public ArrayList<Picture> getCards(String event){
        if (con == null) {
            System.out.println("make connection");
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
        String query = "SELECT * FROM pictures where event = ?";

        try {
            prep = con.prepareStatement(query);
            prep.setString(1, event);
            res = prep.executeQuery();
            ArrayList<Picture> list = new ArrayList<>();
            while (res.next()) {
                BufferedImage image = ImageIO.read(res.getBinaryStream("picture"));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                javax.imageio.ImageIO.write(image, "png", baos);
                list.add(new Picture(res.getString("name") , baos.toByteArray()));
//                BufferedImage image = ImageIO.read(res.getBinaryStream("picture"));
//                File outputfile = new File("saved.png");
//                ImageIO.write(image, "png", outputfile);
            }
            return list;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public void setDatabaseToDatabase(DatabaseToDatabaseImpl databaseToDatabase) {
        this.databaseToDatabaseImpl = databaseToDatabase;
    }
}
