package client;

import messages.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import exceptions.SocketConnectionException;

public class ServerConnector {

    private final int SERVER_PORT = 8080;

    private final String SERVER_IP = "0.0.0.0";

    private final Socket socket;

    private final ObjectOutputStream outputStream;

    public ServerConnector() throws SocketConnectionException {
        String possibleErrorCause = SocketConnectionException.STREAM_OPENING_FAILURE;
        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            possibleErrorCause = SocketConnectionException.STREAM_OPENING_FAILURE;
            outputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new SocketConnectionException(possibleErrorCause);
        }
    }

    public void sendMessage(Message message) throws SocketConnectionException {
        try {
            outputStream.writeObject(message);
        } catch (IOException e) {
            throw new SocketConnectionException(SocketConnectionException.MESSAGE_SENDING_FAILURE);
        }
    }

}
