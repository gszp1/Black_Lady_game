package server;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseConnector {

    private final String DB_USERNAME = "root";

    private final String DB_PASSWORD = "root";

    private Connection connection;

    private final PreparedStatement selectStatement;

    private final PreparedStatement insertStatement;

    DatabaseConnector() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Users_database",
                DB_USERNAME,
                DB_PASSWORD
        );
        if (!connection.isValid(5)) {
            throw new Exception();
        }
        selectStatement = connection.prepareStatement("SELECT * FROM users WHERE email = ?");
        insertStatement = connection.prepareStatement("INSERT INTO users (email, username, password) VALUES (?, ?, ?) ");

    }g

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

    public int insertUserIntoDatabase(String email, String username, String password) throws SQLException {
        insertStatement.setString(1, email);
        insertStatement.setString(2, username);
        insertStatement.setString(3, password);
        return insertStatement.executeUpdate();
    }
}
