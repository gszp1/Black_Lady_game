package messages.toServer.requests;

import exceptions.ClientRoomJoinException;
import messages.MessageType;
import messages.toClient.responses.InviteUserResponse;
import messages.toClient.responses.RoomInviteToReceiverRequest;
import messages.toServer.ToServerMessage;
import server.DatabaseConnector;
import utils.GameDetails;
import utils.User;
import utils.UserList;
import utils.model.Room;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class RoomInviteAcceptRequest extends ToServerMessage {

    private final int roomId;
    private final String accepterEmail;
    private final String inviterEmail;

    public RoomInviteAcceptRequest(int roomId, String accepterEmail, String inviterEmail) {
        super(MessageType.RoomInviteAcceptRequest, String.format("Accept request to room %s sent from %s to %s", roomId, inviterEmail, accepterEmail), null);
        this.roomId = roomId;
        this.accepterEmail = accepterEmail;
        this.inviterEmail = inviterEmail;
    }

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

    private boolean joinRoom(Room room, User accepter) {
        try {
            room.join(accepter);
            return true;
        } catch (ClientRoomJoinException e) {
            sendError(accepter, "You are already in a room");
            return false;
        }
    }

    private void sendError(User user, String msg) {
        try{
            user.getOutputStream().writeObject(new InviteUserResponse(ToServerMessage.FAILURE, msg));
        } catch (IOException e) {
            System.out.printf("Failed to send invite response to user %s\n", user.getEmail());
        }
    }

    private void sendSuccess(User user, String msg) {
        try {
            user.getOutputStream().writeObject(new InviteUserResponse(ToServerMessage.SUCCESS, msg));
        } catch (IOException e) {
            System.out.printf("Failed to send invite response to user %s\n", user.getEmail());
        }
    }
}
