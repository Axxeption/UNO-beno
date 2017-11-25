import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


/**
 *
 * @author vulst
 */
public class ApplicationServerMain {

    public int portNr;

    private void startClientServer() {
        Lobby lobby = new Lobby();
        try {
            // create on port 1099
            Registry registry = LocateRegistry.createRegistry(portNr);
            // create a new service named ClientApplicationService
            registry.rebind("ApplicationServer", new ApplicationServerControllerImpl(lobby));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("system is ready");
    }

    public ApplicationServerMain(int portNr) {
        this.portNr = portNr;
    }

    public static void main(String[] args) {
        ApplicationServerMain clientapp = new ApplicationServerMain(7280);
        clientapp.startClientServer();
    }

}
