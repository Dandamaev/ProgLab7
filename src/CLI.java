import exceptions.InvalidValueException;
import exceptions.NoCommandException;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import managers.CollectionManager;
import managers.CommandsManager;
import managers.ConsoleManager;
import utils.AppConstant;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

@Slf4j
public class CLI {

    public CLI() throws IOException {
        ConsoleManager consoleManager = new ConsoleManager(new InputStreamReader(System.in), new OutputStreamWriter(System.out), false);
        consoleManager.writeln("CLI запущен версия " + AppConstant.VERSION);
        consoleManager.writeln("Используйте help для получения справки");

        log.info("app started");
        log.info("version {}", AppConstant.VERSION);

        CollectionManager collectionManager = new CollectionManager(AppConstant.FILE_PATH);

        while (true) {
            consoleManager.write("> ");
            if (consoleManager.hasNextLine()) {
                String cmd = consoleManager.read().trim();
                if(cmd.equals("")) continue;

                try {
                    CommandsManager.getInstance().execute(cmd, consoleManager, collectionManager);
                }catch (NoCommandException ex) {
                    consoleManager.writeln("Такая команда не найдена :(\nВведите команду help, чтобы вывести спискок команд");
                    log.error(ex.getMessage());
                }catch (NumberFormatException|ClassCastException ex){
                    consoleManager.writeln("Ошибка во время каста\n"+ex.getMessage());
                    log.error(ex.getMessage());
                } catch (InvalidValueException ex){
                    consoleManager.writeln(ex.getMessage());
                    log.error(ex.getMessage());
                }
            }
        }
    }

}
