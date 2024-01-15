package messages.dto;

import cards.Card;
import client.ServerConnector;
import lombok.Builder;
import lombok.Data;
import utils.UserList;
import utils.model.ChatEntry;
import utils.model.Room;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Builder
public class RoomDetails implements Serializable {
    private int roomId;
    private boolean started;
    private Map<UserView, Integer> scores;
//    private List<TrickDetails> tricks;
    private List<Card> myCards;
    private List<Card> cardsOnTable;
    private boolean isMyTurn;
    private boolean isMyRoom;
    private List<UserView> nonInvitedUsers;
    private boolean isReadyToStart;
    private List<ChatEntryView> chat;

    public static RoomDetails fromRoom(Room room, UserList userList, String loggedInUserId) {
        return RoomDetails.builder()
                .roomId(room.getId())
                .started(room.isStarted())
                .scores(getScores(room, loggedInUserId))
                .myCards(getMyCards(room, loggedInUserId))
                .cardsOnTable(room.getCardsOnTable())
                .isMyTurn(false)
                .isMyRoom(room.isUserIdOwner(loggedInUserId))
                .nonInvitedUsers(getNonInvitedUsers(room, userList, loggedInUserId))
                .isReadyToStart(!room.isStarted() && room.isMaxParticipants() && room.isUserIdOwner(loggedInUserId))
                .chat(room.getChatEntries().stream().map(ChatEntryView::fromChatEntry).collect(Collectors.toList()))
                .build();
    }

    private static Map<UserView, Integer> getScores(Room room, String loggedInUserId) {
        Map<UserView, Integer> scores = new HashMap<>();
        System.out.println("SCORES:");
        System.out.println(room.getScores());
        room.getScores().forEach((email, score) -> {
            room.getParticipantByEmail(email).ifPresent(user -> {
                UserView userView = UserView.fromUser(user, loggedInUserId);
                scores.put(userView, score);
            });
        });
        return scores;
    }

    private static List<Card> getMyCards(Room room, String loggedInUserId) {
        return room.getPlay()
                .map(play -> play.getCards(loggedInUserId))
                .orElse(new ArrayList<>());
    }

    private static List<UserView> getNonInvitedUsers(Room room, UserList userList, String loggedInUserId) {
        return userList.getUsers().stream()
                .filter(user -> user.getUserID() != null) // TODO - change to user.idLoggedIn
                .filter(user -> !room.isUserInRoom(user))
                .map(user -> UserView.fromUser(user, loggedInUserId))
                .collect(Collectors.toList());
    }
}
