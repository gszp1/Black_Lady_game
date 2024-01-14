package utils.model;

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

    public Room(User owner) {
        this.owner = owner;
        participants.add(owner);
    }

    public boolean isStarted() {
        return play.isPresent();
    }

    public boolean join(User user) throws ClientRoomJoinException {
        if (participants.size() == MAX_PLAYERS) {
            throw new ClientRoomJoinException(ClientRoomJoinException.ROOM_FULL);
        }
        if (isUserInRoom(user)) {
            throw new ClientRoomJoinException(ClientRoomJoinException.PLAYER_ALREADY_JOINED);
        }
        // TODO exception handling
        return participants.add(user);
    }

    public boolean leave(User user) throws ClientRoomLeaveException {
        if (!isUserInRoom(user)) {
            throw new ClientRoomLeaveException(ClientRoomLeaveException.USER_NOT_IN_ROOM);
        }
        return participants.remove(user);
    }

    public void start() throws PlayException{
        if (participants.size() != MAX_PLAYERS) {
            throw new PlayException(String.format("Must be %s players to start a game", MAX_PLAYERS));
        }
        play = Optional.of(new Play());
    }

    private boolean isUserInRoom(User user) {
        return participants.stream()
                .anyMatch(participant -> participant.getUserID().equals(user.getUserID()));
    }
}
