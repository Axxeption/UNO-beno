import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author vulst
 */
public class DatabaseServerMain {

    private static int portNr;

    public DatabaseServerMain(int portNr) {
        this.portNr = portNr;
    }

    private static void startServer() {
        try {
            // create on port 1099
            Registry registry = LocateRegistry.createRegistry(portNr);
            // create a new service named CounterService
            SQLiteControllerImpl sqLiteControllerImpl = new SQLiteControllerImpl(portNr);
            DatabaseToDatabaseImpl databaseToDatabaseImpl = new DatabaseToDatabaseImpl(sqLiteControllerImpl, portNr);
            sqLiteControllerImpl.setDatabaseToDatabase(databaseToDatabaseImpl);
            registry.rebind("DatabaseServer", sqLiteControllerImpl);
            registry.rebind("DatabaseToDatabase", databaseToDatabaseImpl);
            databaseToDatabaseImpl.initialize();
            System.out.println("done");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("READY.");
    }
    public static void main(String[] args) {
        System.out.println("try to start");
        for(int i = 0; i < 50; i++) System.out.println();
        System.out.println("STARTING DATABASE...");

        portNr = Integer.parseInt(args[0]);

        DatabaseServerMain databaseservermain = new DatabaseServerMain(portNr);
        databaseservermain.startServer();
                
    }
    
}
