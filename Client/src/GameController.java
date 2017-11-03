/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author vulst
 */
public class GameController {
    private GameView gameview;

    public void updateMiddleCard(){
        //Deze methode moet aangesproken worden als je de middelstekaart aanpast
        gameview.updateMiddleCard(4);
    }

    public void updateOtherPlayers(){
        gameview.updateOtherPlayer();
    }
}
