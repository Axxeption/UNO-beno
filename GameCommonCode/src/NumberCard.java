/**
 * Created by Benoit on 28/10/17.
 */
public class NumberCard extends Card{
    private int number;

    public NumberCard(int colour) {
        super(colour);
    }

    public NumberCard(int colour, int number){
        super(colour);
        String id = Card.COLOUR_NAMES[colour] + number;
        String path = Card.COLOUR_NAMES[colour] + "_" + number + ".png";
        this.number = number;
        setId(id);
        setPath(path);
    }

    public int getNumber() {
        return number;
    }

    public void playCard(){
        //Does nothing
    }

    @Override
    public boolean canPlayOn(Card card){
        if(getColour() == card.getColour()) return true;
        if(card instanceof NumberCard){
            if(number == ((NumberCard) card).getNumber()) return true;
        }
        return false;
    }

}
