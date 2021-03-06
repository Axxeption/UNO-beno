import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Benoit on 2/11/17.
 */
public class Lobby {
    private HashMap<Long,UnoGame> unoGameHashMap;
    private List<UnoGame> otherUnoGames;

    public Lobby(){
        unoGameHashMap = new HashMap<>();
        otherUnoGames = new ArrayList<>();
    }

    public void addOtherUnoGames(List<UnoGame> unoGames){
        otherUnoGames.addAll(unoGames);
    }

    public List<UnoGame> getAllUnoGameList() {
        List<UnoGame> games = new ArrayList<>(unoGameHashMap.values());
        games.addAll(otherUnoGames);
        return games;
    }

    public List<UnoGame> getUnoGameList() {
        List<UnoGame> games = new ArrayList<>(unoGameHashMap.values());
        return games;
    }

    public UnoGame getUnoGame(long id){
        return unoGameHashMap.get(id);
    }

    public void addUnoGameToList(UnoGame unoGame){
        unoGameHashMap.put(unoGame.getId(),unoGame);
    }

    public void removeUnoGameFromList(UnoGame unoGame) {
        unoGameHashMap.remove(unoGame.getId());
    }

    public void addOtherUnoGame(UnoGame unoGame){
        otherUnoGames.add(unoGame);
    }

    public void removeOtherUnoGame(long id, ApplicationServerGameInterface applicationServerGameInterface){
        for (UnoGame u: otherUnoGames
             ) {
            if(u.getId() == id && u.getApplicationServerGameInterface().equals(applicationServerGameInterface)){
                otherUnoGames.remove(u);
            }
        }
    }
}
