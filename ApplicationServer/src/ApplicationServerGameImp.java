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
        return false;
    }

    @Override
    public Message subscribe(Player player) throws RemoteException {
        
    }

    @Override
    public Card drawCard() throws RemoteException {
        return null;
    }
}
