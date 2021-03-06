

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private ApplicationServerController applicationServerController;
    private int applicationServerPort;
    private Registry myRegistry;
    private LobbyController lobbyController;
    private gameroomController gameroomController;
    private String username;
    private int playerId;
    private long unoGameId;
    private ApplicationServerGameInterface applicationServerGameInterface;
    private Message stateGame;
    private Integer sessionToken;
    private ArrayList<Picture> cardlist;
    private UnoGame unoGame;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("UNO login");
        connect(null);
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
            if (applicationServerController.checkSession(sessionToken, username)) {
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
                    applicationServerGameInterface = unoGame.getApplicationServerGameInterface();
                    playerId = unoGame.getApplicationServerController().joinGame(new Player(username), unoGameId);
                    this.unoGame = unoGame;
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
                        gameroomController.stopLoading();
//                        lobbyController.stopLoading();
                    });
                    new Thread(task).start();
                } catch (AccessException e1) {
                    e1.printStackTrace();
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
                System.out.println("komt erin!");
                gameroomController.setLoading();
//                lobbyController.setLoading();
            } else {
                //log hem uit en zeg sorry...
                applicationServerController.logout(username);
                lobbyController.sessionExpiredPopup();
                showLogin();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            crash(applicationServerPort);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void playCard(Card card) {
        try {
            if (applicationServerGameInterface.playCard(playerId, card)) {
                System.out.println("card played");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            crash(applicationServerPort);
        }
    }

    public void drawCard() {
        try {
            if (applicationServerGameInterface.drawCard(playerId)) {
                System.out.println("card drawed");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            crash(applicationServerPort);
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

    public void connect(Integer failedServerPort) {
        int count = 0;
        int maxTries = 3;
        while (count < maxTries) {
            try {
                Registry dispatcherRegistry = LocateRegistry.getRegistry("localhost", 9450);
                DispatcherInterface dispatcherInterface = (DispatcherInterface) dispatcherRegistry.lookup("Dispatcher");
                System.out.println(failedServerPort);
                applicationServerPort = dispatcherInterface.whichApplicationServerToConnect(failedServerPort);
                this.myRegistry = LocateRegistry.getRegistry("localhost", applicationServerPort);
                this.applicationServerController = (ApplicationServerController) myRegistry.lookup("ApplicationServer");
                System.out.println("Connected to new server on port: " + applicationServerPort);
                return;
//            System.out.println("TEST " + applicationServerController);

            } catch (AccessException e) {
                e.printStackTrace();
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
            }
            count++;
        }
        System.out.println("FATAL");
        Runtime.getRuntime().exit(0);
    }

    private void crash(Integer failedServerPort){
        connect(failedServerPort);
        loginWithError();
    }

    private void loginWithError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("A server crashed.");
        alert.setContentText("Please log in.");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(primaryStage);
        alert.show();
        showLogin();
    }

    public boolean login(String username, String pass) {
        try {
            if (applicationServerController.login(username, pass)) {
                sessionToken = applicationServerController.getSessionToken(username);
                //ga naar de lobby en start de polling op
                this.username = username;
                //bij het inloggen de juiste kaarten downloaden
                cardlist = applicationServerController.getCards();
                extractCards();
                showLobby();
                return true;
            } else {
                return false;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            crash(applicationServerPort);
        }
        return false;
    }

    private void extractCards() {
        //in de lijst zitten alle kaarten die moeten wel nog in de db gestopt worden
        //de mogelijkheid om speciale kaarten toe te voegen rond de kerstdagen zit in de applicationserver
        //daar moet je een string meegeven voor welke periode je de kaarten wilt
        //de datum moet dan ook daar gechecked worden
        String fileSeparator = System.getProperty("file.separator");
        String path = System.getProperty("user.dir");
        path = path + fileSeparator + "Client" + fileSeparator + "src" + fileSeparator + "Images" + fileSeparator;
        System.out.println(path);

        for (Picture p : cardlist) {
            BufferedImage image = null;
            try {
                image = javax.imageio.ImageIO.read(new ByteArrayInputStream(p.getStream()));
                File outputfile = new File(path + p.getName());
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
            ex.printStackTrace();
            crash(applicationServerPort);
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
            crash(applicationServerPort);
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
                    Platform.runLater(
                            () -> {
                                loginWithError();
                            }
                    );
                    connect(applicationServerPort);
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
                    //deze thread kan de thread die ui regelt niet oproepen
                    if (stateGame.getWinner() != null) {
                        if (stateGame.getWinner() == playerId) {
                            applicationServerController.setScore(stateGame.getPoints(), username);
                            //anders zet deze thread de GUI
                            Platform.runLater(() -> {
                                gameroomController.endGameWinner(unoGame);
                            });
                        } else {
                            List<String> usernames = stateGame.getNames();
                            Platform.runLater(() -> {
                                System.out.println("loser.");
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
                    crash(applicationServerPort);
                }
            }
        }
    };

    public void logout(){
        try {
            applicationServerController.logout(username);
        } catch (RemoteException e) {
            e.printStackTrace();
            crash(applicationServerPort);
        }
        showLogin();
    }


    public static void main(String[] args) {
        launch(args);
    }


}
