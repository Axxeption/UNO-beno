/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author vulst
 */
public class GameView implements Initializable {
    private Parent root;
    private static Stage stage;
    GameController gameController;
    @FXML
    private HBox backgroundBox;

    @FXML
    private Button button;

    @FXML
    private AnchorPane anchor;

    @FXML
    ImageView middleCard;
    
    @FXML
    private HBox otherPlayerBox;

    public GameView() {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //achtergrond zetten van het spel
        BackgroundImage myBI = new BackgroundImage(new Image("background.jpg", 1200, 800, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        anchor.setBackground(new Background(myBI));

        //doe iets als jeop de image klikt!
        middleCard.setOnMouseClicked((MouseEvent e) -> {
            System.out.println("Clicked!"); // change functionality
        });
        stage.setTitle("UNO game");
    }

    public GameView(GameController g){
        this.gameController = g;
    }

    public void setStage(Stage stage, Parent root) {
        this.stage = stage;
        this.root = root;
    }

    public void tmpButton(ActionEvent event) throws IOException {
        System.out.println("clicked on button");
        root = FXMLLoader.load(getClass().getResource("Lobby.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
    
    public void updateMiddleCard(Integer id){
        //if integer == ies dan afhankelijk de jusite kaart nemen
        Image image = new Image("/Cards/EIGHT_BLUE.png");
        middleCard.setImage(image);
    }
    
    @FXML
    public void updateOtherPlayer(){
        //hier krijg je update van de andere kaarten
          for (int i = 0; i < 2; i++) {
            backgroundBox.getChildren().add(new Label("test"));
        }
    }

    public void start()  {
        try {
            root = FXMLLoader.load(getClass().getResource("GameRoom.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

}
