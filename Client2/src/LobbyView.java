
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.sun.xml.internal.bind.v2.runtime.property.PropertyFactory;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * FXML controller class
 *
 * @author vulst
 */
public class LobbyView implements Initializable {
    private Parent root;
    private static Stage stage;
    Stage secondStage;
    GameView g = new GameView();
    static LobbyController lobbyController;
    TextField nameGameInput;
    Scene secondScene;
    /**
     * Initializes the controller class.
     */
    @FXML
    private TableView waitingGames;

    @FXML
    private TableView lastPlayedGames;

    @FXML
    private TableView bestPlayers;

    @FXML
    private AnchorPane anchor;

    @FXML
    private Button newGame;

    public LobbyView(LobbyController lobbyController) {
        this.lobbyController = lobbyController;
    }

    public LobbyView() {
    };

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //achtergrond zetten van het spel
        BackgroundImage myBI = new BackgroundImage(new Image("background.jpg", 1200, 800, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        anchor.setBackground(new Background(myBI));
        stage.setTitle("Lobby");
        //alle kolommen goed zetten

        TableColumn<UnoGame, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(250);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("gameName"));

        TableColumn<UnoGame, String> currentUsersColumn = new TableColumn<>("current users");
        nameColumn.setMinWidth(175);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("currentNumberOfPlayers"));

        TableColumn<UnoGame, String> maxUsersColumn = new TableColumn<>("Max users");
        nameColumn.setMinWidth(175);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("maxNumberOfPlayers"));

        loadGames();
        waitingGames.getColumns().addAll(nameColumn, currentUsersColumn, maxUsersColumn);

    }

    public void start() throws IOException {
        root = FXMLLoader.load(getClass().getResource("Lobby.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    public void setStage(Stage stage, Parent root) {
        //om stage en parten te hebben
        this.stage = stage;
        this.root = root;
        g.setStage(stage, root);
    }

    public void tmpButton() {
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
        secondaryLayout.getChildren().addAll(aantalSpelerLabel, three, four, nameinputLabel, nameGameInput, chosenButton);

        //set "popup" if clicked on new game...
        secondScene = new Scene(secondaryLayout, 300, 230);
        secondStage = new Stage();
        secondStage.setTitle("Initiate game");
        secondStage.setScene(secondScene);
        secondStage.show();
        //uit game object de verschillende spelers halen en invoegen!
        //https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/table-view.htm
    }

    public void playGame() {
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
        if (three.isSelected() && four.isSelected()) {
            System.out.println("doe iets meje leven, stommekloot");

        } else {
            if (three.isSelected()) {
                lobbyController.startGame(3, nameGameInput.getText());

                secondStage.close();
            }
            if (four.isSelected()) {
                //initiage game with 4
                lobbyController.startGame(4, nameGameInput.getText());
//                playGame();
                secondStage.close();
            }
        }

        if (!three.isSelected() && !four.isSelected()) {
            System.out.println("doe iets meje leven, stommekloot");

        }
    }

    public void setCurrentUnoGames(ObservableList<UnoGame> unogamescurrent) {
        System.out.println("opgeroepen: " + unogamescurrent);
        System.out.println("wiatinggames: " + this.waitingGames);
//        this.waitingGames.setItems(unogamescurrent);
    }

    public void loadGames() {
        System.out.println("waitinggamestable: " + waitingGames);
        System.out.println(lobbyController.getUnoGames());
        waitingGames.setItems(lobbyController.getUnoGames());

    }
}
