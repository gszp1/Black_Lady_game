package client;

import messages.toClient.ToClientMessage;
import messages.toServer.ToServerMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.function.Consumer;

import exceptions.ClientSocketConnectionException;

/**
 * Class for handling connection with server on client side.
 */
public class ServerConnector extends Thread {

    /**
     * Game server's port.
     */
    private final int SERVER_PORT = 8080;

    /**
     * Game server's IP address.
     */
    private final String SERVER_IP = "0.0.0.0";

    /**
     * Client-server connection socket.
     */
    private final Socket socket;

    /**
     * Reference to application main class.
     */
    private final GameClient gameClient;

    /**
     *  Output stream for sending messages.
     */
    private final ObjectOutputStream outputStream;

    /**
     * Input stream for receiving messages.
     */
    private final ObjectInputStream inputStream;

    /**
     * Handler for incoming messages.
     */
    private Consumer<ToClientMessage> messageHandler = (message) -> {
        System.out.println("Handler not defined!");
    };

    /**
     * Constructor for ServerConnector, opens connection with server, opens output and input streams.
     * @throws ClientSocketConnectionException Exception thrown upon connection error.
     */
    public ServerConnector(GameClient gameClient) throws ClientSocketConnectionException {
        String possibleErrorCause = ClientSocketConnectionException.SOCKET_CONNECTION_FAILURE;
        this.gameClient = gameClient;
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
     * @param message ToServerMessage to be sent to server.
     * @throws ClientSocketConnectionException Exception thrown upon connection error.
     */
    public void sendMessage(ToServerMessage message) throws ClientSocketConnectionException {
        try {
            outputStream.writeObject(message);
        } catch (IOException e) {
            throw new ClientSocketConnectionException(ClientSocketConnectionException.MESSAGE_SENDING_FAILURE);
        }
    }

    /**
     * Method for reading message from server.
     * @return message ToServerMessage read from server output.
     * @throws ClientSocketConnectionException Exception thrown upon connection error.
     */
    private ToClientMessage readMessage() throws ClientSocketConnectionException {
        try {
            return (ToClientMessage) inputStream.readObject();
        } catch (IOException e) {
            throw new ClientSocketConnectionException(ClientSocketConnectionException.MESSAGE_READING_FAILURE);
        } catch (ClassNotFoundException e) {
            throw new ClientSocketConnectionException(ClientSocketConnectionException.UNKNOWN_MESSAGE_TYPE);
        }
    }

    /**
     * Setter for message handler.
     * @param messageHandler Message handler implementing Consumer functional interface.
     */
    public void setMessageHandler(Consumer<ToClientMessage> messageHandler) {
        this.messageHandler = messageHandler;
    }

    /**
     * Thread run method.
     */
    @Override
    public void run() {
        try {
            while (!interrupted()) {
                ToClientMessage message = readMessage();
                System.out.println(message.getMessageType());
                this.messageHandler.accept(message);
            }
        } catch (ClientSocketConnectionException e) {
            System.out.println(e.getErrorCause());
        }
    }
}
