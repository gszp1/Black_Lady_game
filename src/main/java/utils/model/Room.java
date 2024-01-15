package utils.model;

import cards.Card;
import exceptions.ClientRoomJoinException;
import exceptions.ClientRoomLeaveException;
import exceptions.PlayException;
import lombok.Getter;
import utils.User;

import java.util.*;

/**
 *
 */
@Getter
public class Room {

    public static int MAX_PLAYERS = 4;

    private int id;

    private List<User> participants = new ArrayList<>();

    private Map<String, Integer> scores = new HashMap<>();

    private User owner;

    private List<Play> playHistory = new ArrayList<>();

    private Optional<Play> play = Optional.empty();

    private List<ChatEntry> chatEntries = new ArrayList<>();

    public Room(User owner, int id) {
        this.owner = owner;
        this.id = id;
        scores.put(owner.getEmail(), 0);
        participants.add(owner);
    }

    public void writeToChat(String email, String message) throws PlayException {
        if (!isUserInRoomByEmail(email)) {
            throw new PlayException("Cannot write to chat if user is not in room");
        }
        chatEntries.add(new ChatEntry(email, message));
    }

    public boolean isStarted() {
        return play.isPresent();
    }

    public boolean join(User user) throws ClientRoomJoinException {
        if (participants.size() >= MAX_PLAYERS) {
            throw new ClientRoomJoinException(ClientRoomJoinException.ROOM_FULL);
        }
        if (isUserInRoom(user)) {
            throw new ClientRoomJoinException(ClientRoomJoinException.PLAYER_ALREADY_JOINED);
        }
        scores.put(user.getEmail(), 0);
        // TODO exception handling
        return participants.add(user);
    }

    synchronized public boolean leave(User user) throws ClientRoomLeaveException {
        if (!isUserInRoom(user)) {
            throw new ClientRoomLeaveException(ClientRoomLeaveException.USER_NOT_IN_ROOM);
        }
        scores.remove(user.getEmail());
        return participants.remove(user);
    }

    synchronized public void start() throws PlayException{
        if (participants.size() != MAX_PLAYERS) {
            throw new PlayException(String.format("Must be %s players to start a game", MAX_PLAYERS));
        }
        play = Optional.of(new Play());
    }

    public Optional<User> getParticipantByEmail(String email) {
        return participants.stream()
                .filter(participant -> participant.getEmail().equals(email))
                .findFirst();
    }

    public boolean isUserInRoomByEmail(String email) {
        return participants.stream().map(User::getEmail).anyMatch(email::equals);
    }

    public boolean isUserInRoom(User user) {
        return participants.stream()
                .anyMatch(participant -> participant.getUserID().equals(user.getUserID()));
    }

    public boolean isMaxParticipants() {
        return participants.size() == MAX_PLAYERS;
    }

    public boolean isUserOwner(User user) {
        return isUserIdOwner(user.getUserID());
    }

    public boolean isUserIdOwner(String userId) {
        return owner.getUserID().equals(userId);
    }

    public List<Card> getCardsOnTable() {
        return play.map(Play::getCardsOnTable).orElse(new ArrayList<>());
    }
}
