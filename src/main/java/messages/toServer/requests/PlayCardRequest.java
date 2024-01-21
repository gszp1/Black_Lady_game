package messages.toServer.requests;

import cards.Card;
import exceptions.PlayException;
import messages.MessageType;
import messages.toClient.responses.PlayCardResponse;
import messages.toServer.ToServerMessage;
import server.DatabaseConnector;
import utils.GameDetails;
import utils.User;
import utils.UserList;
import utils.model.Play;
import utils.model.Room;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class PlayCardRequest extends ToServerMessage {

    private final int roomId;

    private final Card card;

    public PlayCardRequest(int roomId, Card card) {
        super(MessageType.PlayCardRequest, String.format("%s|%s", roomId, card.toString()), null);
        this.roomId = roomId;
        this.card = card;
    }

    @Override
    public boolean handle(UserList userList, DatabaseConnector databaseConnector, GameDetails gameDetails) throws IOException, SQLException {
        Optional<User> loggedInUser = userList.getUserByConnectionId(getConnectionId());
        if (!loggedInUser.isPresent()) {
            System.out.println("S: Logged in user does not exist");
            return false;
        }
        Optional<Room> room = gameDetails.getRoomById(roomId);
        if (!room.isPresent()) {
            sendError(loggedInUser.get(), String.format("Room %s does not exist", roomId));
            return false;
        }
        Optional<Play> play = room.get().getPlay();
        if (!play.isPresent()) {
            sendError(loggedInUser.get(), String.format("Game has not yet started in room %s", room.get().getId()));
            return false;
        }
        if (!playCard(loggedInUser.get(), card, room.get())) {
            return false;
        }
        broadcastRoomDetails(userList, gameDetails);
        return true;
    }

    private boolean playCard(User user, Card card, Room room) {
        try {
            room.playCard(user.getUserID(), card);
            return true;
        } catch (PlayException e) {
            sendError(user, String.format("You cannot play card %s", card));
            return false;
        }
    }

    private void sendError(User user, String message) {
        try {
            user.getOutputStream().writeObject(new PlayCardResponse(ToServerMessage.FAILURE, message));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendSuccess(User user, String message) {
        try {
            user.getOutputStream().writeObject(new PlayCardResponse(ToServerMessage.SUCCESS, message));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
