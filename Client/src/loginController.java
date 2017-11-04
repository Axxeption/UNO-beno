

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import sun.applet.Main;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.sql.DriverManager;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class loginController{
    //maak connectie met mainapp
    MainApp mainApp;

    public loginController(MainApp m){
        this.mainApp = m;
    }

    @FXML
    private Label outputLabel;

    @FXML
    private TextField input_username;

    @FXML
    private TextField input_password;

    @FXML
    private AnchorPane anchor;

    public void login(ActionEvent event) throws IOException {

        //            connect();
        if (mainApp.login(input_username.getText(), input_password.getText())) {
            outputLabel.setText("Succesfully logged in.");
        } else {
            outputLabel.setText("Wrong username or pass, try again!");
        }

    }

    public void register(ActionEvent event) {
        if (mainApp.register(input_username.getText(), input_password.getText())) {
            outputLabel.setText("Succesfully account created!");
        } else {
            outputLabel.setText("Sorry, something went wrong.");
        }

    }

    public void background() {
        //achtergrond zetten van het spel
        BackgroundImage myBI = new BackgroundImage(new Image("loginbackground.jpg", 1200, 800, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        anchor.setBackground(new Background(myBI));
    }
}
