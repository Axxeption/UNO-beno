import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class gameroomController implements Initializable {
    MainApp mainApp ;
    private int playerId;
    String path;
    String fileSeparator ;
    String pathToCards;
    boolean yourTurn = false;
    Scene secondScene;
    Stage secondStage;
    private String username;



    @FXML
    private AnchorPane anchor;

    @FXML
    private HBox backgroundBox;

    @FXML
    private Label thisPlayer;

    @FXML
    AnchorPane otherPlayersPane;

    @FXML
    private ImageView middleCard;

    @FXML
    private ImageView cardBack;

    public gameroomController(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //achtergrond zetten van het spel
        BackgroundImage myBI = new BackgroundImage(new Image("Images/background.jpg", 1200, 800, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        anchor.setBackground(new Background(myBI));
        fileSeparator = System.getProperty("file.separator");
        path =  System.getProperty("user.dir");
        pathToCards =  "Images" + fileSeparator ;
        //todo zoeken waar hij ze zet en ze daar ophalen!
        thisPlayer.setText(username);
        thisPlayer.setTextFill(Color.RED);
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

    public void setLoading(){
        backgroundBox.getChildren().clear();
        cardBack.setImage(new Image("loading.gif"));
        cardBack.setFitHeight(400);
        cardBack.setFitWidth(400);
        cardBack.setLayoutX(400);
        cardBack.setLayoutY(200);
    }

    public void stopLoading(){
        cardBack.setImage(new Image(pathToCards + "uno_back.png"));
        cardBack.setLayoutX(600);
        cardBack.setLayoutY(325);
        cardBack.setFitWidth(100);
//        cardBack.setFitHeight(200);
    }


//    public void setLoading() {
//        anchor.getChildren().clear();
//
//        ImageView loadergif = new ImageView(new Image("loading.gif"));
//        loadergif.setFitHeight(400);
//        loadergif.setFitWidth(400);
//        VBox secondaryLayout = new VBox(10);
//        secondaryLayout.getChildren().addAll(loadergif);
//
//        //set "popup" if clicked on new game...
//        secondScene = new Scene(secondaryLayout, 400, 400);
//        secondStage = new Stage();
//        secondStage.setTitle("Initiate game");
//        secondStage.setScene(secondScene);
//        secondStage.show();
//    }


    public void setUI(Message gameState){
        backgroundBox.getChildren().clear();
        backgroundBox.setSpacing(-40);
        yourTurn = false;
        System.out.println("nieuwe gamestate: " + gameState);
        if(gameState.getNextPlayerId() == playerId){
            thisPlayer.setTextFill(Color.GREEN);
            yourTurn = true;
        }else {
            yourTurn = false;
            thisPlayer.setTextFill(Color.RED);
        }
        int i = 0;
        for(Card card : gameState.getThisPlayersCards()){
            ImageView tmp  = new ImageView(pathToCards + card.getPath());
            tmp.setId(Integer.toString(i));
            tmp.setFitHeight(180);
            tmp.setFitWidth(120);
            tmp.setOnMouseClicked(e -> {
                if(yourTurn){
                    mainApp.playCard(card);
                }
            });
            backgroundBox.getChildren().add(tmp);
            i++;
        }

        middleCard.setImage(new Image(pathToCards + gameState.getTopCard().getPath()));
        int k = 0;

        for(Integer otherPlayerCards : gameState.getNumberOfCards()){
            System.out.println(otherPlayersPane);
            Pane otherPlayerBox = null;
            if(otherPlayersPane.getChildren().get(k) instanceof HBox) {
                HBox otherPlayerBoxDummy = (HBox) otherPlayersPane.getChildren().get(k);
                otherPlayerBoxDummy.setSpacing(-40);
                otherPlayerBox = otherPlayerBoxDummy;
            }
            else {
                VBox otherPlayerBoxDummmy = (VBox) otherPlayersPane.getChildren().get(k);
                otherPlayerBoxDummmy.setSpacing(-90);
                otherPlayerBox = otherPlayerBoxDummmy;
            }
            otherPlayerBox.getChildren().clear();
            for (int c = 0; c < otherPlayerCards; c++){
                ImageView tmp  = new ImageView(pathToCards + "uno_back.png");
                tmp.setId(Integer.toString(i));
                tmp.setFitHeight(180);
                tmp.setFitWidth(120);
                otherPlayerBox.getChildren().add(tmp);
            }
            k++;
        }

    }

    public void endGameWinner(UnoGame unoGame) {
        //je hebt het spel gewonnen
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Winner!");
        alert.setHeaderText(null);
        alert.setContentText("Congratulations! You won this game!");
        alert.initModality(Modality.APPLICATION_MODAL); /* *** */
        alert.initOwner(mainApp.getPrimaryStage());
        mainApp.showLobby();
        alert.showAndWait();
        try {
            unoGame.getApplicationServerController().endOfGame(unoGame);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        
    }

    public void endGameLoser(String winner) {
        //je hebt het spel gewonnen
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Try again...");
        alert.setHeaderText(null);
        alert.setContentText("You lost this game!");
        alert.initModality(Modality.APPLICATION_MODAL); /* *** */
        alert.initOwner(mainApp.getPrimaryStage());
        mainApp.showLobby();
        alert.showAndWait();
    }
}
