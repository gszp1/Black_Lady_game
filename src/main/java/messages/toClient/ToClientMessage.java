package messages.toClient;

import messages.MessageType;

import java.io.IOException;
import java.io.Serializable;

/**
 * Abstract class for messages sent to users.
 */
public abstract class ToClientMessage implements Serializable {

    /**
     * Type of message.
     */
    protected final MessageType messageType;

    /**
     * Message's contents.
     */
    protected final String data;

    /**
     * Constructor, sets message's type and data.
     * @param messageType
     * @param data
     */
    public ToClientMessage(MessageType messageType, String data) {
        this.messageType = messageType;
        this.data = data;
    }

    /**
     * Message handling procedure declaration.
     * @return Handling outcome.
     * @throws IOException Thrown when something goes wrong with communication.
     */
    public abstract boolean handle() throws IOException;

    /**
     * Getter for message type.
     * @return Type of message.
     */
    public MessageType getMessageType() {
        return messageType;
    }

    /**
     * Getter for m
     * @return
     */
    public String getData() {
        return data;
    }
}
