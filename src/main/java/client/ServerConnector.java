package client;

import messages.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerConnector {

    private final int SERVER_PORT = 8080;

    private final String SERVER_IP = "0.0.0.0";

    private final Socket socket;

    private final ObjectOutputStream outputStream;

    public ServerConnector() throws IOException {
        socket = new Socket(SERVER_IP, SERVER_PORT);
        outputStream = new ObjectOutputStream(socket.getOutputStream());
    }

    public void sendMessage(Message message) throws IOException {
        outputStream.writeObject(message);
    }

}
