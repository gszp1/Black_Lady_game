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

/**
 * Request for playing selected card.
 */
public class PlayCardRequest extends ToServerMessage {

    /**
     * Room's ID.
     */
    private final int roomId;

    /**
     * Card.
     */
    private final Card card;

    /**
     * Constructor.
     * @param roomId Room's ID.
     * @param card Card;
     */
    public PlayCardRequest(int roomId, Card card) {
        super(MessageType.PlayCardRequest, String.format("%s|%s", roomId, card.toString()), null);
        this.roomId = roomId;
        this.card = card;
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

    /**
     * Play given card.
     * @param user User.
     * @param card Card.
     * @param room Room.
     * @return Boolean.
     */
    private boolean playCard(User user, Card card, Room room) {
        try {
            room.playCard(user.getUserID(), card);
            return true;
        } catch (PlayException e) {
            sendError(user, String.format("You cannot play card %s", card));
            return false;
        }
    }

    /**
     * Sends error.
     * @param user User.
     * @param message Error message.
     */
    private void sendError(User user, String message) {
        try {
            user.getOutputStream().writeObject(new PlayCardResponse(ToServerMessage.FAILURE, message));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends success message.
     * @param user User.
     * @param message Success message.
     */
    private void sendSuccess(User user, String message) {
        try {
            user.getOutputStream().writeObject(new PlayCardResponse(ToServerMessage.SUCCESS, message));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
