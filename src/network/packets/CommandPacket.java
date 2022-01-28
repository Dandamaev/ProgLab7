package network.packets;

import commands.AbstractCommand;
import database.Credentials;

import java.io.Serializable;

public class CommandPacket implements Serializable {

    private AbstractCommand command;
    private Credentials credentials;

    public CommandPacket(AbstractCommand command, Credentials credentials){
        this.command = command;
        this.credentials = credentials;
    }

    public AbstractCommand getCommand() {
        return command;
    }

    public Credentials getCredentials() {
        return credentials;
    }
}
