package commands;

import database.Credentials;
import database.DatabaseController;
import managers.CollectionManager;
import managers.ConsoleManager;

public class ShuffleCommand extends AbstractCommand {
    public ShuffleCommand(){
        cmdName = "shuffle";
        description = "перемешивает элементы коллекции в случайном порядке";
    }

    @Override
    public Object execute(ConsoleManager consoleManager, CollectionManager collectionManager, DatabaseController databaseController, Credentials credentials) {
        collectionManager.shuffle();
        consoleManager.writeln("Коллекция была перемешана в случайном порядке");

        return null;
    }
}
