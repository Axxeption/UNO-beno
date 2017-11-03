/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author vulst
 */
public class UNO_UIController implements Initializable {

    static ApplicationServerController applicationServerController;
    Registry myRegistry;
    private Scene scene;
    private Parent root;
    private static Stage window;
    static LobbyController lobbyController;


    @FXML
    private Label label;

    @FXML
    private Label outputLabel;

    @FXML
    private TextField input_username;

    @FXML
    private TextField input_password;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //deze nethode wordt hier niet gebruikt
    }

    public void start(Stage stage) throws IOException {
        //altijd eerst naar de login page
        connect();
        root = FXMLLoader.load(getClass().getResource("UNO_UI.fxml"));
        scene = new Scene(root);
        this.window = stage;
        this.window.setScene(scene);
        this.window.show();
        stage.setTitle("Login");
        lobbyController = new LobbyController(applicationServerController);
        lobbyController.setStage(stage, root);
    }

    public void login(ActionEvent event) throws IOException {
        try {
            System.out.println(applicationServerController);
//            connect();
            if (applicationServerController.login(input_username.getText(), input_password.getText())) {
                outputLabel.setText("Succesfully logged in.");
                lobbyController.startLobby();
//                root = FXMLLoader.load(getClass().getResource("Lobby.fxml"));
//                scene = new Scene(root);
//                window.setScene(scene);

            } else {
                outputLabel.setText("Wrong username or pass, try again!");
            }
        } catch (RemoteException ex) {
            System.out.println(ex);
        }

    }

    public void register(ActionEvent event) {
        try {
//            connect();
            if (applicationServerController.register(input_username.getText(), input_password.getText())) {
                outputLabel.setText("Succesfully account created!");
            } else {
                outputLabel.setText("Sorry, something went wrong.");
            }

        } catch (RemoteException ex) {
            Logger.getLogger(UNO_UIController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void connect() {
        try {
            this.myRegistry = LocateRegistry.getRegistry("localhost", 9450);
            this.applicationServerController = (ApplicationServerController) myRegistry.lookup("ClientApplicationService");
//            System.out.println("TEST " + applicationServerController);
        } catch (Exception e) {
            System.out.println("Error is: " + e);
        }
    }

}
