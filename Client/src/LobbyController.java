/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author vulst
 */
public class LobbyController {

    static ApplicationServerController applicationServerController;
    LobbyView lobbyview;

    public LobbyController(ApplicationServerController applicationServerController) {
        this.applicationServerController = applicationServerController;
        Runnable updateGames = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        List<UnoGame> unoGameList = applicationServerController.subscribe();
                        System.out.println("something new happend! --> game updaten");
                        lobbyview.setCurrentUnoGames(FXCollections.observableList(unoGameList));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        new Thread(updateGames).start();
        System.out.println("Thread started");
    }

    public ObservableList<UnoGame> getUnoGames(){
        List<UnoGame> unoGameList = null;
        try {
            System.out.println("ophalen");
            unoGameList = applicationServerController.getAllUnoGames();
        } catch (RemoteException e) {
            System.out.println("de error bij getunogame: " + e );
        }
        return FXCollections.observableList(unoGameList);
    }

    public static void startGame(int i, String name)  {
        UnoGame unoGame = new UnoGame(i, name);
        try {
            applicationServerController.addUnoGame(unoGame);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void setStage(Stage stage, Parent root){
        lobbyview = new LobbyView(this);
        lobbyview.setStage(stage,root);
    }

    public void startLobby(){
        try {
            System.out.println("lobbyview" + lobbyview);
            lobbyview.start();
//            lobbyview.loadGames();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
