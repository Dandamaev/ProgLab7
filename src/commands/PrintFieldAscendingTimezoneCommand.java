package commands;

import database.Credentials;
import database.DatabaseController;
import managers.CollectionManager;
import managers.ConsoleManager;

public class PrintFieldAscendingTimezoneCommand extends AbstractCommand {

    public PrintFieldAscendingTimezoneCommand(){
        cmdName = "print_field_ascending_timezone";
        description = "выводит значения поля timezone всех элементов в порядке возрастания";
    }

    @Override
    public Object execute(ConsoleManager consoleManager, CollectionManager collectionManager, DatabaseController databaseController, Credentials credentials) {
        collectionManager.sortByTimezone().forEach(x->consoleManager.writeln(x.toString()));

        return null;
    }
}
