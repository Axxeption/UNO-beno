import java.io.File;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import static java.lang.Thread.sleep;

/**
 * Created by Benoit on 15/11/17.
 */
public class DispatcherMain {

    private static DispatcherImpl dispatcher;

    private static void startServer() {
        try {
            // create on port 1099
            Registry registry = LocateRegistry.createRegistry(9450);
            // create a new service named CounterService
            dispatcher = new DispatcherImpl();
            registry.rebind("Dispatcher", dispatcher);
            dispatcher.initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("DISPATCHER IS READY");
    }

    public static void main(String[] args){

        startServer();


    }


}
