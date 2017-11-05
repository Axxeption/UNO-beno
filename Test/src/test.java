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

        ApplicationServerGameInterface applicationServerGameInterface;
        int playerId1;
        int playerId2;
        int playerId3;

        // add three players to Unogame;
        try {
            applicationServerGameInterface = (ApplicationServerGameInterface) myRegistry.lookup("UnoGame" + unoGameId);
            playerId1 = applicationServerController.joinGame(new Player("jefke"), unoGameId);
            playerId2 = applicationServerController.joinGame(new Player("joske"), unoGameId);
            playerId3 = applicationServerController.joinGame(new Player("axelleke"), unoGameId);

            System.out.println(applicationServerGameInterface.startMessage(playerId1));
            System.out.println(applicationServerGameInterface.startMessage(playerId2));

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }




    }
}
