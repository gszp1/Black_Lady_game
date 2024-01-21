package messages.toServer.requests;

import exceptions.ClientRoomJoinException;
import messages.MessageType;
import messages.toServer.ToServerMessage;
import server.DatabaseConnector;
import utils.GameDetails;
import utils.User;
import utils.UserList;
import utils.model.Room;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class CreateRoomRequest extends ToServerMessage {
    /**
     * Constructor for message.
     */
    public CreateRoomRequest() {
        super(MessageType.CreateGameRequest, "Create Game request", "");
    }

    @Override
    public boolean handle(UserList userList, DatabaseConnector databaseConnector, GameDetails gameDetails) throws IOException, SQLException {
        Optional<User> user = findUserByConnectionId(userList);
        if (!user.isPresent()) {
            System.out.printf("User with connection id %s is not logged in!", getConnectionId());
            return false;
        }
        final Room newRoom = gameDetails.createRoom(user.get());
        broadcastGameDetails(userList, gameDetails);
        return true;
    }
}
