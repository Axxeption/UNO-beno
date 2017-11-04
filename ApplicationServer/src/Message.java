import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Benoit on 3/11/17.
 */
public class Message implements Serializable{
    private int nextPlayerId;
    private int[] numberOfCards;
    private List<Card> thisPlayersCards;
    private Card topCard;

    public Message(UnoGame unoGame, Player player) {
        nextPlayerId = unoGame.getCurrentPlayer().getId();
        int i = 0;
        for(Player p: unoGame.getPlayers()){
            numberOfCards[i] = p.getCards().size();
            i++;
        }
        thisPlayersCards = player.getCards();
        topCard = unoGame.getTopCard();
    }

    @Override
    public String toString() {
        return "Message{" +
                "nextPlayerId=" + nextPlayerId +
                ", numberOfCards=" + Arrays.toString(numberOfCards) +
                ", thisPlayersCards=" + thisPlayersCards +
                ", topCard=" + topCard +
                '}';
    }
}
