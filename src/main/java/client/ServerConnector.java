package client;

import messages.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import exceptions.socketConnectionException;

public class ServerConnector {

    private final int SERVER_PORT = 8080;

    private final String SERVER_IP = "0.0.0.0";

    private final Socket socket;

    private final ObjectOutputStream outputStream;

    public ServerConnector() throws socketConnectionException {
        String possibleErrorCause = socketConnectionException.STREAM_OPENING_FAILURE;
        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            possibleErrorCause = socketConnectionException.STREAM_OPENING_FAILURE;
            outputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new socketConnectionException(possibleErrorCause);
        }
    }

    public void sendMessage(Message message) throws socketConnectionException {
        try {
            outputStream.writeObject(message);
        } catch (IOException e) {
            throw new socketConnectionException(socketConnectionException.MESSAGE_SENDING_FAILURE);
        }
    }

}
