package messages;

import server.DatabaseConnector;
import utils.UserList;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;


/**
 * Abstract class representing messages sent between server and client.
 */
public abstract class Message implements Serializable {

    /**
     *  Type of message.
     */
    private final MessageType messageType;

    /**
     * Information passed with message.
     */
    private final String data;

    /**
     * ID of client who sent the message / to whom the message is being sent.
     */
    private String clientID;

    /**
     * Constructor for message.
     * @param messageType - Type of message.
     * @param data - Information passed with message.
     * @param clientID - ID of client.
     */
    public Message(MessageType messageType, String data, String clientID) {
        this.messageType = messageType;
        this.clientID = clientID;
        this.data = data;
    }

    /**
     * Message handling procedure, behaviour defined by type of message.
     * @return - Returns boolean describing result of message handling.
     * @throws IOException - Exception thrown if something went wrong with sending message.
     */
    public abstract boolean handleMessage(UserList userList, DatabaseConnector databaseConnector) throws IOException, SQLException;

    /**
     * Getter for message type.
     * @return - Message type.
     */
    public MessageType getMessageType() {
        return messageType;
    }

    /**
     * Getter for message's information.
     * @return - Message information.
     */
    public String getData() {
        return data;
    }

    /**
     * Getter for client's ID.
     * @return - ID of client.
     */
    public String getClientID() {
        return clientID;
    }

    /**
     * Setter for client's ID
     * @param clientID - Client id to be set.
     */
    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String [] parseData() {
        return getData().trim().split("\\|");
    }
}
