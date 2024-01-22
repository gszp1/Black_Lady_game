package utils;

import lombok.Getter;
import lombok.Setter;
import utils.model.UserData;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Class containing all data required for handling clients who connect with our service.
 */
@Getter
@Setter
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
     * @param connectionID - user's connectionID.
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
     * Updates user's data with information from userData.
     * @param userData User's data.
     */
    public void updateUserData(UserData userData) {
        setUserID(userData.getUserId());
        setUsername(userData.getUsername());
        setEmail(userData.getEmail());
    }

    /**
     * Closes connection sockets.
     */
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
