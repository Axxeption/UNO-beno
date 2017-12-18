
import java.io.*;
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
            System.out.println("start db on port: " + port);
            String path = System.getProperty("user.dir") + "\\Logs\\Database\\log_" + port + ".txt";
            ProcessBuilder pb = new ProcessBuilder("java", "-jar", "C:\\Users\\vulst\\Documents\\School 4elict\\Gedistribueerde systemen\\UNO-beno\\out\\artifacts\\DatabaseServer_jar\\DatabaseServer.jar" , port);
//            ProcessBuilder pb = new ProcessBuilder("out/artifacts/DatabaseServer_jar/startDatabaseServer.sh", port);
            BufferedWriter pw = new BufferedWriter(new FileWriter(path));
            File log = new File(path);

            pb.redirectOutput(ProcessBuilder.Redirect.appendTo(log));
            pb.redirectErrorStream(true);
            pw.close();
            pb.start();

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
                System.out.println("A ");
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
            System.out.println("Start applicationServer on port: " + port);
            String path = System.getProperty("user.dir") + "\\Logs\\ApplicationServer\\log_" + port + ".txt";
            ProcessBuilder pb = new ProcessBuilder("java", "-jar", "C:\\Users\\vulst\\Documents\\School 4elict\\Gedistribueerde systemen\\UNO-beno\\out\\artifacts\\ApplicationServer_jar\\ApplicationServer.jar" , port);//            ProcessBuilder pb = new ProcessBuilder("out/artifacts/ApplicationServer_jar/startApplicationServer.sh", port);
            BufferedWriter pw = new BufferedWriter(new FileWriter(path));
            File log = new File(path);
            pb.redirectOutput(ProcessBuilder.Redirect.appendTo(log));
            pb.redirectErrorStream(true);
            pw.close();
            pb.start();
//            Process proc = new ProcessBuilder("out/artifacts/ApplicationServer_jar/startApplicationServer.sh", port).start();
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

    public void notifyFailedServer(Integer port){
        for(Integer i : applicationServers){

            Registry databaseRegistry = null;
            try {
                databaseRegistry = LocateRegistry.getRegistry("localhost", i);
                ApplicationToApplication applicationToApplication = (ApplicationToApplication) databaseRegistry.lookup("ApplicationToApplication");
                applicationToApplication.failedApplicationSever(port);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
            }
        }

    }

    public boolean checkIfFailedServer(Integer port){
        System.out.println("failed server check");
        try {
            Registry registry = LocateRegistry.getRegistry(port);
            ApplicationServerController applicationServerController =(ApplicationServerController) registry.lookup("ApplicationServer");
            applicationServerController.getNrOfGames();
        } catch(Exception e){
            System.out.println(e);
            notifyFailedServer(port);
            return true;
        }
        return false;
    }
}
