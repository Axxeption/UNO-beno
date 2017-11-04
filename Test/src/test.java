import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by Benoit on 4/11/17.
 */
public class test {
    public static void main(String[] args){
        DatabaseServerMain.main(args);
        ApplicationServerMain.main(args);

        Registry myRegistry = null;
        ApplicationServerController applicationServerController = null;
        try {
            myRegistry = LocateRegistry.getRegistry("localhost", 7280);
            applicationServerController = (ApplicationServerController) myRegistry.lookup("ClientApplicationService");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }

        long unoGameId = 0;

        //create new Uno Game;
        try {
            applicationServerController.addUnoGame("testGame", 3);
            unoGameId = applicationServerController.getAllUnoGames().get(0).getId();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        // add three players to Unogame;
        try {
            applicationServerController.joinGame(new Player("jefke"), unoGameId);
            applicationServerController.joinGame(new Player("joske"), unoGameId);
            applicationServerController.joinGame(new Player("axelleke"), unoGameId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }




    }
}
