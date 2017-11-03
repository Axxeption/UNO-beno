import java.io.Serializable;

public class Card implements Serializable {
    // Constants for representing colours
    private static final int COLOUR_NONE = 0;
    private static final int COLOUR_GREEN = 1;
    private static final int COLOUR_BLUE = 2;
    private static final int COLOUR_RED = 3;
    private static final int COLOUR_YELLOW = 4;

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // An array of names indexed by the colours above
    public static final String[] COLOUR_NAMES = {"", "Green", "Blue", "Red", "Yellow"};
    protected int myColour;


    public Card(int colour, String id) {
        myColour = colour;
        this.id = id;
    }

    public int getColour() {
        return myColour;
    }

    public void setColour(int colour) {
        myColour = colour;
    }

    public boolean canPlayOn(Card card) {
        return (card.myColour == myColour);
    }

    public void play(UnoGame game) {
        // Default: has no effect
    }

    public String toString() {
        String result;

        result = COLOUR_NAMES[myColour];
        if (!result.isEmpty()) {
            result += " ";
        }
        return result;
    }
}
