package messages.toServer;

import messages.MessageType;
import server.DatabaseConnector;
import utils.GameDetails;
import utils.User;
import utils.UserList;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Optional;


/**
 * Abstract class representing messages sent between server and client.
 */
public abstract class ToServerMessage implements Serializable {

    public static String SUCCESS = "Success";

    public static String FAILURE = "Failure";

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
    private String connectionId;

    /**
     * Constructor for message.
     * @param messageType - Type of message.
     * @param data - Information passed with message.
     */
    public ToServerMessage(MessageType messageType, String data, String connectionId) {
        this.messageType = messageType;
        this.data = data;
        this.connectionId = connectionId;
    }

    /**
     * ToServerMessage handling procedure, behaviour defined by type of message.
     * @param userList List of users.
     * @param databaseConnector Connection to database.
     * @return - Boolean telling if operation was successful.
     * @throws IOException - Exception thrown if something went wrong with sending the message.
     * @throws SQLException Thrown if something went wrong with database connection.
     */
    public abstract boolean handle(
            UserList userList,
            DatabaseConnector databaseConnector,
            GameDetails gameDetails
    ) throws IOException, SQLException;

    /**
     * Getter for message type.
     * @return - ToServerMessage type.
     */
    public MessageType getMessageType() {
        return messageType;
    }

    /**
     * Getter for message's information.
     * @return - ToServerMessage information.
     */
    public String getData() {
        return data;
    }

    /**
     * Getter for client's ID.
     * @return - ID of client.
     */
    public String getConnectionId() {
        return connectionId;
    }

    /**
     * Setter for client's ID
     */
    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    protected Optional<User> findUserByConnectionId(UserList userList) {
        return userList.getUserByConnectionId(connectionId);
    }
}
