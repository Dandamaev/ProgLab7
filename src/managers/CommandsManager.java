package managers;

import commands.*;
import exceptions.InvalidValueException;
import exceptions.NoCommandException;
import java.util.*;

/**
 * Менеджер команд
 */
public class CommandsManager {
    private static CommandsManager instance;

    public static CommandsManager getInstance() {
        if (instance == null) {
            instance = new CommandsManager();
        }
        return instance;
    }

    private Map<String, AbstractCommand> commands = new HashMap<>();

    public CommandsManager(){
        addCommand(new LoginCommand());
        addCommand(new RegisterCommand());
        addCommand(new AddCommand());
        addCommand(new HelpCommand());
        addCommand(new ExitCommand());
        addCommand(new ShowCommand());
        addCommand(new InfoCommand());
        addCommand(new UpdateIdCommand());
        addCommand(new RemoveIdCommand());
        addCommand(new ClearCommad());
        //addCommand(new ShuffleCommand());
        //addCommand(new AddIfMinCommand());
        addCommand(new CountLessThanGovernor());
        addCommand(new FilterContainsNameCommand());
        //addCommand(new RemoveLowerCommand());
        addCommand(new PrintFieldAscendingTimezoneCommand());
        //addCommand(new SaveCommand());
        addCommand(new ExecuteScriptCommand());
    }

    private void addCommand(AbstractCommand cmd){
        commands.put(cmd.getCmdName(), cmd);
    }

    public AbstractCommand getCommand(String s) throws NoCommandException {
        if (!commands.containsKey(s)) {
            throw new NoCommandException("Команда не найдена");
        }
        return commands.get(s);
    }

    public AbstractCommand parseCommand(String str){
        AbstractCommand cmd = null;
        String[] parse = str.trim().split("\\s+");
        cmd = getCommand(parse[0].toLowerCase());
        String[] args = Arrays.copyOfRange(parse, 1, parse.length);
        if(cmd.getArgCount() == args.length)
            cmd.setArgs(args);
        else throw new InvalidValueException("Введено " + args.length + " аргументов, ожидалось " + cmd.getArgCount());

        return cmd;
    }

    /**
     * Выполняет команды введенные пользователем
     * @param str
     * @param consoleManager
     * @param collectionManager
     */
    public void execute(String str, ConsoleManager consoleManager, CollectionManager collectionManager){
        //parseCommand(str).execute(consoleManager, collectionManager);
    }


    public List<AbstractCommand> getAllCommands() {
        return new ArrayList<>(commands.values());
    }
}
