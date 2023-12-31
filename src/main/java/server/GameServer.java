package server;

import exceptions.LoginFailureException;
import exceptions.RegistrationFailureException;
import messages.Message;
import org.apache.commons.codec.digest.DigestUtils;
import utils.UserList;
import utils.Utils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;


/**
 * Class for game server, communicates with users and database.
 */
public class GameServer {

    private final int PORT = 8080;

    private final ConcurrentLinkedQueue<Message> inputQueue;

    private final UserList userList;

    private DatabaseConnector databaseConnector;

    public GameServer() {
        inputQueue = new ConcurrentLinkedQueue<>();
        userList = new UserList();
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
    public static void main(String [] args) {
        if (args.length < 1) {
            System.out.println("No database url given.");
            return;
        }
        GameServer gameServer = new GameServer();
        if (!gameServer.establishDatabaseConnection(args[0])) {
            return;
        }
        try (ServerSocket serverSocket = new ServerSocket(gameServer.PORT)) {
            System.out.println("S: Server is running. Listening for new connections.");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("S: New connection established with client: "
                        + clientSocket.getInetAddress()
                        +
                        "," + clientSocket.getPort()
                );
            }
        } catch (IOException e) {
            System.out.println("S: Failed to run server. Terminating process.");
        }
    }



    /**
     * Method for user registration. Sends query to database with new user credentials.
     * @param email - New user's email - must be unique for all records.
     * @param username - New user's username, must be unique for all records;
     * @param password - New user's raw password, hashed with md5 before being sent.
     * @param passwordConfirmation - password confirmation. Must be equal to password.
     * @return - boolean representing registration result.
     * @throws RegistrationFailureException - Exception with reason of registration failure.
     */
    public boolean registerUser(String email, String username, String password, String passwordConfirmation)
            throws RegistrationFailureException {
        Optional<String> validationResult = validateRegistrationCredentials(email, username,
                password, passwordConfirmation);
        if(validationResult.isPresent()) {
            throw(new RegistrationFailureException(validationResult.get()));
        }
        String hashPassword = DigestUtils.md5Hex(password).toUpperCase();
        int insertedRecords = 0;
        try {
            insertedRecords = databaseConnector.insertUserIntoDatabase(email, username, hashPassword);
        } catch (SQLException e) {
            throw(new RegistrationFailureException(RegistrationFailureException.USER_EXISTS));
        }
        return insertedRecords == 1;
    }

    /**
     * Method for user login. Checks if such user exists in database and if given credentials are valid.
     * @param email - User's email.
     * @param password - User's email. Hashed with md5 encryption for comparison.
     * @return - boolean representing login result.
     * @throws LoginFailureException - Exception representing login failure.
     * @throws SQLException - Exception for error during query lifetime.
     */
    public boolean loginUser(String email, String password) throws LoginFailureException, SQLException {
        Optional<String> validationResult = validateLoginCredentials(email, password);
        if(validationResult.isPresent()) {
            throw(new LoginFailureException(validationResult.get()));
        }
        String hashPassword = DigestUtils.md5Hex(password).toUpperCase();
        ArrayList<String> queryResult = databaseConnector.getUserFromDatabase(email);
        if (queryResult.isEmpty()) {
            throw (new LoginFailureException(LoginFailureException.INVALID_CREDENTIALS));
        }
        if (!queryResult.get(3).equals(hashPassword)) {
            throw (new LoginFailureException(LoginFailureException.INVALID_CREDENTIALS));
        }
        return true;
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


    /**
     * Validates provided login credentials.
     * @param email - User's email.
     * @param password - User's password.
     * @return - Returns empty Optional if credentials are valid, otherwise Optional contains information why credentials are not valid.
     */
    public Optional<String> validateLoginCredentials(String email, String password) {
        if(email.isEmpty() || password.isEmpty()) {
            return Optional.of(LoginFailureException.EMPTY_FIELDS);
        }
        Matcher matcher = Utils.pattern.matcher(email);
        if(!matcher.matches()) {
            return Optional.of(LoginFailureException.INCORRECT_EMAIL);
        }
        return Optional.empty();
    }

    /**
     * Validates provided registration credentials.
     * @param email - New user's email - must be unique for all records.
     * @param username - New user's username, must be unique for all records;
     * @param password - New user's password.
     * @param passwordConfirmation - password confirmation. Must be equal to password.
     * @return - Returns empty Optional if credentials are valid, otherwise Optional contains information why credentials are not valid.
     */
    public Optional<String> validateRegistrationCredentials(String email, String username,
                                                             String password, String passwordConfirmation) {
        if(username.isEmpty() || email.isEmpty() || password.isEmpty() || passwordConfirmation.isEmpty()) {
            return Optional.of(RegistrationFailureException.EMPTY_FIELDS);
        }
        if(!passwordConfirmation.equals(password)) {
            return Optional.of(RegistrationFailureException.PASSWORDS_NOT_EQUAL);
        }
        Matcher matcher = Utils.pattern.matcher(email);
        if(!matcher.matches()) {
            return Optional.of(RegistrationFailureException.INCORRECT_EMAIL);
        }

        return Optional.empty();
    }

}

