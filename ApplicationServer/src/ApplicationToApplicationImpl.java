import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Benoit on 26/11/17.
 */
public class ApplicationToApplicationImpl extends UnicastRemoteObject implements ApplicationToApplication {
    private List<Integer> applicationToApplicationList = new ArrayList<>();
    private ApplicationServerControllerImpl applicationServerController;
    private int portNr;

    public ApplicationToApplicationImpl(ApplicationServerControllerImpl applicationServerController, int portNr) throws RemoteException {
        this.applicationServerController = applicationServerController;
        this.portNr = portNr;
    }

    /*@Override
    public void crashed
*/
    @Override
    public void notifyNewApplicationServer(Integer port) throws RemoteException {

        applicationToApplicationList.add(port);
        System.out.println("A new ApplicationServer connected on port " + port );
    }

    @Override
    public void notifyNewUnoGame(UnoGame unoGame, int portNr) throws RemoteException {
        System.out.println("New game added on other server: " + portNr + ".");
        applicationServerController.addOtherUnoGame(unoGame);
    }

    @Override
    public void notifyRemoveUnoGame(UnoGame unoGame) throws RemoteException {
        applicationServerController.removeOtherUnoGame(unoGame);
    }

    public void initialize() {
        List<Integer> applictionServers = null;
        try {
            Registry dispatcherRegistry = LocateRegistry.getRegistry("localhost", 9450);
            DispatcherInterface dispatcherInterface = (DispatcherInterface) dispatcherRegistry.lookup("Dispatcher");
            applictionServers = dispatcherInterface.getAllApplicationServers();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        for (Integer i: applictionServers
                ) {
            if(i != portNr) {
                    applicationToApplicationList.add(i);
            }
        }

        for (Integer a: applicationToApplicationList
                ) {
            try {

                Registry databaseRegistry = LocateRegistry.getRegistry("localhost", a);
                ApplicationToApplication applicationToApplication = (ApplicationToApplication) databaseRegistry.lookup("ApplicationToApplication");
                applicationToApplication.notifyNewApplicationServer(portNr);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void addUnoGameOnAllServers(UnoGame unoGame) {
        for (Integer a: applicationToApplicationList
                ) {
            try {

                Registry databaseRegistry = LocateRegistry.getRegistry("localhost", a);
                ApplicationToApplication applicationToApplication = (ApplicationToApplication) databaseRegistry.lookup("ApplicationToApplication");
                applicationToApplication.notifyNewUnoGame(unoGame, portNr);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void removeUnoGameOnAllServers(UnoGame unoGame) {
        for (Integer a: applicationToApplicationList
                ) {
            try {
                Registry databaseRegistry = LocateRegistry.getRegistry("localhost", a);
                ApplicationToApplication applicationToApplication = (ApplicationToApplication) databaseRegistry.lookup("ApplicationToApplication");
                applicationToApplication.notifyRemoveUnoGame(unoGame);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void failedApplicationSever(Integer applicationToApplication) throws RemoteException {
        System.out.println("Notification of failed server on port " + applicationToApplication +".");
        for(Integer applicationToApplication1: applicationToApplicationList){
            if(applicationToApplication1.equals(applicationToApplication)){
                applicationToApplicationList.remove(applicationToApplication1);

            }

        }
    }
}
