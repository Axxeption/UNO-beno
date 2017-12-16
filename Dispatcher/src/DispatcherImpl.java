import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * Created by Benoit on 15/11/17.
 */
public class DispatcherImpl extends UnicastRemoteObject implements DispatcherInterface {

    private List<DatabaseServerDummy> databases = new ArrayList<>();
    private List<Integer> applicationServers = new ArrayList<>();

    final int MAXUNOGAMES = 2;

    public DispatcherImpl() throws RemoteException {
    }

    @Override
    public List<Integer> getAllDatabases() throws RemoteException {
        List<Integer> list = new ArrayList<>();
        for(DatabaseServerDummy d: databases) list.add(d.getPort());
        return list;
    }

    @Override
    public List<Integer> getAllApplicationServers() throws RemoteException {
        return applicationServers;
    }

    public void initialize() {
        startDatabaseServer("9430");
        startDatabaseServer("9431");
        startApplicationServer("7290");
        startApplicationServer("7291");
    }

    public void startDatabaseServer(String port){
        try {
            Process proc = new ProcessBuilder("out/artifacts/DatabaseServer_jar/startDatabaseServer.sh", port).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        databases.add(new DatabaseServerDummy(Integer.parseInt(port)));
    }

    @Override
    public Integer whichDatabaseServerToConnect(){
        DatabaseServerDummy database = databases.get(0);
        for(DatabaseServerDummy d: databases){
            if(d.getNrOfConnections() <= database.getNrOfConnections()) database = d;
        }
        database.incrementNrOfConnections();
        return database.getPort();

    }

    @Override
    public Integer whichApplicationServerToConnect(Integer failedServer) throws RemoteException {

        System.out.println(failedServer);
        if(failedServer != null){
            if(checkIfFailedServer(failedServer)){
                applicationServers.remove(failedServer);
                System.out.println("remove");
            }
        }

        int server = 0;

        for(Integer i: applicationServers){
            System.out.println(i);
            Registry registry = LocateRegistry.getRegistry("localhost", i);
            try {
                ApplicationServerController applicationServerController = (ApplicationServerController) registry.lookup("ApplicationServer");
                int newMin = applicationServerController.getNrOfGames();
                if(newMin <= MAXUNOGAMES) return i;

            } catch (NotBoundException e) {
                e.printStackTrace();
            }
        }
        int portnr = applicationServers.get(applicationServers.size() - 1) + 1;
        startApplicationServer(Integer.toString(portnr));
        return portnr;
    }

    public void startApplicationServer(String port){
        try {
            Process proc = new ProcessBuilder("out/artifacts/ApplicationServer_jar/startApplicationServer.sh", port).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        applicationServers.add(Integer.parseInt(port));
    }

    public boolean checkIfFailedServer(Integer port){
        try {
            Registry registry = LocateRegistry.getRegistry(port);
            registry.lookup("ApplicationServer");
        } catch( Exception e){
            System.out.println(e);
            return true;
        }
        return false;
    }
}
