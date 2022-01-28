import java.io.IOException;
import java.util.regex.Pattern;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import network.Client;
import network.Server;

/**
 * Главный класс приложения
 */


public class Main {


    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        String mode = System.getProperty("mode", "server").toLowerCase();
        switch (mode){
            case "cli":
                new CLI();
                break;

            case "client":
                new Client(args).run();
                break;

            case "server":
                new Server(args).run();
                break;
        }

    }

}
