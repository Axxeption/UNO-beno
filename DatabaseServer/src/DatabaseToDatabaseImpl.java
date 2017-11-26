import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Benoit on 25/11/17.
 */
public class DatabaseToDatabaseImpl extends UnicastRemoteObject implements DatabaseToDatabase {
    private List<DatabaseToDatabase> databaseToDatabaseList = new ArrayList<>();
    private SQLiteControllerImpl sqLiteController;
    private int portNr;

    public DatabaseToDatabaseImpl(SQLiteControllerImpl sqLiteController, int portNr) throws RemoteException {
        this.sqLiteController = sqLiteController;
        this.portNr = portNr;
    }



    @Override
    public void notifyNewDatabase(Integer port) throws RemoteException {

        try {
            Registry databaseRegistry = LocateRegistry.getRegistry("localhost", port);
            DatabaseToDatabase databaseToDatabase = (DatabaseToDatabase) databaseRegistry.lookup("DatabaseToDatabase");
            databaseToDatabaseList.add(databaseToDatabase);
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.println("A new database connected on port " + port );
    }

    @Override
    public void addNewUser(String username, byte[] salt, byte[] hashedpassword) throws RemoteException{
        System.out.println("New player from other DatabaseServer.");
        sqLiteController.addNewUserSingle(username, salt, hashedpassword);
    }

    public void initialize() {
        List<Integer> databases = null;
        try {
            Registry dispatcherRegistry = LocateRegistry.getRegistry("localhost", 9450);
            DispatcherInterface dispatcherInterface = (DispatcherInterface) dispatcherRegistry.lookup("Dispatcher");
            databases = dispatcherInterface.getAllDatabases();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        for (Integer i: databases
             ) {
            if(i != portNr) {
                try {
                    Registry databaseRegistry = LocateRegistry.getRegistry("localhost", i);
                    DatabaseToDatabase databaseToDatabase = (DatabaseToDatabase) databaseRegistry.lookup("DatabaseToDatabase");
                    databaseToDatabaseList.add(databaseToDatabase);
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (NotBoundException e) {
                    e.printStackTrace();
                }
            }
        }

        for (DatabaseToDatabase d: databaseToDatabaseList
             ) {
            try {
                d.notifyNewDatabase(portNr);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

    }

    public void updateAllDatabases(User u){

    }

    public void updateAllDatabases(String username, byte[] salt, byte[] hashedpassword) {
        for (DatabaseToDatabase d: databaseToDatabaseList
                ) {
            try {
                d.addNewUser(username, salt, hashedpassword);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
