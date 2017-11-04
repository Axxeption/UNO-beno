import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class gameroomController implements Initializable {
    MainApp mainApp ;

    @FXML
    private AnchorPane anchor;

    @FXML
    private HBox backgroundBox;

    @FXML
    private ImageView middleCard;

    public gameroomController(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //achtergrond zetten van het spel
        BackgroundImage myBI = new BackgroundImage(new Image("background.jpg", 1200, 800, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        anchor.setBackground(new Background(myBI));
    }

    //om de kaarten te testen
    public void showcards() {
        List<NumberCard> list = new ArrayList<>();
        list.add(new NumberCard(3, 5));
        list.add(new NumberCard(2, 5));
        list.add(new NumberCard(1, 5));
        list.add(new NumberCard(4, 5));
        list.add(new NumberCard(3, 2));
        String path = System.getProperty("user.dir");
        String fileSeparator = System.getProperty("file.separator");

      for (int i = 0; i < list.size(); i++) {
          System.out.println(list.get(i).getPath());
          String path2 = "file:" +fileSeparator + path + fileSeparator + "Client" + fileSeparator + "Cards" + fileSeparator + list.get(i).getPath();
          System.out.println(path2);
          ImageView tmp  = new ImageView(path2);
          tmp.setId(Integer.toString(i));
          tmp.setFitHeight(194);
          tmp.setFitWidth(140);
          int finalI1 = i;
          tmp.setOnMouseClicked(e -> {
              // TODO hier moet je dan zeggen dat je gespeeld hebt!
              System.out.println("You played card: " + finalI1); // change functionality
          });
          backgroundBox.getChildren().add(tmp);
//          setMiddleCard();
      }
    }

    @FXML
    public void updateOtherPlayer() {
        //hier krijg je update van de andere kaarten
        try {
            for (int i = 0; i < 2; i++) {
                System.out.println("bckground: " + backgroundBox);
                backgroundBox.getChildren().add(new Label("test"));
            }
        }catch(Exception e){
            System.out.println("error: " + e);
        }
    }
}
