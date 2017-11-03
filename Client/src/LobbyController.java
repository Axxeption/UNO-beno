/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
                        System.out.println("blalqksjf");
                        List<UnoGame> unoGameList = applicationServerController.subscribe();
                        System.out.println("lijst: " + unoGameList);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        new Thread(updateGames).start();
    }

    public static void startGame(int i)  {
        UnoGame unoGame = new UnoGame(i);
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
