package server;

import messages.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.ConcurrentLinkedQueue;

public class InputHandler extends Thread{

    private final String userID;

    private final ObjectInputStream inputStream;

    private final ConcurrentLinkedQueue<Message> inputQueue;

    public InputHandler(String userID, ObjectInputStream inputStream, ConcurrentLinkedQueue<Message> inputQueue) {
        this.userID = userID;
        this.inputStream = inputStream;
        this.inputQueue = inputQueue;
    }

    @Override
    public void run() {
        try {
            while (!interrupted()) {
                Message message = (Message) inputStream.readObject();
                message.setClientID(userID);
                inputQueue.add(message);
            }
        } catch (ClassNotFoundException e) {

        } catch (IOException e) {

        }
    }






    
}
