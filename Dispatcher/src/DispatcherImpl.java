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
    private List<Integer> databases = new ArrayList<>();
    private List<Integer> applicationServers = new ArrayList<>();


    public DispatcherImpl() throws RemoteException {
    }

    @Override
    public List<Integer> getAllDatabases() throws RemoteException {
        return databases;
    }

    @Override
    public List<Integer> getAllApplicationServers() throws RemoteException {
        return applicationServers;
    }

    public void initialize() {
        startDatabaseServer("9430");
        startDatabaseServer("9431");
        startDatabaseServer("9432");
        startApplicationServer("7290");
        startApplicationServer("7291");
        startApplicationServer("7292");
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
        databases.add(Integer.parseInt(port));
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
}
