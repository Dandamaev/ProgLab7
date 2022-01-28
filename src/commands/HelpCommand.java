package commands;

import database.Credentials;
import database.DatabaseController;
import managers.CollectionManager;
import managers.CommandsManager;
import managers.ConsoleManager;

import java.util.List;

public class HelpCommand extends AbstractCommand {

    public HelpCommand(){
        cmdName = "help";
        description = "выводит список команд и их описание";
    }

    @Override
    public Object execute(ConsoleManager consoleManager, CollectionManager collectionManager, DatabaseController databaseController, Credentials credentials) {

        List<AbstractCommand> aCommands = CommandsManager.getInstance().getAllCommands();
        for (AbstractCommand cmd: aCommands){
            consoleManager.writeln(cmd.getCmdName() + " - " + cmd.getDescription() );
        }

        return null;
    }
}
