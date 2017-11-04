import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

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
        myCards.add(card);
    }

    public String toString() {
        return myName;
    }

//    /public boolean removeCard(Card card) {
//        Card card1 = Arrays.stream(myCards.iterator())
//                .filter(x -> (x.getId()))
//                .findFirst()
//                .orElse(null);
//    }
}



