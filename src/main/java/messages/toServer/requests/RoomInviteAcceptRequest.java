package messages.toServer.requests;

import exceptions.ClientRoomJoinException;
import messages.MessageType;
import messages.toClient.responses.InviteUserResponse;
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
 * Request for accepting invitation.
 */
public class RoomInviteAcceptRequest extends ToServerMessage {

    /**
     * Room's ID.
     */
    private final int roomId;
    /**
     * Acceptor's email.
     */
    private final String accepterEmail;

    /**
     * Inviter's email.
     */
    private final String inviterEmail;


    /**
     * Constructor for message.
     * @param roomId Room's ID.
     * @param accepterEmail Acceptor's email.
     * @param inviterEmail Inviter's email.
     */
    public RoomInviteAcceptRequest(int roomId, String accepterEmail, String inviterEmail) {
        super(MessageType.RoomInviteAcceptRequest, String.format("Accept request to room %s sent from %s to %s", roomId, inviterEmail, accepterEmail), null);
        this.roomId = roomId;
        this.accepterEmail = accepterEmail;
        this.inviterEmail = inviterEmail;
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
        Optional<User> accepter = userList.getUserByConnectionId(getConnectionId());
        if (!accepter.isPresent()){
            System.out.println("S: Accepter who sent invite accept is not logged in.");
            return false;
        }
        Optional<User> inviter = userList.getUserByEmail(inviterEmail);
        if (!inviter.isPresent()) {
            sendError(accepter.get(), String.format("Inviter with email %s does not exist", inviterEmail));
            return false;
        }

        Optional<Room> roomToJoin = gameDetails.getRoomById(roomId);
        if (!roomToJoin.isPresent()) {
            sendError(accepter.get(), String.format("Room %s does not exist", roomId));
            sendError(inviter.get(), String.format("Room %s does not exist", roomId));
            return false;
        }
        if (!joinRoom(roomToJoin.get(), accepter.get())) {
            return false;
        }
        broadcastGameDetails(userList, gameDetails);
        broadcastRoomDetails(userList, gameDetails);
        sendSuccess(accepter.get(), String.format("Successfully joined room %s", roomId));
        sendSuccess(inviter.get(), String.format("User %s joined the room", accepter.get().getEmail()));
        return true;
    }

    /**
     * Join room.
     * @param room Room.
     * @param accepter Invitation accepter.
     * @return Boolean.
     */
    private boolean joinRoom(Room room, User accepter) {
        try {
            room.join(accepter);
            return true;
        } catch (ClientRoomJoinException e) {
            sendError(accepter, "You are already in a room");
            return false;
        }
    }

    /**
     * Sends error message.
     * @param user User.
     * @param msg Success message.
     */
    private void sendError(User user, String msg) {
        try{
            user.getOutputStream().writeObject(new InviteUserResponse(ToServerMessage.FAILURE, msg));
        } catch (IOException e) {
            System.out.printf("Failed to send invite response to user %s\n", user.getEmail());
        }
    }

    /**
     * Sends success message.
     * @param user User.
     * @param msg Success message.
     */
    private void sendSuccess(User user, String msg) {
        try {
            user.getOutputStream().writeObject(new InviteUserResponse(ToServerMessage.SUCCESS, msg));
        } catch (IOException e) {
            System.out.printf("Failed to send invite response to user %s\n", user.getEmail());
        }
    }
}
