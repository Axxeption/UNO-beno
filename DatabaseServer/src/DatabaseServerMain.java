import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

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
    private static List<Integer> otherServers;

    public DatabaseServerMain(int portNr) {
        this.portNr = portNr;
    }

    private static void startServer() {
        try {
            // create on port 1099
            Registry registry = LocateRegistry.createRegistry(portNr);
            // create a new service named CounterService
            registry.rebind("DatabaseServer", new SQLiteControllerImpl());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Databse is ready");
    }
    public static void main(String[] args) {

        /*portNr = Integer.parseInt(args[0]);

        for(int i = 1 ; i < args.length; i++) {
            otherServers.add(Integer.parseInt(args[i]));
        }*/

        DatabaseServerMain databaseservermain = new DatabaseServerMain(7280);
        databaseservermain.startServer();
                
    }
    
}
