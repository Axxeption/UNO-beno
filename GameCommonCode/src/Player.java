import java.io.Serializable;
import java.util.ArrayList;

/**
 * A Player in the Uno Game.
 *
 * @author Malcolm Ryan
 * @version 23 September 2010
 */
public class Player implements Serializable
{
    private String myName;
    private ArrayList<Card> myCards;

    /**
     * Create a player. The player's hand is initially empty.
     *
     * @params The player's name
     */
    public Player(String name) {
        myName = name;
        myCards = new ArrayList<Card>();
    }

    /**
     * Access the player's name
     */
    public String getName() {
        return myName;
    }

    /**
     * Access the player's hand
     */
    public ArrayList<Card> getCards() {
        return myCards;
    }

    /**
     * Clear the player's hand
     */
    public void clearCards() {
        myCards.clear();
    }

    /**
     * Add a card to the player's hand
     *
     * @param card The card to add.
     */
    public void gainCard(Card card) {
        myCards.add(card);
    }


    /**
     * The string representation of the player
     */
    public String toString() {
        return myName;
    }
}



