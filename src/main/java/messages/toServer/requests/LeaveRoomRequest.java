package messages.toServer.requests;

import exceptions.ClientRoomJoinException;
import exceptions.ClientRoomLeaveException;
import messages.MessageType;
import messages.toClient.responses.JoinRoomResponse;
import messages.toClient.responses.LeaveRoomResponse;
import messages.toServer.ToServerMessage;
import server.DatabaseConnector;
import utils.GameDetails;
import utils.User;
import utils.UserList;
import utils.model.Room;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class LeaveRoomRequest extends ToServerMessage {

    private int roomId;

    public LeaveRoomRequest(int roomId) {
        super(MessageType.LeaveRoomRequest, String.format("Leave room request %s", roomId), null);
        this.roomId = roomId;
    }

    @Override
    public boolean handle(UserList userList, DatabaseConnector databaseConnector, GameDetails gameDetails) throws IOException, SQLException {
        Optional<User> user = userList.getUserByConnectionId(getConnectionId());
        if (!user.isPresent()){
            System.out.printf("Leave Room Request: Cannot find user for connection id: %s\n", getConnectionId());
            return false;
        }
        Optional<Room> room = gameDetails.getRoomById(roomId);
        if (!room.isPresent()) {
            sendError(user.get(), String.format("Failed to join room %s", roomId));
            return false;
        }
        if (!leaveRoom(room.get(), user.get())) {
            return false;
        }
        deleteRoomIfOwnerLeft(user.get(), room.get(), gameDetails);
        sendSuccess(user.get(), String.format("Successfully joined room %s", room.get().getId()));
        broadcastGameDetails(userList, gameDetails);
        broadcastRoomDetails(userList, gameDetails);
        return true;
    }

    private boolean leaveRoom(Room room, User user) {
        try {
            room.leave(user);
            return true;
        } catch (ClientRoomLeaveException e) {
            sendError(user, e.getErrorCause());
            return false;
        }
    }

    private void deleteRoomIfOwnerLeft(User user, Room room, GameDetails gameDetails) {
        if (room.isUserOwner(user)) {
            gameDetails.deleteRoom(room.getId());
        }
    }

    private void sendError(User user, String error) {
        try {
            user.getOutputStream().writeObject(new LeaveRoomResponse(ToServerMessage.FAILURE, error));
        } catch (IOException e) {
            System.out.println("S: Failed to send leave room response.");
        }
    }

    private void sendSuccess(User user, String message) {
        try {
            user.getOutputStream().writeObject(new LeaveRoomResponse(ToServerMessage.SUCCESS, message));
        } catch (IOException e) {
            System.out.println("S: Failed to send leave room response.");
        }
    }
}
