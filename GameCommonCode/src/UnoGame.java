import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicLong;

/**
 * An implementation of the game of Uno.
 *
 * @author Malcolm Ryan
 * @version 23 September 2011
 */
public class UnoGame implements Serializable {
    static final AtomicLong NEXT_ID = new AtomicLong(0);
    private final long id = NEXT_ID.getAndIncrement();

    private ArrayList<Card> myDeck;
    private ArrayList<Card> myPile;
    private ArrayList<Player> myPlayers;
    private int maxNumberOfPlayers;
    private int currentNumberOfPlayers;
    private ApplicationServerGameInterface applicationServerGameInterface;
    private ApplicationServerController applicationServerController;

    private Player myCurrentPlayer;
    private int myPlayDirection;
    private String gameName;

    public int getMaxNumberOfPlayers() {
        return maxNumberOfPlayers;
    }

    public int getCurrentNumberOfPlayers() {
        return currentNumberOfPlayers;
    }

    public Player checkIfWinner() {
        for (Player p : myPlayers) {
            if (p.getCards().size() == 0) {
                return p;
            }
        }
        return null;
    }

    public Card getTopCard() {
        return myPile.get(myPile.size() - 1);
    }

    public boolean playCard(Card card, Player player) {
        System.out.println(player.getName() + " tries to play card " + card.getId());
        Card realCard = player.findPlayersCard(card);
        if (myCurrentPlayer.equals(player)) {
            System.out.println("Het is die speler zijn beurt");
            if (card.canPlayOn(getTopCard()) && realCard != null) {
                myPile.add(realCard);
                realCard.playCard();
                System.out.println("card played");
                player.removeCard(realCard);
                goToNextPlayer();
                return true;
            }
        } else {
            System.out.println("Cards don't match or it isn't this players turn");
            return false;
        }
        return false;

    }

    public UnoGame(int nPlayers, String name) {
        gameName = name;
        myDeck = new ArrayList<Card>();
        maxNumberOfPlayers = nPlayers;
        this.applicationServerGameInterface = applicationServerGameInterface;

        for(int k = 0; k < 2; k++) {
            for (int c = 1; c <= 4; c++) {
                myDeck.add(new ReverseCard(c, this));
                myDeck.add(new SkipCard(c, this));
                myDeck.add(new PickerCard(c, this));
                for (int i = 0; i <= 9; i++) {
                    myDeck.add(new NumberCard(c, i));
                }
            }
        }

        Collections.shuffle(myDeck);

        // Pile is initially empty;
        myPile = new ArrayList<Card>();
        myPlayers = new ArrayList<Player>();
        currentNumberOfPlayers = 0;
    }

    /**
     * Access the deck
     */
    public ArrayList<Card> getDeck() {
        return myDeck;
    }

    /**
     * Access the pile
     */
    public ArrayList<Card> getPile() {
        return myPile;
    }

    /**
     * Access the players
     */
    public ArrayList<Player> getPlayers() {
        return myPlayers;
    }

    public ApplicationServerGameInterface getApplicationServerGameInterface() {
        return applicationServerGameInterface;
    }

    public int addPlayer(Player player) {
        if (currentNumberOfPlayers >= maxNumberOfPlayers) {
            return -1;
        } else {
            myPlayers.add(player);

            player.setId(currentNumberOfPlayers);
            currentNumberOfPlayers++;
            if (currentNumberOfPlayers == maxNumberOfPlayers) {
                startGame();
                System.out.println("A new game is started.");
            }
            return player.getId();
        }
    }

    public long getId() {
        return id;
    }

    /**
     * Get the player whose turn it currently is.
     */
    public Player getCurrentPlayer() {
        return myCurrentPlayer;
    }

    /**
     * Get the player who is a given number of positions ahead in play (in the current direction of play).
     *
     * @returns The next Player according to the
     */
    public Player getNextPlayer() {
        int i = myPlayers.indexOf(myCurrentPlayer);
        i += myPlayDirection;
        i = i % myPlayers.size();
        if (i < 0) {
            i += myPlayers.size();
        }

        return myPlayers.get(i);
    }

