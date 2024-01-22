package messages.dto;

import cards.Card;
import lombok.Builder;
import lombok.Data;
import utils.User;
import utils.UserList;
import utils.model.Room;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Data transfer object containing room's data and game data for recipient.
 */
@Data
@Builder
public class RoomDetails implements Serializable {

    /**
     * ID of room.
     */
    private int roomId;

    /**
     * Is the game started.
     */
    private boolean started;

    /**
     * Map associating user with his score.
     */
    private Map<UserView, Integer> scores;

    /**
     * List player's cards.
     */
    private List<Card> myCards;

    /**
     * Is this user's turn.
     */
    private boolean isMyTurn;

    /**
     * Was this room created by this user.
     */
    private boolean isMyRoom;

    /**
     * Users who were not invited to this room.
     */
    private List<UserView> nonInvitedUsers;

    /**
     * Is the game ready to start.
     */
    private boolean isReadyToStart;

    /**
     * Room chat entries.
     */
    private List<ChatEntryView> chat;

    /**
     * Cards on table.
     */
    private Map<String, Card> cardsOnTable;

    /**
     * Trick from last tour.
     */
    private Map<String, Card> lastTrick;

    /**
     * Player who picked trick.
     */
    private String lastTrickPicker;

    /**
     * Order of users.
     */
    private List<String> usersOrder;

    /**
     * Map for relating user's id with email
     */
    private Map<String, String> userIdsToEmailsMapping;

    /**
     *  Email of user to whom this dto is sent.
     */
    private String myEmail;

    /**
     * ID of dto receiver.
     */
    private String myUserId;

    /**
     * ID of user who is making his move.
     */
    private String currentPlayingUserId;

    /**
     * First card of current trick put on the table
     */
    private Card firstCardOnTable;

    /**
     * Did the game finish.
     */
    private boolean isFinished;

    /**
     * Creates RoomDetails instance, basing on given parameters.
     * @param room Room data.
     * @param userList List of users.
     * @param loggedInUserId ID of user.
     * @return RoomDetails instance
     */
    public static RoomDetails fromRoom(Room room, UserList userList, String loggedInUserId) {
        return RoomDetails.builder()
                .roomId(room.getId())
                .started(room.isStarted())
                .scores(getScores(room, loggedInUserId))
                .myCards(new ArrayList<>(getMyCards(room, loggedInUserId)))
                .cardsOnTable(room.getCardsOnTable())
                .lastTrick(room.getLastTrick())
                .isMyTurn(room.isUserTurn(loggedInUserId))
                .isMyRoom(room.isUserIdOwner(loggedInUserId))
                .nonInvitedUsers(getNonInvitedUsers(room, userList, loggedInUserId))
                .isReadyToStart(!room.isStarted() && room.isMaxParticipants() && room.isUserIdOwner(loggedInUserId))
                .chat(room.getChatEntries().stream().map(ChatEntryView::fromChatEntry).collect(Collectors.toList()))
                .usersOrder(room.getPlayersOrder())
                .userIdsToEmailsMapping(room.getUserIdsToEmailsMapping())
                .myEmail(getMyEmail(userList, loggedInUserId))
                .myUserId(loggedInUserId)
                .currentPlayingUserId(room.getCurrentPlayingUserId())
                .firstCardOnTable(room.getFirstCardOnTable())
                .isFinished(room.hasGameFinished())
                .build();
    }

    /**
     * Gets users scores in given room.
     * @param room Room data.
     * @param loggedInUserId ID of user.
     * @return Map of users scores.
     */
    private static Map<UserView, Integer> getScores(Room room, String loggedInUserId) {
        Map<UserView, Integer> scores = new HashMap<>();
        room.getScores().forEach((email, score) -> {
            room.getParticipantByEmail(email).ifPresent(user -> {
                UserView userView = UserView.fromUser(user, loggedInUserId);
                scores.put(userView, score);
            });
        });
        return scores;
    }

    /**
     * Returns cards of user with ID loggedInUserID.
     * @param room Room data.
     * @param loggedInUserId ID of user.
     * @return List of cards.
     */
    private static List<Card> getMyCards(Room room, String loggedInUserId) {
        return room.getPlay()
                .map(play -> play.getCards(loggedInUserId))
                .orElse(new ArrayList<>());
    }

    /**
     * Get users not invited to room.
     * @param room Room data.
     * @param userList List of users.
     * @param loggedInUserId ID of user.
     * @return List of users.
     */
    private static List<UserView> getNonInvitedUsers(Room room, UserList userList, String loggedInUserId) {
        return userList.getUsers().stream()
                .filter(user -> user.getUserID() != null)
                .filter(user -> !room.isUserInRoom(user))
                .map(user -> UserView.fromUser(user, loggedInUserId))
                .collect(Collectors.toList());
    }

    /**
     * Gets user's email.
     * @param userList List of users.
     * @param loggedInUserId ID of user.
     * @return User's email.
     */
    private static String getMyEmail(UserList userList, String loggedInUserId) {
        return userList.getUserByUserID(loggedInUserId)
                .map(User::getEmail)
                .orElse("");
    }
}
