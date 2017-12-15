
import java.io.*;
import java.rmi.RemoteException;
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
            System.out.println("start db on port: " + port);
            String path = System.getProperty("user.dir") + "\\Logs\\Database\\log_" + port + ".txt";
            ProcessBuilder pb = new ProcessBuilder("java", "-jar", "C:\\Users\\vulst\\Documents\\School 4elict\\Gedistribueerde systemen\\UNO-beno\\out\\artifacts\\DatabaseServer_jar\\DatabaseServer.jar" , port);
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
        databases.add(Integer.parseInt(port));
    }

    public void startApplicationServer(String port){
        try {
            System.out.println("Start applicationServer on port: " + port);
            String path = System.getProperty("user.dir") + "\\Logs\\ApplicationServer\\log_" + port + ".txt";
            ProcessBuilder pb = new ProcessBuilder("java", "-jar", "C:\\Users\\vulst\\Documents\\School 4elict\\Gedistribueerde systemen\\UNO-beno\\out\\artifacts\\ApplicationServer_jar\\ApplicationServer.jar" , port);
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
}
