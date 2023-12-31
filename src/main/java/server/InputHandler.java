package server;

import exceptions.ServerSocketConnectionException;
import messages.Message;
import utils.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.ConcurrentLinkedQueue;

public class InputHandler extends Thread{

    private final User user;

    private final ObjectInputStream inputStream;

    private final ConcurrentLinkedQueue<Message> inputQueue;

    public InputHandler(User user, ConcurrentLinkedQueue<Message> inputQueue) throws ServerSocketConnectionException {
        this.user = user;
        this.inputQueue = inputQueue;
        try {
            inputStream = new ObjectInputStream(user.getSocket().getInputStream());
        } catch (IOException e) {
            throw new ServerSocketConnectionException(ServerSocketConnectionException.INPUT_STREAM_OPENING_FAILURE);
        }
    }

    @Override
    public void run() {
        try {
            while (!interrupted()) {
                Message message = (Message) inputStream.readObject();
                message.setClientID(user.getUserID());
                inputQueue.add(message);
            }
        } catch (ClassNotFoundException e) {
            System.out.println(ServerSocketConnectionException.UNKNOWN_MESSAGE_TYPE);
        } catch (IOException e) {
            System.out.println(ServerSocketConnectionException.MESSAGE_READING_FAILURE);
        }
    }






    
}
