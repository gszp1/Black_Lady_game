package utils;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Multi-thread safe class for storing users.
 */
public class UserList {

    /**
     * Collection of users.
     */
    private final ArrayList<User> users;

    /**
     * Constructor, creates ArrayList for users.
     */
    public UserList() {
        users = new ArrayList<>();
    }

    /**
     * Synchronized method for adding user to list.
     * @param user - User to be added.
     */
    synchronized public void addUser(User user) {
        users.add(user);
    }

    /**
     * Synchronized method for removing user from list.
     * @param user - User to be removed.
     * @return - Boolean stating if operation was successful.
     */
    synchronized public boolean removeUser(User user) {
        return users.remove(user);
    }

    /**
     * Synchronized method for retrieving user from list by his ID.
     * @param userID - ID of user.
     * @return - User.
     */
    synchronized public Optional<User> getUser(String userID) {
        return users.stream().filter(user -> user.getUserID().equals(userID)).findFirst();
    }

    /**
     * Getter for user list.
     * @return - ArrayList of users.
     */
    public ArrayList<User> getUsers() {
        return users;
    }

}
