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
    private List<ApplicationToApplication> applicationToApplicationList = new ArrayList<>();
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

        try {
            Registry databaseRegistry = LocateRegistry.getRegistry("localhost", port);
            ApplicationToApplication applicationToApplication = (ApplicationToApplication) databaseRegistry.lookup("ApplicationToApplication");
            applicationToApplicationList.add(applicationToApplication);
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
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
                try {
                    Registry databaseRegistry = LocateRegistry.getRegistry("localhost", i);
                    ApplicationToApplication applicationToApplication = (ApplicationToApplication) databaseRegistry.lookup("ApplicationToApplication");
                    applicationToApplicationList.add(applicationToApplication);
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (NotBoundException e) {
                    e.printStackTrace();
                }
            }
        }

        for (ApplicationToApplication a: applicationToApplicationList
                ) {
            try {
                a.notifyNewApplicationServer(portNr);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void addUnoGameOnAllServers(UnoGame unoGame) {
        for (ApplicationToApplication a: applicationToApplicationList
                ) {
            try {
                a.notifyNewUnoGame(unoGame, portNr);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void removeUnoGameOnAllServers(UnoGame unoGame) {
        for (ApplicationToApplication a: applicationToApplicationList
                ) {
            try {
                a.notifyRemoveUnoGame(unoGame);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
