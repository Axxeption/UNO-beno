

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.swing.text.StyledEditorKit;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    ApplicationServerController applicationServerController;
    Registry myRegistry;
    private LobbyController lobbyController;
    private gameroomController gameroomController;
    private String username;
    private int playerId;
    private long unoGameId;
    ApplicationServerGameInterface applicationServerGameInterface;
    Message stateGame;
    Integer sessionToken;
    ArrayList<Picture> cardlist;


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("UNO login");
        connect();
        initRootLayout();

        showLogin();
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the person overview inside the root layout.
     */
    public void showLogin() {
        try {

            // Load person overview.
            loginController controller = new loginController(this);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("Login.fxml"));
            loader.setController(controller);
            AnchorPane personOverview = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(personOverview);
            controller.background();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showLobby() {
        try {
            lobbyController = new LobbyController(this);
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setController(lobbyController);
            loader.setLocation(MainApp.class.getResource("Lobby.fxml"));
            AnchorPane lobbypane = (AnchorPane) loader.load();
            // Set person overview into the center of root layout.
            rootLayout.setCenter(lobbypane);
            this.primaryStage.setTitle("UNO lobby");
            //todo remooovee
            applicationServerController.checkSession(sessionToken, username);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void showGameroom(UnoGame unoGame) {
        try {
            //check als de user is een sessiontoken heeft die niet vervallen is
            if(applicationServerController.checkSession(sessionToken, username)) {
                this.primaryStage.setTitle("UNO game");

                gameroomController = new gameroomController(this);
                gameroomController.setUsername(username);
                // Load person overview.
                FXMLLoader loader = new FXMLLoader();
                loader.setController(gameroomController);
                loader.setLocation(MainApp.class.getResource("Gameroom.fxml"));
                AnchorPane gameroompane = (AnchorPane) loader.load();
                // Set person overview into the center of root layout.
                rootLayout.setCenter(gameroompane);


                try {
                    unoGameId = unoGame.getId();
                    applicationServerGameInterface = (ApplicationServerGameInterface) myRegistry.lookup("UnoGame" + unoGameId);
                    playerId = applicationServerController.joinGame(new Player(username), unoGameId);

                    Task<Boolean> task = new Task<Boolean>() {
                        @Override
                        public Boolean call() throws RemoteException {
                            // process long-running computation, data retrieval, etc...
                            stateGame = applicationServerGameInterface.startMessage(playerId);
                            return true;
                        }
                    };

                    task.setOnSucceeded(e ->
                    {                       // update UI with result
                        //verwijder die popup
                        gameroomController.setPlayerId(playerId);
                        gameroomController.setUI(stateGame);
                        startThreadGameState();
                        lobbyController.stopLoading();
                    });
                    new Thread(task).start();
                } catch (AccessException e1) {
                    e1.printStackTrace();
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
                System.out.println("komt erin!");
                lobbyController.setLoading();
            }
            else{
                //log hem uit en zeg sorry...
                applicationServerController.logout(username);
                lobbyController.sessionExpiredPopup();
                showLogin();
            }
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

//    public void showGameroom() {
//        try {
//            this.primaryStage.setTitle("UNO game");
//            gameroomController = new gameroomController(this);
//            // Load person overview.
//            FXMLLoader loader = new FXMLLoader();
//            loader.setController(gameroomController);
//            loader.setLocation(MainApp.class.getResource("Gameroom.fxml"));
//            AnchorPane gameroompane = (AnchorPane) loader.load();
//            // Set person overview into the center of root layout.
//            rootLayout.setCenter(gameroompane);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public void waitForStartMessage(UnoGame unoGame) {
        try {
            unoGameId = unoGame.getId();
            applicationServerGameInterface = (ApplicationServerGameInterface) myRegistry.lookup("UnoGame" + unoGameId);
            playerId = applicationServerController.joinGame(new Player(username), unoGameId);
            stateGame = applicationServerGameInterface.startMessage(playerId);
            gameroomController.setPlayerId(playerId);
            gameroomController.setUI(stateGame);
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (AccessException e1) {
            e1.printStackTrace();
        } catch (RemoteException e1) {
            e1.printStackTrace();
        }
    }


    public void playCard(Card card) {
        try {
            if (applicationServerGameInterface.playCard(playerId, card)) {
                System.out.println("card played");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void drawCard() {
        try {
            if (applicationServerGameInterface.drawCard(playerId)) {
                System.out.println("card drawed");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the main stage.
     *
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void connect() {
        try {
            this.myRegistry = LocateRegistry.getRegistry("localhost", 7280);
            this.applicationServerController = (ApplicationServerController) myRegistry.lookup("ClientApplicationService");
//            System.out.println("TEST " + applicationServerController);
        } catch (Exception e) {
            System.out.println("Error is: " + e);
        }
    }

    public boolean login(String username, String pass) {
        try {
            if (applicationServerController.login(username, pass)) {
                sessionToken = applicationServerController.getSessionToken(username);
                //ga naar de lobby en start de polling op
                this.username = username;
                cardlist = applicationServerController.getCards();
                extractCards();
                showLobby();
                return true;
            } else {
                return false;
            }
        } catch (RemoteException ex) {
            System.out.println(ex);
        }
        return false;
    }

    private void extractCards() {
        for(Picture p : cardlist){
            BufferedImage image = null;
            try {
                image = ImageIO.read(p.getStream());
                File outputfile = new File(p.getName());
                ImageIO.write(image, "png", outputfile);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public boolean register(String username, String pass) {
        try {
            if (applicationServerController.register(username, pass)) {
                return true;
            } else {
                return false;
            }

        } catch (RemoteException ex) {
            System.out.println("iets fout in register in mainapp:" + ex);
        }
        return false;
    }

    public ObservableList<UnoGame> getUnoGames() {
        List<UnoGame> unoGameList = null;
        try {
            System.out.println("ophalen");
            unoGameList = applicationServerController.getAllUnoGames();
            System.out.println(unoGameList);
        } catch (RemoteException e) {
            System.out.println("de error bij getunogame: " + e);
        }
        return FXCollections.observableArrayList(unoGameList);
    }

    public ObservableList<User> getBestPlayers() {
        List<User> bestPlayersList = null;
        try {
            bestPlayersList = applicationServerController.getBestPlayers();
            System.out.println("bestplayers: " + bestPlayersList.get(0).getUsername());
        } catch (RemoteException e) {
            System.out.println("de error bij getunogame: " + e);
        }
        return FXCollections.observableArrayList(bestPlayersList);
    }

    public void startGame(int i, String name) {
        UnoGame unoGame = new UnoGame(i, name);
        try {
            applicationServerController.addUnoGame(name, i);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        //nog overgaan naar het spel nu
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    //thread starten
    public void startThreadCurrentGameList() {
        new Thread(updateCurrentGamesList).start();
    }

    public void startThreadGameState() {
        new Thread(updateGame).start();
    }

    Runnable updateCurrentGamesList = new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    List<UnoGame> unoGameList = applicationServerController.subscribe();
                    System.out.println("something new happend! --> game updaten");
                    Platform.runLater(
                            () -> {
                                lobbyController.setCurrentUnoGames(FXCollections.observableList(unoGameList));
                            }
                    );
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    Runnable updateGame = new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    stateGame = applicationServerGameInterface.subscribe(playerId);
                    System.out.println("something new happend! --> game updaten");
                    //deze thread kan de thread die die ui regelt niet oproepen
                    if (stateGame.getWinner() != null) {
                        if (stateGame.getWinner() == playerId) {
                            applicationServerController.setScore(stateGame.getPoints(), username);
                            //anders zet deze thread de GUI
                            Platform.runLater(() -> {
                                gameroomController.endGameWinner();
                            });
                        } else {
                            List<String> usernames = stateGame.getNames();
                            Platform.runLater(() -> {
                                gameroomController.endGameLoser(usernames.get(stateGame.getWinner()));
                            });
                        }
                    }
                    Platform.runLater(
                            () -> {
                                gameroomController.setUI(stateGame);
                            }
                    );

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public void logout() throws RemoteException{
        applicationServerController.logout(username);
        showLogin();
    }


    public static void main(String[] args) {
        launch(args);
    }


}
