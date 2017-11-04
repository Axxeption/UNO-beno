

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import sun.applet.Main;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class loginController {
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
}
