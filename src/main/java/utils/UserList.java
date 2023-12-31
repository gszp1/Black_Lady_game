package utils;

import java.util.ArrayList;
import java.util.Optional;

public class UserList {

    private final ArrayList<User> users;

    public UserList() {
        users = new ArrayList<>();
    }

    synchronized public void addUser(User user) {
        users.add(user);
    }

    synchronized public boolean removeUser(User user) {
        return users.remove(user);
    }

    synchronized public Optional<User> getUser(String userID) {
        return users.stream().filter(user -> user.getUserID().equals(userID)).findFirst();
    }

    public ArrayList<User> getUsers() {
        return users;
    }

}
