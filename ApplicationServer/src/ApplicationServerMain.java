import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


/**
 *
 * @author vulst
 */
public class ApplicationServerMain {

    private void startClientServer() {
        Lobby lobby = new Lobby();
        try {
            // create on port 1099
            Registry registry = LocateRegistry.createRegistry(7280);
            // create a new service named ClientApplicationService
            registry.rebind("ClientApplicationService", new ApplicationServerControllerImpl(lobby));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("system is ready");
    }


    public static void main(String[] args) {
        ApplicationServerMain clientapp = new ApplicationServerMain();
        clientapp.startClientServer();
    }

}
