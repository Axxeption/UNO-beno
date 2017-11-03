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
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void playCard(){
        //Does nothing
    }


}
