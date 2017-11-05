import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Benoit on 3/11/17.
 */
public class Message implements Serializable {
    private int nextPlayerId;
    private List<Integer> numberOfCards;
    private List<Card> thisPlayersCards;
    private Card topCard;
    private List<String> names;
    private Integer winner;
    private int points;

    public Integer getWinner() {
        return winner;
    }
    public int getPoints() {
        return points;
    }

    public void setWinner(Integer winner) {
        this.winner = winner;
    }

    public Message(UnoGame unoGame, Player player) {
        nextPlayerId = unoGame.getCurrentPlayer().getId();
        int i = 0;
        numberOfCards = new ArrayList<>();
        for (Player p : unoGame.getPlayers()) {
            if (p.getId() != player.getId()) {
                numberOfCards.add(p.getCards().size());
            }
        }
        thisPlayersCards = player.getCards();
        topCard = unoGame.getTopCard();
        winner = null;
        fillPlayers(unoGame);
        reOrganizeListInteger(numberOfCards,player);
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public int getNextPlayerId() {
        return nextPlayerId;
    }

    public void setNextPlayerId(int nextPlayerId) {
        this.nextPlayerId = nextPlayerId;
    }

    public List<Integer> getNumberOfCards() {
        return numberOfCards;
    }

    public void setNumberOfCards(List<Integer> numberOfCards) {
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
                ", numberOfCards=" + numberOfCards +
                ", thisPlayersCards=" + thisPlayersCards +
                ", topCard=" + topCard +
                '}';
    }

    public void fillPlayers(UnoGame unoGame) {
        names = new ArrayList<>();
        int i = 0;
        for (Player p : unoGame.getPlayers()) {
            names.add(p.getName());
        }
    }

    public void reOrganizeListInteger(List<Integer> list, Player p){
        for(int i = 0; i < p.getId(); i++){
            list.add(list.remove(0));
        }
        if(list.size() > 1) {
            list.add(0, list.remove(1));
        }
    }

    public void setPoints(UnoGame unoGame) {
        points = unoGame.calculatePoints();
    }
}
