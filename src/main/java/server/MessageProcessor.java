package server;

import exceptions.ServerSocketConnectionException;
import messages.Message;
import utils.UserList;

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

    /**
     * Constructor, sets inputQueue and userList with given reference.
     * @param inputQueue - Reference to inputQueue.
     * @param userList - Reference to userList.
     */
    public MessageProcessor(ConcurrentLinkedQueue<Message> inputQueue, UserList userList) {
        this.inputQueue = inputQueue;
        this.userList = userList;
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
                    message.handleMessage();
                }
            }
        } catch (IOException e) {
            System.out.println(ServerSocketConnectionException.MESSAGE_SENDING_FAILURE);
        }
    }


}
