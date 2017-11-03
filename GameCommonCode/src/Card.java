import java.io.Serializable;

public class Card implements Serializable
{
    // Constants for representing colours
    public static final int COLOUR_NONE = 0;
    public static final int COLOUR_GREEN = 1;
    public static final int COLOUR_BLUE = 2;
    public static final int COLOUR_RED = 3;
    public static final int COLOUR_YELLOW = 4;

    // An array of names indexed by the colours above
    public static final String[] COLOUR_NAMES= {"", "Green","Blue","Red","Yellow"};
    protected int myColour;

    /**
     * Create a card with a given colour and symbol
     *
     * @param colour The card colour. Should be one of COLOUR_NONE, COLOUR_GREEN, COLOUR_BLUE, COLOUR_RED or COLOUR_YELLOW.
     */
    public Card(int colour) {
        myColour = colour;
    }

    /**
     * Get the colour of the card
     */
    public int getColour() {
        return myColour;
    }

    /**
     * Set the colour of the card
     */
    public void setColour(int colour) {
        myColour = colour;
    }


    /**
     * Check whether this card can be played on the given card.
     * @param card The card to be played on
     * @returns true if either the colours or the symbols match
     */
    public boolean canPlayOn(Card card) {
        return (card.myColour == myColour);
    }

    public void play(UnoGame game) {
        // Default: has no effect
    }

    /**
     * String representation of the card.
     */
    public String toString() {
        String result;

        result = COLOUR_NAMES[myColour];
        if (!result.isEmpty()) {
            result += " ";
        }
        return result;
    }
}
