package server;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;
import com.mysql.cj.jdbc.Driver;

/**
 * Class which provides interface for connection between server and MySQL database.
 */

public class DatabaseConnector {

    private final String DB_USERNAME = "root";

    private final String DB_PASSWORD = "root";

    private Connection connection;

    private final String dbServiceUrl;

    private final PreparedStatement selectStatement;

    private final PreparedStatement insertStatement;

    private final PreparedStatement deleteStatement;

    /**
     * Constructor, establishes connection with MySQL database with ip: 127.0.0.1 and port 3306, called Users_database.
     * Creates prepared statements.
     * @throws Exception - Cumulative exception, all exceptions are handled as connection error.
     */
    DatabaseConnector(String dbServiceUrl) throws Exception {
        this.dbServiceUrl = dbServiceUrl;
        Class.forName("com.mysql.cj.jdbc.Driver").newInstance();

        connection = connectToDatabase();
        if (!connection.isValid(30)) {
            throw new Exception();
        }
        selectStatement = connection.prepareStatement("SELECT * FROM users WHERE email = ?");
        insertStatement = connection.prepareStatement("INSERT INTO users (email, username, password) VALUES (?, ?, ?) ");
        deleteStatement = connection.prepareStatement("DELETE FROM users WHERE email = ?");
    }

    private Connection connectToDatabase() {
        int[] backoffs = { 1, 2, 4, 8 };
        Optional<Connection> connection = Optional.empty();
        for (Integer backoff: backoffs) {
            connection = tryConnectToDatabase(false, backoff);
            if (connection.isPresent()){
                return connection.get();
            }
        }
        return tryConnectToDatabase(true, -1).get();
    }

    private Optional<Connection> tryConnectToDatabase(boolean failOnError, int backoff) {
        try {
            return Optional.of(DriverManager.getConnection(
                    dbServiceUrl,
                    DB_USERNAME,
                    DB_PASSWORD
            ));
        } catch (Exception e) {
            if (failOnError) {
                throw new RuntimeException(e);
            }
            System.out.println("Failed to connect to database");
        }
        try {
            System.out.printf("Current connection retry wait time: %s%n", backoff);
            Thread.sleep(backoff * 1000L);
        } catch (InterruptedException e) {
            System.out.println("Failed to connect.");
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    /**
     * Sends SELECT query to database in order to get user given my email argument.
     * @param email - email of searched user.
     * @return - ArrayList of record parameters in forms of strings.
     * @throws SQLException - Thrown when error happens during query lifetime.
     */
    public ArrayList<String> getUserFromDatabase(String email) throws SQLException {
        ArrayList<String> fields = new ArrayList<>();
        selectStatement.setString(1, email);
        ResultSet resultSet = selectStatement.executeQuery();
        selectStatement.clearParameters();
        while (resultSet.next()) {
            fields.add(Integer.toString(resultSet.getInt("UserID")));
            fields.add(resultSet.getString("email"));
            fields.add(resultSet.getString("username"));
            fields.add(resultSet.getString("password"));
        }
        return fields;
    }

    /**
     * Sends INSERT query to database in order to add a new user to users table.
     * @param email - New user's email, must be unique for all records.
     * @param username - New user's username, must be unique for all records.
     * @param password - New user's password hashed with md5 encryption.
     * @return - Number of inserted rows.
     * @throws SQLException - Thrown when error happens during query lifetime.
     */
    public int insertUserIntoDatabase(String email, String username, String password) throws SQLException {
        insertStatement.setString(1, email);
        insertStatement.setString(2, username);
        insertStatement.setString(3, password);
        return insertStatement.executeUpdate();
    }

    /**
     * Sends DELETE query to database in order to remove user with given email.
     * @param email - Email of user to be removed.
     * @return - number of affected records
     * @throws SQLException - Thrown when error happens during query lifetime.
     */
    public int removeUserFromDatabase(String email) throws SQLException {
        deleteStatement.setString(1, email);
        return deleteStatement.executeUpdate();
    }
}
