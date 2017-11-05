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

    @Override
    public void playCard(){
        //Does nothing
    }

    @Override
    public boolean canPlayOn(Card card){
        System.out.println("We kijken of deze nummerkaart op de bovenste kaart kan.");
        System.out.println(getColour() == card.getColour());
        if(getColour() == card.getColour()) return true;
        if(number == ((NumberCard) card).getNumber()) return true;
        return false;
    }

}
