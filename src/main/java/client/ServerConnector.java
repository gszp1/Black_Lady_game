package client;

import messages.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import exceptions.ClientSocketConnectionException;

/**
 * Class for handling connection with server on client side.
 */
public class ServerConnector extends Thread{

    private final int SERVER_PORT = 8080;

    private final String SERVER_IP = "0.0.0.0";

    private final Socket socket;

    private final ObjectOutputStream outputStream;

    private final ObjectInputStream inputStream;

    /**
     * Constructor for ServerConnector, opens connection with server, opens output and input streams.
     * @throws ClientSocketConnectionException - Exception thrown upon connection error.
     */
    public ServerConnector() throws ClientSocketConnectionException {
        String possibleErrorCause = ClientSocketConnectionException.SOCKET_CONNECTION_FAILURE;
        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            possibleErrorCause = ClientSocketConnectionException.OUTPUT_STREAM_OPENING_FAILURE;
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            possibleErrorCause = ClientSocketConnectionException.INPUT_STREAM_OPENING_FAILURE;
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new ClientSocketConnectionException(possibleErrorCause);
        }
    }

    /**
     * Method for sending message to server.
     * @param message - Message to be sent to server.
     * @throws ClientSocketConnectionException - Exception thrown upon connection error.
     */
    public void sendMessage(Message message) throws ClientSocketConnectionException {
        try {
            outputStream.writeObject(message);
        } catch (IOException e) {
            throw new ClientSocketConnectionException(ClientSocketConnectionException.MESSAGE_SENDING_FAILURE);
        }
    }

    /**
     * Method for reading message from server.
     * @return message - Message read from server output.
     * @throws ClientSocketConnectionException - Exception thrown upon connection error.
     */
    private Message readMessage() throws ClientSocketConnectionException {
        Message message = null;
        try {
            message= (Message) inputStream.readObject();
        } catch (IOException e) {
            throw new ClientSocketConnectionException(ClientSocketConnectionException.MESSAGE_READING_FAILURE);
        } catch (ClassNotFoundException e) {
            throw new ClientSocketConnectionException(ClientSocketConnectionException.UNKNOWN_MESSAGE_TYPE);
        }
        return message;
    }

    @Override
    public void run() {
        try {
            while (!interrupted()) {
                Message message = readMessage();
                //todo: message handling
            }
        } catch (ClientSocketConnectionException e) {
            System.out.println(e.getErrorCause());
        }
    }


}
