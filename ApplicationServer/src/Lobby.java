import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Benoit on 2/11/17.
 */
public class Lobby {
    HashMap<Long,UnoGame> unoGameHashMap;

    public Lobby(){
        unoGameHashMap = new HashMap<>();
    }

    public List<UnoGame> getUnoGameList() {
        return new ArrayList<>(unoGameHashMap.values());
    }

    public UnoGame getUnoGame(long id){
        return unoGameHashMap.get(id);
    }

    public void addUnoGameToList(UnoGame unoGame){
        unoGameHashMap.put(unoGame.getId(),unoGame);
    }
}
