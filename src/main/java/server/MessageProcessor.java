package server;

import exceptions.ServerSocketConnectionException;
import messages.Message;
import utils.UserList;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageProcessor extends Thread{

    private final ConcurrentLinkedQueue<Message> inputQueue;

    private final UserList userList;

    public MessageProcessor(ConcurrentLinkedQueue<Message> inputQueue, UserList userList) {
        this.inputQueue = inputQueue;
        this.userList = userList;
    }

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
