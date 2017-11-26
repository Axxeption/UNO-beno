import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


/**
 *
 * @author vulst
 */
public class ApplicationServerMain {

    private int portNr;

    private void startClientServer() {
        Lobby lobby = new Lobby();
        try {
            // create on port 1099
            Registry registry = LocateRegistry.createRegistry(portNr);
            // create a new service named ClientApplicationService
            ApplicationServerControllerImpl applicationServerController = new ApplicationServerControllerImpl(lobby, portNr);
            registry.rebind("ApplicationServer", applicationServerController);
            ApplicationToApplicationImpl applicationToApplication = new ApplicationToApplicationImpl(applicationServerController, portNr);
            registry.rebind("ApplicationToApplication", applicationToApplication);
            applicationToApplication.initialize();
            applicationServerController.setApplicationToApplication(applicationToApplication);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("READY.");
    }

    public ApplicationServerMain(int portNr) {
        this.portNr = portNr;
    }

    public static void main(String[] args) {
        for(int i = 0; i < 50; i++) System.out.println();
        System.out.println("STARTING APPLICATIONSERVER...");

        int port = Integer.parseInt(args[0]);

        ApplicationServerMain clientapp = new ApplicationServerMain(port);
        clientapp.startClientServer();
    }

}
