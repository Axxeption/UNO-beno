/**
 * Created by Benoit on 28/10/17.
 */
public class SkipCard extends Card {
    private UnoGame unoGame;

    public SkipCard(int colour, UnoGame unoGame){
        super(colour);
        this.unoGame = unoGame;
        String id = Card.COLOUR_NAMES[colour] + "skip";
        String path = Card.COLOUR_NAMES[colour] + "_skip.png";
        setId(id);
        setPath(path);
    }

    public void playCard(){
        unoGame.goToNextPlayer();
    }
}
