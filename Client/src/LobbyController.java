import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LobbyController implements Initializable{
    MainApp mainapp;
    TextField nameGameInput;
    Scene secondScene;
    Stage secondStage;

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

    public LobbyController(MainApp mainApp) {
        this.mainapp = mainApp;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //achtergrond zetten van het spel
        BackgroundImage myBI = new BackgroundImage(new Image("background.jpg", 1200, 800, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        anchor.setBackground(new Background(myBI));
        TableColumn<UnoGame, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(250);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("gameName"));

        TableColumn<UnoGame, String> currentUsersColumn = new TableColumn<>("current users");
        currentUsersColumn.setMinWidth(175);
        currentUsersColumn.setCellValueFactory(new PropertyValueFactory<>("currentNumberOfPlayers"));

        TableColumn<UnoGame, String> maxUsersColumn = new TableColumn<>("Max users");
        maxUsersColumn.setMinWidth(175);
        maxUsersColumn.setCellValueFactory(new PropertyValueFactory<>("maxNumberOfPlayers"));

        loadGames();
        waitingGames.getColumns().addAll(nameColumn, currentUsersColumn, maxUsersColumn);
        mainapp.startThreadCurrentGameList();
    }

//

    public void loadGames() {
        waitingGames.setItems(mainapp.getUnoGames());
    }

    //handle 3 or 4 games
    private void checkPlayers(CheckBox three, CheckBox four) {
        //make new game!
        if (three.isSelected() && four.isSelected()) {
            System.out.println("doe iets meje leven, stommekloot");

        } else {
            if (three.isSelected()) {
                System.out.println(nameGameInput.getText());
                mainapp.startGame(3, nameGameInput.getText());

                secondStage.close();
            }
            if (four.isSelected()) {
                //initiage game with 4
                mainapp.startGame(4, nameGameInput.getText());
//                playGame();
                secondStage.close();
            }
        }

        if (!three.isSelected() && !four.isSelected()) {
            System.out.println("doe iets meje leven, stommekloot");

        }
    }
    public void addgame() {
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

    public void startbutton(){
        UnoGame unoGame = (UnoGame) waitingGames.getSelectionModel().getSelectedItem();
        //go into the gameroom with this selectedgame
        System.out.println(unoGame);
        mainapp.showGameroom(unoGame);
    }


    public void setCurrentUnoGames(ObservableList<UnoGame> currentUnoGames) {
        waitingGames.setItems(currentUnoGames);    }

//    public void tmpbutton() {
//        System.out.println("gedrukt!");
//        TableColumn<UnoGame, String> nameColumn = new TableColumn<>("Name");
//        nameColumn.setMinWidth(250);
//        nameColumn.setCellValueFactory(new PropertyValueFactory<>("gameName"));
//
//        TableColumn<UnoGame, String> currentUsersColumn = new TableColumn<>("current users");
//        currentUsersColumn.setMinWidth(175);
//        currentUsersColumn.setCellValueFactory(new PropertyValueFactory<>("currentNumberOfPlayers"));
//
//        TableColumn<UnoGame, String> maxUsersColumn = new TableColumn<>("Max users");
//        maxUsersColumn.setMinWidth(175);
//        maxUsersColumn.setCellValueFactory(new PropertyValueFactory<>("maxNumberOfPlayers"));
//
//        loadGames();
//        waitingGames.getColumns().addAll(nameColumn, currentUsersColumn, maxUsersColumn);
//    }
}
