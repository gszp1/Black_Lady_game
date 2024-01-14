package server;

import cards.Deck;
import utils.User;

import java.util.ArrayList;

public class GameRoom {

    public final int MAX_USERS = 4;

    private static int gameRoomsCounter = 0;

    private int round = 1;

    private int dealerID;
    private final String gameRoomID;

    private final Deck cardDeck;

    private final ArrayList<User> users;

    public GameRoom() {
        users = new ArrayList<>();
        cardDeck = new Deck();
        gameRoomID = Integer.toString(gameRoomsCounter + 1);
        ++gameRoomsCounter;
    }

    public boolean addUser(User user) {
        if (users.size() < 4) {
            return users.add(user);
        }
        return false;
    }

    private boolean removeUser(User user) {
        return users.remove(user);
    }

    private void incrementRound() {
        if (round < 7) {
            ++round;
        }
    }
}
