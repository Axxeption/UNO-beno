/**
 * Created by Benoit on 28/10/17.
 */
public class SkipCard extends Card {
    private UnoGame unoGame;

    public SkipCard(int colour, UnoGame unoGame){
        super(colour);
        this.unoGame = unoGame;
    }

    public void playCard(){
        unoGame.goToNextPlayer();
    }
}
