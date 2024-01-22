package utils.model;

/**
 * Class for storing user information.
 */
public class UserData {

    /**
     * User's ID.
     */
    private String userId;

    /**
     * User's email.
     */
    private String email;

    /**
     * User's username.
     */
    private String username;

    /**
     * User's password.
     */
    private String password;

    /**
     * Constructor, sets user data.
     * @param userId User's ID.
     * @param email User's email.
     * @param username User's username.
     * @param password User's password.
     */
    public UserData(String userId, String email, String username, String password) {
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    /**
     * Getter for userID.
     * @return User's ID.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Setter for userID.
     * @param userId User's new ID.
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Getter for user's email.
     * @return User's email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter for user's email.
     * @param email User's new email.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter for user's username.
     * @return User's username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter for user's username.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter for user's password.
     * @return User's password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter for user's password.
     * @param password user's password.
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
