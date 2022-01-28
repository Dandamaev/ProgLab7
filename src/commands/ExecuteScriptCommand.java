package commands;

import database.Credentials;
import database.DatabaseController;
import exceptions.InvalidValueException;
import managers.CollectionManager;
import managers.CommandsManager;
import managers.ConsoleManager;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ExecuteScriptCommand extends AbstractCommand {

    private boolean executeFault = false;
    public ExecuteScriptCommand(){
        cmdName = "execute_script";
        description = "выполняет команды из скрипта";
        argCount = 1;
    }


    @Override
    public Object execute(ConsoleManager consoleManager, CollectionManager collectionManager, DatabaseController databaseController, Credentials credentials) {

        executeFault = false;
        Path pathToScript = Paths.get(args[0]);
        consoleManager.writeln("Идет выполнение скрипта: " + pathToScript.getFileName());
        int lineNum = 1;
        try {
            ConsoleManager _consoleManager = new ConsoleManager(new FileReader(pathToScript.toFile()), new OutputStreamWriter(System.out), true);
            for (lineNum=1; _consoleManager.hasNextLine(); lineNum++) {
                String line = _consoleManager.read().trim();
                if(line.equals("")) continue;

                if(!executeFault) CommandsManager.getInstance().execute(line, _consoleManager, collectionManager);
            }
            //consoleManager.writeln("Скрипт выполнен.");
        } catch (FileNotFoundException e) {
            executeFault = true;
            consoleManager.writeln("Файла скрипта не найден.");
        }catch (StackOverflowError | NullPointerException ex){
            executeFault = true;
            consoleManager.writeln("Стек переполнен, выполнение прервано");
        }

        return null;
    }
}
