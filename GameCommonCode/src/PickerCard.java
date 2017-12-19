public class PickerCard extends Card {
    UnoGame unoGame;

    public PickerCard(int colour, UnoGame unoGame){
        super(colour);
        String id = Card.COLOUR_NAMES[colour] + "picker";
        String path = Card.COLOUR_NAMES[colour] + "_picker.png";
        setId(id);
        setPath(path);
        this.unoGame = unoGame;
    }

    @Override
    public void playCard(){
        unoGame.draw(unoGame.getNextPlayer(), 2);
        unoGame.goToNextPlayer();
    }

    @Override
    public boolean canPlayOn(Card card){
        if(getColour() == card.getColour()) return true;
        if(card instanceof PickerCard) return true;
        return false;
    }

}
