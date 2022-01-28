package commands;

import database.Credentials;
import database.DatabaseController;
import managers.CollectionManager;
import managers.ConsoleManager;

public class SaveCommand extends AbstractCommand {

    public SaveCommand(){
        cmdName = "save";
        description = "сохраняет коллекцию в файл";
    }

    @Override
    public Object execute(ConsoleManager consoleManager, CollectionManager collectionManager, DatabaseController databaseController, Credentials credentials) {
        collectionManager.save();
        consoleManager.writeln("Коллекция была сохранена.");

        return null;
    }
}
