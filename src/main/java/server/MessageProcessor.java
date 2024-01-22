package server;

import exceptions.ServerSocketConnectionException;
import messages.toServer.ToServerMessage;
import utils.GameDetails;
import utils.UserList;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Class for handling messages.
 */
public class MessageProcessor extends Thread{

    /**
     * Queue with messages to be handled.
     */
    private final ConcurrentLinkedQueue<ToServerMessage> inputQueue;

    /**
     * UserList containing all users.
     */
    private final UserList userList;

    /**
     * DatabaseConnector for retrieving user data from database.
     */
    private final DatabaseConnector databaseConnector;

    /**
     * Server game details.
     */
    private final GameDetails gameDetails;

    /**
     * Constructor, sets inputQueue and userList with given reference.
     * @param inputQueue - Reference to inputQueue.
     * @param userList - Reference to userList.
     * @param databaseConnector - Reference to DatabaseConnector.
     */
    public MessageProcessor(
            ConcurrentLinkedQueue<ToServerMessage> inputQueue,
            UserList userList,
            DatabaseConnector databaseConnector,
            GameDetails gameDetails
    ) {
        this.inputQueue = inputQueue;
        this.userList = userList;
        this.databaseConnector = databaseConnector;
        this.gameDetails = gameDetails;
    }

    /**
     * Implementation of Thread.run() abstract method.
     */
    @Override
    public void run() {
        try {
            while(!interrupted()) {
                if (!inputQueue.isEmpty()) {
                    System.out.println("Handling");
                    ToServerMessage message = inputQueue.remove();
                    System.out.println(message.getMessageType());
                    message.handle(userList, databaseConnector, gameDetails);
                }
            }
        } catch (IOException e) {
            System.out.println(ServerSocketConnectionException.MESSAGE_SENDING_FAILURE);
        } catch (SQLException e) {
            System.out.println("Database connection lost.");
        }
    }
}
