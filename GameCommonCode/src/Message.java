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
        numberOfCards = new int[unoGame.getMaxNumberOfPlayers()];
        for(Player p: unoGame.getPlayers()){
            numberOfCards[i] = p.getCards().size();
            i++;
        }
        thisPlayersCards = player.getCards();
        topCard = unoGame.getTopCard();
    }

    public int getNextPlayerId() {
        return nextPlayerId;
    }

    public void setNextPlayerId(int nextPlayerId) {
        this.nextPlayerId = nextPlayerId;
    }

    public int[] getNumberOfCards() {
        return numberOfCards;
    }

    public void setNumberOfCards(int[] numberOfCards) {
        this.numberOfCards = numberOfCards;
    }

    public List<Card> getThisPlayersCards() {
        return thisPlayersCards;
    }

    public void setThisPlayersCards(List<Card> thisPlayersCards) {
        this.thisPlayersCards = thisPlayersCards;
    }

    public Card getTopCard() {
        return topCard;
    }

    public void setTopCard(Card topCard) {
        this.topCard = topCard;
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
