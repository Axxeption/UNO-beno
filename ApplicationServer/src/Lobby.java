import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Benoit on 2/11/17.
 */
public class Lobby {
    List<UnoGame> unoGameList;

    public Lobby(){
        unoGameList = new ArrayList<>();
    }

    public List<UnoGame> getUnoGameList() {
        return unoGameList;
    }

    public void setUnoGameList(List<UnoGame> unoGameList) {
        this.unoGameList = unoGameList;
    }

    public void addUnoGameToList(UnoGame unoGame){
        unoGameList.add(unoGame);
    }
}