    /**
     * Get the current direction of play (1 or -1)
     */
    public int getPlayDirection() {
        return myPlayDirection;
    }

    /**
     * Switch the direction of play
     */
    public void switchPlayDirection() {
        System.out.println("De richting was: " + myPlayDirection);
        myPlayDirection = myPlayDirection == -1 ? 1 : -1;
        System.out.println("De richting is nu: " + myPlayDirection);
    }

    /**
     * Change the current player to the next one in the direction of play
     */
    public void goToNextPlayer() {
        myCurrentPlayer = getNextPlayer();
    }

    /**
     * Draw cards from the deck into the hand of the given player.
     * <p>
     * If the deck does not contain enough cards, the pile will be shuffled into the deck
     * (except for the top card). If this still doesn't provide enough cards, draw as many
     * cards as are available and stop.
     *
     * @param player The player who is drawing
     * @param nCards The number of cards to draw
     */
    public void draw(Player player, int nCards) {
        System.out.println(player + " draws " + nCards + ".");

        for (int i = 0; i < nCards; i++) {
            if (myDeck.size() == 0 && myPile.size() > 0) {
                // The deck is empty, shuffle in the pile
                // but keep the top card

                Card keep = myPile.get(0);
                myDeck.addAll(myPile.subList(1, myPile.size()));
                myPile.clear();
                myPile.add(keep);
                Collections.shuffle(myDeck);
            }

            if (myDeck.size() == 0) {
                System.out.println("Geen kaarten meer te verdelen.");
                return;
            }

            player.gainCard(myDeck.remove(0));
        }
    }

    public Card drawSingleCard(Player player, int nCards) {
        System.out.println(player + " draws " + nCards + ".");

        if (myDeck.size() == 0 && myPile.size() > 0) {
            // The deck is empty, shuffle in the pile
            // but keep the top card
            Card keep = myPile.get(0);
            myDeck.addAll(myPile.subList(1, myPile.size()));
            myPile.clear();
            myPile.add(keep);
            Collections.shuffle(myDeck);
        }

        if (myDeck.size() == 0) {
            System.out.println("Geen kaarten meer te verdelen.");
            return null;
        }

        Card card = myDeck.remove(0);
        player.gainCard(card);
        return card;

    }

    public void setApplicationServerGameInterface(ApplicationServerGameInterface applicationServerGameInterface) {
        this.applicationServerGameInterface = applicationServerGameInterface;
    }

    public void setApplicationServerController(ApplicationServerController applicationServerController) {
        this.applicationServerController = applicationServerController;
    }

    public ApplicationServerController getApplicationServerController() {
        return applicationServerController;
    }

    /**
     * Deal out cards and start a new game
     */
    public void startGame() {
        // Everyone draws five cards to start
        for (Player p : myPlayers) {

            draw(p, 5);
        }
        System.out.println("Cards are distributed");
        // turn over the top card
        Card card = myDeck.remove(0);
        myPile.add(0, card);
        System.out.println("The starting card is: " + card);

        // We start from player zero, going in the positive direction
        myCurrentPlayer = myPlayers.get(0);
        myPlayDirection = 1;

    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public boolean drawAndMaybeGoToNextPlayer(Player player) {
        if (player.equals(myCurrentPlayer)) {
            Card card = drawSingleCard(player, 1);
            if(!card.canPlayOn(getTopCard())) {
                goToNextPlayer();
            }
            return true;
        }
        return false;
    }

    public int calculatePoints() {
        int score = 0;
        for (Player p : myPlayers) {
            for (Card c : p.getCards()) {
                if (c instanceof NumberCard) {
                    score += ((NumberCard) c).getNumber();
                } else {
                    score += 20;
                }
            }
        }
        return score;
    }


}
