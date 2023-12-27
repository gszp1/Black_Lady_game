package server;

import java.sql.*;
import java.util.ArrayList;

/**
 * Class which provides interface for connection between server and MySQL database.
 */

public class DatabaseConnector {

    private final String DB_USERNAME = "root";

    private final String DB_PASSWORD = "root";

    private Connection connection;

    private final String DB_SERVICE_NAME = "database_service";

    private final PreparedStatement selectStatement;

    private final PreparedStatement insertStatement;

    /**
     * Constructor, establishes connection with MySQL database with ip: 127.0.0.1 and port 3306, called Users_database.
     * Creates prepared statements.
     * @throws Exception - Cumulative exception, all exceptions are handled as connection error.
     */
    DatabaseConnector() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        connection = DriverManager.getConnection(String.format("jdbc:mysql://%s:3306/Users_database", DB_SERVICE_NAME),
                DB_USERNAME,
                DB_PASSWORD
        );
        if (!connection.isValid(5)) {
            throw new Exception();
        }
        selectStatement = connection.prepareStatement("SELECT * FROM users WHERE email = ?");
        insertStatement = connection.prepareStatement("INSERT INTO users (email, username, password) VALUES (?, ?, ?) ");
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
}
