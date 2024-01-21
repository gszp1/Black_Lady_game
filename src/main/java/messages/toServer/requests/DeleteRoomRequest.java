package messages.toServer.requests;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import messages.MessageType;
import messages.toClient.responses.DeleteRoomResponse;
import messages.toServer.ToServerMessage;
import server.DatabaseConnector;
import utils.GameDetails;
import utils.User;
import utils.UserList;
import utils.model.Room;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
public class DeleteRoomRequest extends ToServerMessage {

    private final int roomId;

    public DeleteRoomRequest(int roomId) {
        super(MessageType.DeleteRoomRequest, String.format("Deleting room id %s", roomId), "");
        this.roomId = roomId;
    }

    @Override
    public boolean handle(UserList userList, DatabaseConnector databaseConnector, GameDetails gameDetails) throws IOException, SQLException {
        Optional<User> user = findUserByConnectionId(userList);
        if (!user.isPresent()){
            System.out.printf("User with connection id %s is not logged in!", getConnectionId());
            return false;
        }
        List<Integer> existingRoomIds = gameDetails.getRooms().stream().map(Room::getId).collect(Collectors.toList());
        if (!existingRoomIds.contains(roomId)) {
            user.get().getOutputStream().writeObject(new DeleteRoomResponse(ToServerMessage.FAILURE, "Room id does not exist"));
            return false;
        }
        gameDetails.deleteRoom(roomId);
        broadcastGameDetails(userList, gameDetails);
        return true;
    }
}
