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
        unoGame.goToNextPlayer();
        Player player = unoGame.getCurrentPlayer();
        System.out.println("Player how will pick two: " + player);
        unoGame.draw(player, 2);
        System.out.println("Player how picked two: " + player);
    }

    @Override
    public boolean canPlayOn(Card card){
        if(getColour() == card.getColour()) return true;
        if(card instanceof PickerCard) return true;
        return false;
    }

}
