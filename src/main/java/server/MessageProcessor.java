package server;

import exceptions.ServerSocketConnectionException;
import messages.Message;
import utils.UserList;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Class for handling messages.
 */
public class MessageProcessor extends Thread{

    /**
     * Queue with messages to be handled.
     */
    private final ConcurrentLinkedQueue<Message> inputQueue;

    /**
     * UserList containing all users.
     */
    private final UserList userList;

    private final DatabaseConnector databaseConnector;

    /**
     * Constructor, sets inputQueue and userList with given reference.
     * @param inputQueue - Reference to inputQueue.
     * @param userList - Reference to userList.
     */
    public MessageProcessor(ConcurrentLinkedQueue<Message> inputQueue, UserList userList, DatabaseConnector databaseConnector) {
        this.inputQueue = inputQueue;
        this.userList = userList;
        this.databaseConnector = databaseConnector;
    }

    /**
     * Implementation of Thread.run() abstract method.
     */
    @Override
    public void run() {
        try {
            while(!interrupted()) {
                if (!inputQueue.isEmpty()) {
                    Message message = inputQueue.remove();
                    message.handleMessage(userList, databaseConnector);
                }
            }
        } catch (IOException e) {
            System.out.println(ServerSocketConnectionException.MESSAGE_SENDING_FAILURE);
        }
    }


}
