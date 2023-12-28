package server;

import utils.User;

import java.util.ArrayList;

public class GameRoom {

    private final int MAX_USERS = 4;

    private final ArrayList<User> users;

    public GameRoom() {
        users = new ArrayList<>();
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

}
