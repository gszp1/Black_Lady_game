package messages.toServer;

import messages.MessageType;
import messages.dto.GameView;
import messages.dto.RoomDetails;
import messages.toClient.responses.GameDetailsResponse;
import messages.toClient.responses.RoomDetailsResponse;
import server.DatabaseConnector;
import utils.GameDetails;
import utils.User;
import utils.UserList;
import utils.model.Room;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Function;


/**
 * Abstract class representing messages sent between server and client.
 */
public abstract class ToServerMessage implements Serializable {

    /**
     * Success result.
     */
    public static String SUCCESS = "Success";

    /**
     * Failure result.
     */
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
     * @param connectionId User's connection ID.
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
     * @param gameDetails Rooms data.
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
     * @param connectionId User's connection ID.
     */
    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    /**
     * Finds user with given userID.
     * @param userList Users list.
     * @return Optional with user.
     */
    protected Optional<User> findUserByConnectionId(UserList userList) {
        return userList.getUserByConnectionId(connectionId);
    }

    /**
     * Send data about rooms to users.
     * @param userList List of users.
     * @param gameDetails Rooms data.
     */
    public void broadcastGameDetails(UserList userList, GameDetails gameDetails) {
        try {
            Function<User, GameView> createGameView = (user) -> GameView.fromGameDetails(gameDetails, user.getUserID());
            for (User user : userList.getUsers()) {
                user.getOutputStream().writeObject(new GameDetailsResponse(createGameView.apply(user)));
            }
        } catch (IOException e) {
            System.out.println("S: Cannot write Game Details");
        }
    }

    /**
     * For each user send data about rooms he joined.
     * @param userList List of users.
     * @param gameDetails Rooms data.
     */
    public void broadcastRoomDetails(UserList userList, GameDetails gameDetails) {
        try {
            for (Room room : gameDetails.getRooms()) {
                for (User participant: room.getParticipants()) {
                    participant.getOutputStream().writeObject(
                            new RoomDetailsResponse(RoomDetails.fromRoom(room, userList, participant.getUserID()))
                    );
                }
            }
        } catch (IOException e) {
            System.out.println("S: Cannot Write Room Details");
        }
    }
}
