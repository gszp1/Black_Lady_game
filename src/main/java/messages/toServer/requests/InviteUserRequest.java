package messages.toServer.requests;

import messages.MessageType;
import messages.toClient.responses.InviteUserResponse;
import messages.toClient.responses.RoomInviteToReceiverRequest;
import messages.toServer.ToServerMessage;
import server.DatabaseConnector;
import utils.GameDetails;
import utils.User;
import utils.UserList;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class InviteUserRequest extends ToServerMessage {

    private String receiverEmail;

    private int roomId;

    public InviteUserRequest(String receiverEmail, int roomId){
        super(MessageType.InviteUserRequest, String.format("Invite user request to room %s sent to email %s", roomId, receiverEmail), null);
        this.receiverEmail = receiverEmail;
        this.roomId = roomId;
    }

    @Override
    public boolean handle(UserList userList, DatabaseConnector databaseConnector, GameDetails gameDetails) throws IOException, SQLException {
        Optional<User> user = userList.getUserByConnectionId(getConnectionId());
        if (!user.isPresent()) {
            System.out.printf("S: Cannot find user by connection id: %s\n", getConnectionId());
            return false;
        }
        Optional<User> userToInvite = userList.getUserByEmail(receiverEmail);
        if (!userToInvite.isPresent()){
            sendBackError(user.get(), String.format("User %s does not exist.", user.get().getEmail()));
            return false;
        }
        sendInvitationRequestToUser(user.get(), userToInvite.get());
        return true;
    }

    private void sendInvitationRequestToUser(User sender, User receiver) {
        try {
            receiver.getOutputStream().writeObject(new RoomInviteToReceiverRequest(receiverEmail, sender.getEmail(), roomId));
        } catch (IOException e) {
            System.out.println("S: Failed to send invitation request to user.");
        }
    }

    private void sendBackError(User user, String error) {
        try {
            user.getOutputStream().writeObject(new InviteUserResponse(ToServerMessage.SUCCESS, error));
        } catch (IOException e) {
            System.out.println("S: Failed to send back error regarding invitation request.");
        }
    }
}
