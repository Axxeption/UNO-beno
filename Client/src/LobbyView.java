
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author vulst
 */
public class LobbyView implements Initializable {
    private Parent root;
    private static Stage stage;
    Stage secondStage;
    GameView g = new GameView();
    LobbyController lobbyController;
    TextField nameGameInput;

    Scene secondScene;
    /**
     * Initializes the controller class.
     */
    @FXML
    private TableView waitingGames;

    @FXML
    private TableColumn firstPlayer;

    @FXML
    private TableColumn secondPlayer;

    @FXML
    private TableColumn thirdPlayer;

    @FXML
    private TableView lastPlayedGames;

    @FXML
    private TableView bestPlayers;

    @FXML
    private AnchorPane anchor;

    public LobbyView(LobbyController lobbyController) {
        this.lobbyController = lobbyController;
    }

    public LobbyView() {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //achtergrond zetten van het spel
        BackgroundImage myBI = new BackgroundImage(new Image("background.jpg", 1200, 800, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        anchor.setBackground(new Background(myBI));
        stage.setTitle("Lobby");
        //alle kolommen goed zetten
        waitingGames = new TableView();
        waitingGames.setEditable(true);
    }

    public void start() throws IOException {
        System.out.println("root" + root);
        root = FXMLLoader.load(getClass().getResource("Lobby.fxml"));
        Scene scene = new Scene(root);
        System.out.println("stge: " + stage);
        stage.setScene(scene);
    }

    public void setStage(Stage stage, Parent root){
        //om stage en parten te hebben
        this.stage = stage;
        this.root = root;
        g.setStage(stage, root);
    }

    //deze methode moet vervangen worden door als je op een bepaald spel klikt om mee te doen, of fowel een nieuw aanmaakt...
//    public void tmpButton(ActionEvent event) {
//
//    }

    public void tmpButton(){
        //Label
        Label aantalSpelerLabel = new Label("Choose number of players.");
        //Checkboxes
        CheckBox three = new CheckBox("Three players");
        CheckBox four = new CheckBox("Four players");
        four.setSelected(true);
        //Button
        Button chosenButton = new Button("Let's go!");
        chosenButton.setOnAction(e -> checkPlayers(three, four));
        //textfield
        Label nameinputLabel = new Label("Give a name for the game");
         nameGameInput = new TextField();

        //layout
        VBox secondaryLayout = new VBox(10);
        secondaryLayout.getChildren().addAll(aantalSpelerLabel, three, four, chosenButton);
        
        //set "popup" if clicked on new game...
        secondScene = new Scene(secondaryLayout, 220, 170);
        secondStage = new Stage();
        secondStage.setTitle("Initiate game");
        secondStage.setScene(secondScene);
        secondStage.show();
        //uit game object de verschillende spelers halen en invoegen!
        //https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/table-view.htm
    }

    public void playGame(){
        try {
            root = FXMLLoader.load(getClass().getResource("GameRoom.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    //handle 3 or 4 games
    private void checkPlayers(CheckBox three, CheckBox four) {
        //make new game!
        String message = "";
        if(three.isSelected() && four.isSelected()){
            System.out.println("doe iets meje leven, stommekloot");

        }else{
            if(three.isSelected()){
                System.out.println(333);

                lobbyController.startGame(3);
                playGame();
                secondStage.close();
            }
            if(four.isSelected()){
                //initiage game with 4
                lobbyController.startGame(3);
                playGame();
                secondStage.close();

            }
        }

        if(!three.isSelected() && !four.isSelected()){
            System.out.println("doe iets meje leven, stommekloot");

        }


    }


}
