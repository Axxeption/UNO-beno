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
    private int playerId;
    String path;
    String fileSeparator ;
    String pathToCards;
    boolean yourTurn = false;


    @FXML
    private AnchorPane anchor;

    @FXML
    private HBox backgroundBox;

    @FXML
    private ImageView middleCard;

    @FXML
    private Label uwbeurt;

    @FXML
    private VBox otherplayersbox;

    @FXML
    private ImageView cardBack;

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
        fileSeparator = System.getProperty("file.separator");
        path =  System.getProperty("user.dir");
        pathToCards =  "file:" +fileSeparator + path + fileSeparator + "Client" + fileSeparator + "Cards" + fileSeparator ;
        cardBack.setImage(new Image(pathToCards + "uno_back.png"));
        cardBack.setOnMouseClicked(e -> {
            if(yourTurn){
                System.out.println("card draw");
                mainApp.drawCard();
            }
            else {
                System.out.println("Sorry je moet je beurt afwachten");
            }
        });
    }


    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public void setUI(Message gameState){
        backgroundBox.getChildren().clear();
        otherplayersbox.getChildren().clear();
        yourTurn = false;
        System.out.println("nieuwe gamestate: " + gameState);
        if(gameState.getNextPlayerId() == playerId){
            uwbeurt.setText("It's you turn!");
            yourTurn = true;
        }else {
            yourTurn = false;
            uwbeurt.setText("Just wait a minute");
        }
        int i = 0;
        for(Card card : gameState.getThisPlayersCards()){
            ImageView tmp  = new ImageView(pathToCards + card.getPath());
            tmp.setId(Integer.toString(i));
            tmp.setFitHeight(194);
            tmp.setFitWidth(140);
            tmp.setOnMouseClicked(e -> {
                System.out.println("je wilt spelen");
                if(yourTurn){
                    System.out.println("Je gaat een kaart spelen!");
                    mainApp.playCard(card);
                }
                else {
                    System.out.println("Sorry je moet je beurt afwachten");
                }
            });
            backgroundBox.getChildren().add(tmp);
            i++;
        }

        middleCard.setImage(new Image(pathToCards + gameState.getTopCard().getPath()));

        for(Integer otherPlayerCards : gameState.getNumberOfCards()){
            //hier nieuw hbox toevoegen voor image van user en hoeveel kaarten
            otherplayersbox.getChildren().add(new Label("This player has " + otherPlayerCards + " cards to play"));
        }

    }
}
