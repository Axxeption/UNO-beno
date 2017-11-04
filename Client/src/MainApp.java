

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showGameroom(UnoGame unoGame) {
        try {
            gameroomController = new gameroomController(this);
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setController(gameroomController);
            loader.setLocation(MainApp.class.getResource("Gameroom.fxml"));
            AnchorPane gameroompane = (AnchorPane) loader.load();
            // Set person overview into the center of root layout.
            rootLayout.setCenter(gameroompane);
            playerId = applicationServerController.joinGame(new Player("axel"), unoGame.getId());
            unoGameId = unoGame.getId();
            try {
                applicationServerGameInterface = (ApplicationServerGameInterface) myRegistry.lookup("UnoGame" + unoGameId);
                System.out.println(applicationServerGameInterface.startMessage(playerId));
            } catch (NotBoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
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
                //ga naar de lobby en start de polling op
                this.username = username;
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

    public void startGame(int i, String name) {
        UnoGame unoGame = new UnoGame(i, name);
        try {
            applicationServerController.addUnoGame(name,i);
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


    public void startCheckingGamesThread() {
        new Thread(updateGames).start();
    }

    Runnable updateGames = new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    List<UnoGame> unoGameList = applicationServerController.subscribe();
                    System.out.println("something new happend! --> game updaten");
                    lobbyController.setCurrentUnoGames(FXCollections.observableList(unoGameList));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    public static void main(String[] args) {
        launch(args);
    }


}