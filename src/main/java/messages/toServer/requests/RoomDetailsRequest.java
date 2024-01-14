package messages.toServer.requests;

import messages.MessageType;
import messages.dto.RoomDetails;
import messages.toClient.responses.RoomDetailsResponse;
import messages.toServer.ToServerMessage;
import server.DatabaseConnector;
import utils.GameDetails;
import utils.User;
import utils.UserList;
import utils.model.Room;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class RoomDetailsRequest extends ToServerMessage {

    private int roomId;

    public RoomDetailsRequest(int roomId) {
        super(MessageType.RoomDetailsRequest, String.format("%s", roomId), null);
        this.roomId = roomId;
    }

    @Override
    public boolean handle(UserList userList, DatabaseConnector databaseConnector, GameDetails gameDetails) throws IOException, SQLException {
        Optional<User> user = userList.getUserByConnectionId(getConnectionId());
        if (!user.isPresent()){
            System.out.printf("S: User with id %s is not connected.\n", getConnectionId());
            return false;
        }
        Optional<Room> room = gameDetails.getRoomById(roomId);
        if (!room.isPresent()){
            System.out.printf("S: Tried to get details of room with id %s\n", room.get().getId());
            return false;
        }
        user.get().getOutputStream().writeObject(new RoomDetailsResponse(
                RoomDetails.fromRoom(room.get(), userList, user.get().getUserID())
        ));

        return true;
    }
}
