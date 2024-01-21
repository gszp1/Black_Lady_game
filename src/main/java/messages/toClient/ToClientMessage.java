package messages.toClient;

import messages.MessageType;
import server.DatabaseConnector;
import utils.UserList;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;

public abstract class ToClientMessage implements Serializable {

    protected final MessageType messageType;

    protected final String data;

    public ToClientMessage(MessageType messageType, String data) {
        this.messageType = messageType;
        this.data = data;
    }

    public abstract boolean handle() throws IOException;

    public MessageType getMessageType() {
        return messageType;
    }

    public String getData() {
        return data;
    }
}
