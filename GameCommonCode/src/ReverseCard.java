/**
 * Created by Benoit on 28/10/17.
 */
public class ReverseCard extends Card{
        private UnoGame unoGame;

        public ReverseCard(int colour, UnoGame unoGame){
            super(colour);
            this.unoGame = unoGame;
        }

        public void playCard(){
            unoGame.switchPlayDirection();
        }

}
