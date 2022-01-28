package commands;

import database.Credentials;
import database.DatabaseController;
import managers.CollectionManager;
import managers.ConsoleManager;

public class InfoCommand extends AbstractCommand {

    public InfoCommand(){
        cmdName = "info";
        description = "выводит информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)";
    }

    @Override
    public Object execute(ConsoleManager consoleManager, CollectionManager collectionManager, DatabaseController databaseController, Credentials credentials) {
        consoleManager.writeln("Type; " + collectionManager.getCityCollection().getClass().getName());
        consoleManager.writeln("Count: " + collectionManager.getCityCollection().size());
        consoleManager.writeln("Init date: " + collectionManager.getInitDate().toString());

        return null;
    }
}
