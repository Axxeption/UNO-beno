import java.rmi.RemoteException;
import java.rmi.server.RemoteObject;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Benoit on 3/11/17.
 */
public class ApplicationServerGameImp extends UnicastRemoteObject implements ApplicationServerGameInterface{

    UnoGame unoGame;

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
        message.fillPlayers(unoGame);
        return message;
    }

    public ApplicationServerGameImp(UnoGame unoGame) throws RemoteException {
        this.unoGame = unoGame;
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
            message.setPoints(unoGame);
        }
        return message;
    }

    @Override
    public synchronized boolean drawCard(int playerId) throws RemoteException {
        Player realPlayer = unoGame.getPlayers().get(playerId);
        boolean succeed = unoGame.drawAndGoToNextPlayer(realPlayer);
        if(succeed)notifyAll();
        return succeed;
    }
}
