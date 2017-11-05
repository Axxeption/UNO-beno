import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

/**
 * A Player in the Uno Game.
 *
 * @author Malcolm Ryan
 * @version 23 September 2010
 */
public class Player implements Serializable
{
    private String myName;
    private int id;
    private ArrayList<Card> myCards;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Player(String name) {
        myName = name;
        myCards = new ArrayList<Card>();
    }

    public String getName() {
        return myName;
    }

    public ArrayList<Card> getCards() {
        return myCards;
    }

    public void clearCards() {
        myCards.clear();
    }

    public void gainCard(Card card) {
        System.out.println("Player " + myName + " draws card " + card);
        myCards.add(card);
    }

    public String toString() {
        return myName;
    }

    public void removeCard(Card card){
        myCards.remove(card);
    }

    public Card findPlayersCard(Card card) {
        for(Card c : myCards){
            if(c.getId().equals(card.getId())){

                System.out.println("kaart gevonden in player");
                return c;
            }
        }
        System.out.println("This player doesn't have this card.");
        return null;
    }
}



