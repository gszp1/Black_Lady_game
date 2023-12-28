package messages;

import java.io.IOException;
import java.io.Serializable;

public abstract class Message implements Serializable {

    private final MessageType messageType;

    private final String data;

    private final int clientID;

    public Message(MessageType messageType, String data, int clientID) {
        this.messageType = messageType;
        this.clientID = clientID;
        this.data = data;
    }

    public abstract boolean handleMessage() throws IOException;

    public MessageType getMessageType() {
        return messageType;
    }

    public String getData() {
        return data;
    }

    public int getClientID() {
        return clientID;
    }
}
