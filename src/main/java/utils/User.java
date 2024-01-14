package utils;

import utils.model.UserData;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Class containing all data required for handling clients who connect with our service.
 */

public class User {

    /**
     * ID of a user - Taken from database, unique.
     */
    private String userID;

    /**
     * ID of connection - IP + port.
     */
    private String connectionID;

    /**
     * Email of a user - Taken from database, unique.
     */
    private String email;

    /**
     * User's username - Taken from database, unique.
     */
    private String username;

    /**
     * OutputStream used for sending messages to user's client app.
     */
    private final ObjectOutputStream outputStream;

    /**
     * Reference to socket created upon establishing connection.
     */
    private final Socket socket;

    /**
     * Constructor, fills are needed class fields, wraps socket's output stream with ObjectOutputStream.
     * @param userID - user's ID.
     * @param email - user's Email.
     * @param username - user's username.
     * @param socket - Server side socket, to which client's app socket is connected.
     * @throws IOException - Input Output Exception thrown when it's impossible to open output stream.
     */
    public User(String connectionID, String userID, String email, String username, Socket socket) throws IOException {
        this.connectionID = connectionID;
        this.userID = userID;
        this.email = email;
        this.username = username;
        this.socket = socket;
        outputStream = new ObjectOutputStream(socket.getOutputStream());
    }

    /**
     * Updates userID of user to whom this message was sent.
     * @param userData userData to set.
     * @return true if update was successful, false if not.
     */
    public void updateUserData(UserData userData) {
        setUserID(userData.getUserId());
        setUsername(userData.getUsername());
        setEmail(userData.getEmail());
    }

    /**
     * Getter for userID.
     * @return - user's ID.
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Getter for user's email.
     * @return - user's email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Getter for user's username.
     * @return - user's username.
     */
    public String getUsername() {
        return username;
    }

    public String getConnectionID() {
        return connectionID;
    }

    /**
     * Getter for server side socket handling connection with client.
     * @return - socket to which client is connected.
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * Getter for ObjectOutputStream wrapping socket OutputStream.
     * @return - reference for socket's ObjectOutputStream.
     */
    public ObjectOutputStream getOutputStream() {
        return outputStream;
    }

    /**
     * Setter for userID.
     * @param userID - New userID.
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * Setter for email.
     * @param email - New email.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Setter for username;
     * @param username - New username;
     */
    public void setUsername(String username) {
        this.username = username;
    }

    public void close() {
        try {
            if (outputStream != null) {
                outputStream.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            System.out.println("Error during socket closing.");
        }
    }
}
