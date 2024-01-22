package messages.toServer.requests;

import exceptions.ClientRoomLeaveException;
import messages.MessageType;
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

/**
 * Request for creating game room.
 */
public class LeaveRoomRequest extends ToServerMessage {

    /**
     * ID of room user wants to leave.
     */
    private int roomId;

    /**
     * Constructor for message.
     * @param roomId Room ID.
     */
    public LeaveRoomRequest(int roomId) {
        super(MessageType.LeaveRoomRequest, String.format("Leave room request %s", roomId), null);
        this.roomId = roomId;
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
        deleteRoomIfGameInProgress(room.get(), gameDetails);
        sendSuccess(user.get(), String.format("Successfully joined room %s", room.get().getId()));
        broadcastGameDetails(userList, gameDetails);
        broadcastRoomDetails(userList, gameDetails);
        return true;
    }

    /**
     * Procedure for leaving room.
     * @param room Room.
     * @param user User.
     * @return Boolean.
     */
    private boolean leaveRoom(Room room, User user) {
        try {
            room.leave(user);
            return true;
        } catch (ClientRoomLeaveException e) {
            sendError(user, e.getErrorCause());
            return false;
        }
    }

    /**
     * Checks if leaving player is an owner and removes room if he is.
     * @param user User.
     * @param room Room.
     * @param gameDetails Server game details.
     */
    private void deleteRoomIfOwnerLeft(User user, Room room, GameDetails gameDetails) {
        if (room.isUserOwner(user)) {
            gameDetails.deleteRoom(room.getId());
        }
    }

    /**
     * Removes room if game is in progress.
     * @param room Room.
     * @param gameDetails Server game details.
     */
    private void deleteRoomIfGameInProgress(Room room, GameDetails gameDetails) {
        if (room.isStarted()) {
            gameDetails.deleteRoom(room.getId());
        }
    }

    /**
     * Sends error to user.
     * @param user User.
     * @param error Error message.
     */
    private void sendError(User user, String error) {
        try {
            user.getOutputStream().writeObject(new LeaveRoomResponse(ToServerMessage.FAILURE, error));
        } catch (IOException e) {
            System.out.println("S: Failed to send leave room response.");
        }
    }
    /**
     *
     * Sends success message to user.
     * @param user User.
     * @param message message contents.
     */
    private void sendSuccess(User user, String message) {
        try {
            user.getOutputStream().writeObject(new LeaveRoomResponse(ToServerMessage.SUCCESS, message));
        } catch (IOException e) {
            System.out.println("S: Failed to send leave room response.");
        }
    }
}
