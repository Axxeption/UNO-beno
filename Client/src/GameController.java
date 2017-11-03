/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javafx.collections.FXCollections;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vulst
 */
public class GameController {

    static ApplicationServerController applicationServerController;
    private GameView gameview;

    public GameController(ApplicationServerController applicationServerController) {
        this.applicationServerController = applicationServerController;
        Runnable updateGames = new Runnable() {
            @Override
            public void run() {
                while (true) {
                   //vraag nieuwe dingen op
                }
            }
        };
        new Thread(updateGames).start();
        System.out.println("Thread started");

    }

    public void updateMiddleCard(){
        //Deze methode moet aangesproken worden als je de middelstekaart aanpast
        gameview.updateMiddleCard(4);
    }

    public void setStage(Stage stage, Parent root){
        System.out.println("gameview setten");
        this.gameview = new GameView(this);
        gameview.setStage(stage,root);
    }

    public void startGame(){
        System.out.println("gameview: " + gameview);
        gameview.start();
        gameview.setController(this);
    }

    public void updateYourCards() {
        List<NumberCard> list = new ArrayList<>();
        list.add(new NumberCard(3, 5));
        list.add(new NumberCard(2, 5));
        list.add(new NumberCard(1, 5));
        list.add(new NumberCard(4, 5));
        list.add(new NumberCard(3, 2));
        gameview.showCards(list);
        //nu doorgeven naar ui


    }

    public void updateOtherPlayers(){
        gameview.updateOtherPlayer();
    }
}
