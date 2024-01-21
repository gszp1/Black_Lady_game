package messages.toServer.requests;

import exceptions.ClientRoomJoinException;
import lombok.Getter;
import messages.MessageType;
import messages.toClient.responses.JoinRoomResponse;
import messages.toServer.ToServerMessage;
import server.DatabaseConnector;
import utils.GameDetails;
import utils.User;
import utils.UserList;
import utils.model.Room;

import java.util.Optional;

import java.io.IOException;
import java.sql.SQLException;
import java.util.stream.Collectors;

@Getter
public class JoinRoomRequest extends ToServerMessage {

    final private int roomId;

    public JoinRoomRequest(int roomId) {
        super(MessageType.JoinRoomRequest, String.format("Request to join room %s", roomId), null);
        this.roomId = roomId;
    }

    @Override
    public boolean handle(UserList userList, DatabaseConnector databaseConnector, GameDetails gameDetails) throws IOException, SQLException {
        Optional<User> user = userList.getUserByConnectionId(getConnectionId());
        if (!user.isPresent()){
            System.out.printf("User with connection id %s does not exist\n", getConnectionId());
            return false;
        }
        Optional<Room> room = gameDetails.getRoomById(roomId);
        if (!room.isPresent()){
            sendError(user.get(), String.format("Room with id %s does not exist\n", roomId));
            return false;
        }
        if (!joinRoom(room.get(), user.get())) {
            return false;
        }
        sendSuccess(user.get(), String.format("Successfully joined room %s", room.get().getId()));
        broadcastGameDetails(userList, gameDetails);
        broadcastRoomDetails(userList, gameDetails);
        return true;
    }

    private boolean joinRoom(Room room, User user) {
        try {
            room.join(user);
            return true;
        } catch (ClientRoomJoinException e) {
            sendError(user, e.getErrorCause());
            return false;
        }
    }

    private void sendError(User user, String error) {
        try {
            user.getOutputStream().writeObject(new JoinRoomResponse(ToServerMessage.FAILURE, error));
        } catch (IOException e) {
            System.out.println("S: Failed to send join room response.");
        }
    }

    private void sendSuccess(User user, String message) {
        try {
            user.getOutputStream().writeObject(new JoinRoomResponse(ToServerMessage.SUCCESS, message));
        } catch (IOException e) {
            System.out.println("S: Failed to send join room response.");
        }
    }
}
