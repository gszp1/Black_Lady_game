package utils;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class User {

    private final String userID;

    private final String email;

    private final String username;

    private final ObjectOutputStream outputStream;

    private final Socket socket;

    public User(String userID, String email, String username, Socket socket) throws IOException {
        this.userID = userID;
        this.email = email;
        this.username = username;
        this.socket = socket;
        outputStream = new ObjectOutputStream(socket.getOutputStream());
    }

    public String getUserID() {
        return userID;
    }

    public String getEmail() {
        return email;
    }

    public Socket getSocket() {
        return socket;
    }

    public ObjectOutputStream getOutputStream() {
        return outputStream;
    }
}
