import java.rmi.RemoteException;

/**
 * Created by Benoit on 3/11/17.
 */
public class ApplicationServerGameImp implements ApplicationServerGameInterface{

    UnoGame unoGame;

    public ApplicationServerGameImp(UnoGame unoGame) {
        this.unoGame = unoGame;
    }

    @Override
    public boolean playCard(Player player, Card card) throws RemoteException {
        Player realPlayer = unoGame.getPlayers().get(player.getId());
        return unoGame.playCard(card, realPlayer);
    }

    @Override
    public synchronized Message subscribe(Player player) throws RemoteException {
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Player realPlayer = unoGame.getPlayers().get(player.getId());
        return new Message(unoGame, realPlayer);
    }

    @Override
    public synchronized boolean drawCard(Player player) throws RemoteException {
        Player realPlayer = unoGame.getPlayers().get(player.getId());
        notifyAll();
        return unoGame.drawAndGoToNextPlayer(realPlayer);
    }
}
