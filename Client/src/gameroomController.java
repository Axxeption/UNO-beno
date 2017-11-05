import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.net.URL;
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

    @FXML
    private AnchorPane anchor;

    @FXML
    private HBox backgroundBox;

    @FXML
    AnchorPane otherPlayersPane;

    @FXML
    private ImageView middleCard;

    @FXML
    private Label uwbeurt;

    @FXML
    private ImageView cardBack;

    public gameroomController(MainApp mainApp) {
        this.mainApp = mainApp;
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
        backgroundBox.setSpacing(-50);
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
                otherPlayerBoxDummy.setSpacing(-50);
                otherPlayerBox = otherPlayerBoxDummy;
            }
            else {
                VBox otherPlayerBoxDummmy = (VBox) otherPlayersPane.getChildren().get(k);
                otherPlayerBoxDummmy.setSpacing(-90);
                otherPlayerBox = otherPlayerBoxDummmy;
            }
            System.out.println(otherPlayerBox + "we hebben een gevonden" + otherPlayerCards);
            for (int c = 0; c < otherPlayerCards; c++){
                ImageView tmp  = new ImageView(pathToCards + "uno_back.png");
                tmp.setId(Integer.toString(i));
                tmp.setFitHeight(194);
                tmp.setFitWidth(140);
                otherPlayerBox.getChildren().add(tmp);
            }
            k++;
        }

    }

    public void endGameWinner() {
        //je hebt het spel gewonnen
        Label won = new Label("Congratulations, you won this game!");
        won.setFont(Font.font("Verdana", 20));
        won.setTextFill(Color.FORESTGREEN);

        //Button
        Button okButton = new Button("Let's play another game!");
        okButton.setOnAction(e -> {
            mainApp.showLobby();
            secondStage.close();
        });

        //layout
        VBox secondaryLayout = new VBox(10);
        secondaryLayout.getChildren().addAll(won, okButton);

        secondScene = new Scene(secondaryLayout, 300, 150);
        secondStage = new Stage();
        secondStage.setTitle("You won!");
        secondStage.setScene(secondScene);
        secondStage.show();
        
    }

    public void endGameLoser(String winner) {
        //je hebt het spel verloren

        Label lost = new Label(winner + "Just won the game!");
        lost.setFont(Font.font("Verdana", 20));
        lost.setTextFill(Color.DARKRED);

        //Button
        Button okButton = new Button("Let's try again!!");
        okButton.setOnAction(e -> {
            mainApp.showLobby();
            secondStage.close();
        });

        //layout
        VBox secondaryLayout = new VBox(10);
        secondaryLayout.getChildren().addAll(lost, okButton);

        secondScene = new Scene(secondaryLayout, 300, 150);
        secondStage = new Stage();
        secondStage.setTitle("You lose!");
        secondStage.setScene(secondScene);
        secondStage.show();

    }
}
