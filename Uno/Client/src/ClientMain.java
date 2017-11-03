/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author vulst
 */
public class ClientMain extends Application {
    UNO_UIController u = new UNO_UIController();

    @Override
    public void start(Stage stage) throws Exception {
        u.start(stage);
    }

    public static void main(String[] args) {
        launch(args); //start UI
        //om te testen

    }

}