package server;

import exceptions.ServerSocketConnectionException;
import messages.toServer.ToServerMessage;
import org.xml.sax.SAXException;
import utils.GameDetails;
import utils.User;
import utils.UserList;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * Class for game server, communicates with users and database.
 */
public class GameServer {

    /**
     * Server port.
     */
    private final int PORT = 8080;

    /**
     * Queue to store messages to handle.
     */
    private final ConcurrentLinkedQueue<ToServerMessage> inputQueue;

    /**
     * List of users.
     */
    private final UserList userList;

    /**
     * Message processor.
     */
    private MessageProcessor messageProcessor;

    /**
     * Database connector.
     */
    private DatabaseConnector databaseConnector;

    /**
     * Details of game server.
     */
    private GameDetails gameDetails;

    /**
     * Map for user handlers.
     */
    private Map<String, InputHandler> userHandlersMap = new ConcurrentHashMap<>();

    /**
     * Constructor.
     */
    public GameServer() {
        inputQueue = new ConcurrentLinkedQueue<>();
        userList = new UserList();
        gameDetails = new GameDetails();
    }

    /**
     * Adds user to users list.
     * @param user New user.
     * @throws ServerSocketConnectionException Thrown when a connection error occurred.
     */
    public void addUser(User user) throws ServerSocketConnectionException {
        userList.addUser(user);
        InputHandler inputHandler = new InputHandler(user, inputQueue);
        userHandlersMap.put(user.getConnectionID(), inputHandler);
        inputHandler.start();
    }

    /**
     * Establishes connection with database.
     * Creates DatabaseConnector object.
     */
    public boolean establishDatabaseConnection(String databaseURL) {
        try {
            databaseConnector = new DatabaseConnector(databaseURL);
        } catch (Exception e) {
            System.out.println("S: Failed to establish connection with database. Terminating process.");
            return false;
        }
        return true;
    }

    /**
     * Start point for server.
     * Creates ServerSocket in order to listen to incoming client connections.
     * @param args - command line arguments.
     */
    public static void main(String [] args) throws ParserConfigurationException, IOException, SAXException {
        if (args.length < 1) {
            System.out.println("No database url given.");
            return;
        }
        GameServer gameServer = new GameServer();
        if (!gameServer.establishDatabaseConnection(args[0])) {
            return;
        }
        gameServer.startMessageProcessorThread();
        try (ServerSocket serverSocket = new ServerSocket(gameServer.PORT)) {
            System.out.println("S: Server is running. Listening for new connections.");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("S: New connection established with client: "
                        + clientSocket.getInetAddress()
                        + ","
                        + clientSocket.getPort()
                );
                String connectionID = String.format("%s|%s", clientSocket.getInetAddress(), clientSocket.getPort());
                User user = new User(connectionID, null, null, null, clientSocket);
                gameServer.addUser(user);
            }
        } catch (IOException e) {
            System.out.println("S: Failed to run server. Terminating process.");
            throw new RuntimeException(e);
        } catch (ServerSocketConnectionException e) {
            System.out.println("S: Failed to add user");
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets up and runs message processor thread.
     * Requires inputQueue, userList and databaseConnector being initialized.
     */
    public void startMessageProcessorThread() {
        messageProcessor = new MessageProcessor(inputQueue, userList, databaseConnector, gameDetails);
        messageProcessor.start();
    }

    /**
     * Method for user removal.
     * @param email - Email of user to be removed.
     * @return - true if user was successfully removed, otherwise false
     * @throws SQLException - Exception for error during query lifetime.
     */
    public boolean removeUser(String email) throws SQLException {
        return databaseConnector.removeUserFromDatabase(email) == 1;
    }

}

