package messages.toServer.requests;

import exceptions.RequestFailureException;
import messages.MessageType;
import messages.toServer.ToServerMessage;
import server.DatabaseConnector;
import utils.GameDetails;
import utils.User;
import utils.UserList;
import utils.model.UserData;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

/**
 * ToServerMessage for requesting other user to join our room.
 */
public class UserInviteRequest extends ToServerMessage {

    private final String inviterUsername;

    private final String recipientUsername;

    private final String gameRoomID;

    /**
     * Constructor
     * @param gameRoomID ID of gameRoom in which user is sitting.
     * @param userID ID of user who sends message.
     */
    public UserInviteRequest(String inviterUsername, String recipientUsername, String gameRoomID, String userID) {
        super(MessageType.InviteUserRequest, String.format("%s|%s|%s", inviterUsername, recipientUsername, gameRoomID), userID);
        this.gameRoomID = gameRoomID;
        this.inviterUsername = inviterUsername;
        this.recipientUsername = recipientUsername;
    }

    /**
     * User invite request handling procedure.
     * @param userList List of users.
     * @param databaseConnector Connection to database.
     * @return - Boolean telling if operation was successful.
     * @throws IOException - Exception thrown if something went wrong with sending the message.
     * @throws SQLException Thrown if something went wrong with database connection.
     */
    public boolean handle(UserList userList, DatabaseConnector databaseConnector, GameDetails gameDetails) throws IOException, SQLException {
        try {
            Optional<User> user = userList.getUserByUsername(recipientUsername); // get user recipient from userList
            if (!user.isPresent()) {
                throw new RequestFailureException(RequestFailureException.MESSAGE_RECEIVER_NOT_FOUND);
            }

        } catch (RequestFailureException e) {

        }

        return true;
    }

    private boolean sendMessage(String result, String data, String userID, UserList userList) {
        return true;
    }
}
