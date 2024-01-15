package messages.toServer.requests;

import exceptions.PlayException;
import messages.MessageType;
import messages.toClient.responses.StartGameResponse;
import messages.toServer.ToServerMessage;
import server.DatabaseConnector;
import utils.GameDetails;
import utils.User;
import utils.UserList;
import utils.model.Room;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class StartGameRequest extends ToServerMessage {

    private int roomId;

    public StartGameRequest(int roomId) {
        super(MessageType.StartGameRequest, String.format("Start game %s", roomId), null);
        this.roomId = roomId;
    }

    @Override
    public boolean handle(UserList userList, DatabaseConnector databaseConnector, GameDetails gameDetails) throws IOException, SQLException {
        Optional<User> user = userList.getUserByConnectionId(getConnectionId());
        if (!user.isPresent()) {
            System.out.printf("User %s does not exist.\n", getConnectionId());
            return false;
        }
        Optional<Room> room = gameDetails.getRoomById(roomId);
        if (!room.isPresent()) {
            sendError(user.get(), String.format("Room %s does not exist", roomId));
            return false;
        }
        if (!startGame(user.get(), room.get())) {
            return false;
        }
        broadcastRoomDetails(userList, gameDetails);
        return true;
    }

    private void sendError(User user, String message) {
        try {
            user.getOutputStream().writeObject(new StartGameResponse(ToServerMessage.FAILURE, message));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean startGame(User user, Room room) {
        try {
            if (!room.isUserIdOwner(user.getUserID())){
                throw new PlayException(String.format("User %s is not an owner of the room", user.getUserID()));
            }
            room.start();
            return true;
        } catch (PlayException e) {
            sendError(user, e.getErrorCause());
            return false;
        }
    }
}
