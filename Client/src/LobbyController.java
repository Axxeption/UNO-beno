import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.SplittableRandom;

public class LobbyController implements Initializable {
    MainApp mainapp;
    TextField nameGameInput;
    Scene secondScene;
    Stage secondStage;
    UnoGame unoGame;

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
        BackgroundImage myBI = new BackgroundImage(new Image("Images/background.jpg", 1200, 800, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        anchor.setBackground(new Background(myBI));

        //alles voor de huidige games te zetten
        TableColumn<UnoGame, String> nameColumn = new TableColumn<>("Game name");
        nameColumn.setMinWidth(150);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("gameName"));

        TableColumn<UnoGame, String> currentUsersColumn = new TableColumn<>("Current users");
        currentUsersColumn.setMinWidth(125);
        currentUsersColumn.setCellValueFactory(new PropertyValueFactory<>("currentNumberOfPlayers"));

        TableColumn<UnoGame, String> maxUsersColumn = new TableColumn<>("Needed users");
        maxUsersColumn.setMinWidth(124);
        maxUsersColumn.setCellValueFactory(new PropertyValueFactory<>("maxNumberOfPlayers"));

        loadGames();
        waitingGames.getColumns().addAll(nameColumn, currentUsersColumn, maxUsersColumn);
        mainapp.startThreadCurrentGameList();

        //alles voor de beste spelers weer te geven
        TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setMinWidth(199);
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<User, Integer> scoreColumn = new TableColumn<>("Score");
        scoreColumn.setMinWidth(199);
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));

        loadScore();
        bestPlayers.getColumns().addAll(usernameColumn, scoreColumn);
    }

    private void loadScore() {
        bestPlayers.setItems(mainapp.getBestPlayers());

    }

    public void loadGames() {
        waitingGames.setItems(mainapp.getUnoGames());
    }

    //handle 3 or 4 games
    private void checkPlayers(CheckBox three, CheckBox four, CheckBox two) {
        //maak nieuw spel afhankelijk met hoeveel
        if (two.isSelected()) {

            mainapp.startGame(2, nameGameInput.getText());
            secondStage.close();
        }
        if (three.isSelected()) {
            mainapp.startGame(3, nameGameInput.getText());
            secondStage.close();
        }
        if (four.isSelected()) {
            mainapp.startGame(4, nameGameInput.getText());
            secondStage.close();
        }
    }

    public void addgame() {
        //Label
        Label aantalSpelerLabel = new Label("Choose number of players.");
        //Checkboxes
        final CheckBox three = new CheckBox("Three players");
        final CheckBox four = new CheckBox("Four players");
        final CheckBox two = new CheckBox("two players");
        two.setSelected(true);
        //Button
        Button chosenButton = new Button("Let's go!");
        chosenButton.setOnAction(e -> checkPlayers(three, four, two));
        //textfield
        Label nameinputLabel = new Label("Give a name for the game");
        nameGameInput = new TextField();

        //layout
        VBox secondaryLayout = new VBox(10);
        secondaryLayout.getChildren().addAll(aantalSpelerLabel, two, three, four, nameinputLabel, nameGameInput, chosenButton);

        //set "popup" if clicked on new game...
        secondScene = new Scene(secondaryLayout, 300, 250);
        secondStage = new Stage();
        secondStage.setTitle("Initiate game");
        secondStage.setScene(secondScene);
        secondStage.show();

        two.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                three.setSelected(false);
                four.setSelected(false);
            }
        });

        three.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                two.setSelected(false);
                four.setSelected(false);
            }
        });

        four.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                three.setSelected(false);
                two.setSelected(false);
            }
        });
    }

    public void startbutton() {
        if (waitingGames.getSelectionModel().getSelectedItem() != null) {
            UnoGame unoGame = (UnoGame) waitingGames.getSelectionModel().getSelectedItem();
            //go into the gameroom with this selectedgame
            System.out.println(unoGame);
            mainapp.showGameroom(unoGame);
        }
    }

//    public void startbutton() {
//
//        if (waitingGames.getSelectionModel().getSelectedItem() != null) {
//            unoGame = (UnoGame) waitingGames.getSelectionModel().getSelectedItem();
//            mainapp.waitForStartMessage(unoGame);
//            secondStage.close();
//            mainapp.showGameroom();
////            new Thread(waitForStartingGame).start();
////            ImageView loading = new ImageView(new Image("loading.gif"));
//            loading.setFitWidth(200);
//            loading.setFitHeight(200);
//            VBox secondaryLayout = new VBox(10);
//            Label wait = new Label("Friends need to join...");
//            secondaryLayout.getChildren().addAll(loading, wait);
//            //set "popup" if clicked on new game...
//            secondScene = new Scene(secondaryLayout, 200, 230);
//            secondStage = new Stage();
//            secondStage.setTitle("Waiting for other players");
//            secondStage.setScene(secondScene);
//            secondStage.show();

//            //go into the gameroom with this selectedgame
//        }
//    }

//    Runnable waitForStartingGame = new Runnable() {
//        @Override
//        public void run() {
//            unoGame = (UnoGame) waitingGames.getSelectionModel().getSelectedItem();
//            mainapp.waitForStartMessage(unoGame);
//            Platform.runLater(() -> {
//                secondStage.close();
//                mainapp.showGameroom();
//            });
//
//        }
//
//    };


    public void setCurrentUnoGames(ObservableList<UnoGame> currentUnoGames) {
        waitingGames.setItems(currentUnoGames);
    }

}
