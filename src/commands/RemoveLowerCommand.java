package commands;

import database.Credentials;
import database.DatabaseController;
import models.City;
import managers.CollectionManager;
import managers.ConsoleManager;

public class RemoveLowerCommand extends AbstractCommand {
    public RemoveLowerCommand(){
        cmdName = "remove_lower";
        description = "удаляет из коллекции все элементы, меньшие, чем заданный";
        needInput = true;
    }

    @Override
    public Object getInput(ConsoleManager consoleManager){
        return consoleManager.getCity();
    }

    @Override
    public Object execute(ConsoleManager consoleManager, CollectionManager collectionManager, DatabaseController databaseController, Credentials credentials) {
        if(needInput && inputData == null) inputData = this.getInput(consoleManager);

        int initSize = collectionManager.getCityCollection().size();
        collectionManager.removeLower((City) inputData);
        int afterSize = collectionManager.getCityCollection().size();

        consoleManager.writeln("Было удалено " + (initSize - afterSize) + " элементов");

        inputData = null;

        return null;
    }
}
