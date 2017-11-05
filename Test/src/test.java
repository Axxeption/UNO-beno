import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by Benoit on 4/11/17.
 */
public class test {
    public static void main(String[] args) throws Exception {
        DatabaseServerMain.main(args);
        ApplicationServerMain.main(args);

        Registry myRegistry = null;
        myRegistry = LocateRegistry.getRegistry("localhost", 7280);
        final ApplicationServerController applicationServerController = (ApplicationServerController) myRegistry.lookup("ClientApplicationService");


        long unoGameId = 0;

        //create new Uno Game;
        try {
            applicationServerController.addUnoGame("testGame", 3);
            unoGameId = applicationServerController.getAllUnoGames().get(0).getId();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        final int playerId1 = 0;
        int playerId2;
        int playerId3;

        // add three players to Unogame;
        final ApplicationServerGameInterface applicationServerGameInterface = (ApplicationServerGameInterface) myRegistry.lookup("UnoGame" + unoGameId);
        applicationServerController.joinGame(new Player("jefke"), unoGameId);
        playerId2 = applicationServerController.joinGame(new Player("joske"), unoGameId);
        playerId3 = applicationServerController.joinGame(new Player("axelleke"), unoGameId);

        System.out.println(applicationServerGameInterface.startMessage(playerId1));
        System.out.println(applicationServerGameInterface.startMessage(playerId2));
        System.out.println(applicationServerGameInterface.startMessage(playerId3));


        Runnable updateGame = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        System.out.println(applicationServerGameInterface.subscribe(playerId1));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        new Thread(updateGame).start();

        Card card1 = new NumberCard(1, 5);
        Card card2 = new NumberCard(2, 5);
        Card card3 = new NumberCard(3, 5);
        Card card4 = new NumberCard(4, 5);

        try {
            System.out.println(applicationServerGameInterface.drawCard(0));
            System.out.println(applicationServerGameInterface.drawCard(1));
            System.out.println(applicationServerGameInterface.drawCard(2));
            System.out.println(applicationServerGameInterface.playCard(0, card1));
            System.out.println(applicationServerGameInterface.playCard(0, card2));
            System.out.println(applicationServerGameInterface.playCard(0, card3));
            System.out.println(applicationServerGameInterface.playCard(0, card4));

        } catch (RemoteException e) {
            e.printStackTrace();
        }


    }
}
