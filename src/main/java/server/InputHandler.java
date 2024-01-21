package server;

import exceptions.ServerSocketConnectionException;
import messages.toServer.ToServerMessage;
import utils.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Class for handling input from single user.
 */
public class InputHandler extends Thread{

    /**
     * Reference to user object containing data of user to whom this socket is connected to.
     */
    private final User user;

    /**
     * Input Stream from which we read messages sent by user.
     */
    private final ObjectInputStream inputStream;

    /**
     * Reference to linked queue for messages to be handled.
     */
    private final ConcurrentLinkedQueue<ToServerMessage> inputQueue;

    /**
     * Constructor, sets reference to user and linked list.
     * @param user - Reference to user object.
     * @param inputQueue - Reference to input queue.
     * @throws ServerSocketConnectionException - Exception thrown upon connection error.
     */
    public InputHandler(
            User user,
            ConcurrentLinkedQueue<ToServerMessage> inputQueue
    ) throws ServerSocketConnectionException {
        this.user = user;
        this.inputQueue = inputQueue;
        try {
            inputStream = new ObjectInputStream(user.getSocket().getInputStream());
        } catch (IOException e) {
            throw new ServerSocketConnectionException(ServerSocketConnectionException.INPUT_STREAM_OPENING_FAILURE);
        }
    }

    /**
     * Run thread method implementation.
     */
    @Override
    public void run() {
        try {
            while (!interrupted()) {
                ToServerMessage message = (ToServerMessage) inputStream.readObject();
                System.out.println(message.getMessageType());
                message.setConnectionId(user.getConnectionID());
                System.out.println(user.getConnectionID());
                inputQueue.add(message);
            }
        } catch (ClassNotFoundException e) {
            System.out.println(ServerSocketConnectionException.UNKNOWN_MESSAGE_TYPE);
        } catch (IOException e) {
            System.out.println(ServerSocketConnectionException.MESSAGE_READING_FAILURE);
        }
    }
}
