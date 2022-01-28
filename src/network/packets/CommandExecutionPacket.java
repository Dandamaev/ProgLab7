package network.packets;

import java.io.Serializable;

public class CommandExecutionPacket implements Serializable {

    private final Object message;
    public CommandExecutionPacket(Object message){
        this.message = message;
    }

    public Object getMessage() {
        return message;
    }
}
