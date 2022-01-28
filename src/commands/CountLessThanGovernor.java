package commands;

import database.Credentials;
import database.DatabaseController;
import models.Human;
import managers.CollectionManager;
import managers.ConsoleManager;

public class CountLessThanGovernor extends AbstractCommand {
    public CountLessThanGovernor(){
        cmdName = "count_less_than_governor";
        description = "выводит количество элементов, значение поля governor которых меньше заданного";
        needInput = true;
    }

    @Override
    public Object getInput(ConsoleManager consoleManager){
        return consoleManager.getHuman();
    }

    @Override
    public Object execute(ConsoleManager consoleManager, CollectionManager collectionManager, DatabaseController databaseController, Credentials credentials) {
        if(needInput && inputData == null) inputData = this.getInput(consoleManager);
        long count = collectionManager.countLessThanGovernor((Human) inputData);
        consoleManager.writeln("Count elements less than governor: " + count);

        inputData = null;

        return null;
    }
}
