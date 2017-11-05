/**
 * Created by Benoit on 28/10/17.
 */
public class ReverseCard extends Card{
        private UnoGame unoGame;

        public ReverseCard(int colour, UnoGame unoGame){
            super(colour);
            this.unoGame = unoGame;
            String id = Card.COLOUR_NAMES[colour] + "reverse";
            String path = Card.COLOUR_NAMES[colour] + "_reverse.png";
            setId(id);
            setPath(path);
        }

        public void playCard(){
            System.out.println("We wisselen van richting");
            unoGame.switchPlayDirection();
        }

        public boolean canPlayOn(Card card){
            if(getColour() == card.getColour()) return true;
            if(card instanceof ReverseCard) return true;
            return false;
        }

}
