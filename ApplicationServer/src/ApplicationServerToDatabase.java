/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vulst
 */
public class ApplicationServerToDatabase {

    private void connectDbServer() {
        Registry myRegistry;
        try {
            myRegistry = LocateRegistry.getRegistry("localhost", 9430);
            SQLiteController impl = (SQLiteController) myRegistry.lookup("DatabaseService");
        } catch (NotBoundException ex) {
            Logger.getLogger(ApplicationServerMain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AccessException ex) {
            Logger.getLogger(ApplicationServerMain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(ApplicationServerMain.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    

}
