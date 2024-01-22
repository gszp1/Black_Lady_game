package messages.toServer.requests;

import exceptions.PlayException;
import messages.MessageType;
import messages.dto.GameView;
import messages.toClient.responses.GameDetailsResponse;
import messages.toServer.ToServerMessage;
import server.DatabaseConnector;
import utils.GameDetails;
import utils.User;
import utils.UserList;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Request for game details.
 */
public class GameDetailsRequest extends ToServerMessage {
    /**
     * Constructor for message.
     */
    public GameDetailsRequest() {
        super(MessageType.GameDetailsRequest, "Game details request", null);
    }
    
    /**
     * Request Handling procedure.
     * @param userList List of users.
     * @param databaseConnector Connection to database.
     * @param gameDetails Data about active game rooms.
     * @return Boolean.
     * @throws IOException Exception for connection errors.
     * @throws SQLException Exception for database connection error.
     */
    @Override
    public boolean handle(
            UserList userList,
            DatabaseConnector databaseConnector,
            GameDetails gameDetails
    ) throws IOException, SQLException {
        try {
            Optional<User> user = findUserByConnectionId(userList);
            if (!user.isPresent()) {
                throw new PlayException("User is not logged in");
            }
            GameView gameView = GameView.fromGameDetails(gameDetails, user.get().getUserID());
            user.get().getOutputStream().writeObject(new GameDetailsResponse(gameView));
            return true;
        } catch (PlayException e) {
            System.out.printf("Error when reaching game view details by user %s", getConnectionId());
            return false;
        }
    }
}
