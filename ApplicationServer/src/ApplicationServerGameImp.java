import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Benoit on 3/11/17.
 */
public class ApplicationServerGameImp extends UnicastRemoteObject implements ApplicationServerGameInterface{

    UnoGame unoGame;
    ApplicationServerControllerImpl applicationServerControllerImpl;

    public ApplicationServerGameImp(UnoGame unoGame, ApplicationServerControllerImpl applicationServerControllerImpl) throws RemoteException {
        this.unoGame = unoGame;
        this.applicationServerControllerImpl = applicationServerControllerImpl;
    }

    public synchronized Message startMessage(int playerId){
        notifyAll();
        while (unoGame.getCurrentNumberOfPlayers() < unoGame.getMaxNumberOfPlayers()){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("We verzenden de nieuwe games info!");
        Player realPlayer = unoGame.getPlayers().get(playerId);
        Message message = new Message(unoGame, realPlayer);
        return message;
    }

    @Override
    public synchronized boolean playCard(int playerId, Card card) throws RemoteException {
        Player realPlayer = unoGame.getPlayers().get(playerId);
        boolean succeed = unoGame.playCard(card, realPlayer);
        if(succeed)notifyAll();
        return succeed;
    }

    @Override
    public synchronized Message subscribe(int playerId) throws RemoteException {
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Player realPlayer = unoGame.getPlayers().get(playerId);
        Message message = new Message(unoGame, realPlayer);
        if(unoGame.checkIfWinner() != null){
            message.setWinner(unoGame.checkIfWinner().getId());
            message.setPoints(unoGame);
            //applicationServerControllerImpl.endOfGame(unoGame);
        }
        return message;
    }

    @Override
    public synchronized boolean drawCard(int playerId) throws RemoteException {
        Player realPlayer = unoGame.getPlayers().get(playerId);
        boolean succeed = unoGame.drawAndMaybeGoToNextPlayer(realPlayer);
        if(succeed)notifyAll();
        return succeed;
    }
}
