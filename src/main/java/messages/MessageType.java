package messages;

/**
 * Enumerate for all types of messages in server-client connection.
 */
public enum MessageType {
    LoginRequest("LoginRequest"),
    LoginResponse("LoginResponse"),
    RegisterRequest("RegisterRequest"),
    RegisterResponse("RegisterResponse"),
    LogoutRequest("LogoutRequest"),
    LogoutResponse("LogoutResponse");

    /**
     * String depicting message type.
     */
    private final String typeName;

    /**
     * Constructor for enumerate. Sets the typeName field.
     * @param typeName - String depicting message type.
     */
    MessageType(String typeName) {
        this.typeName = typeName;
    }

    /**
     * Getter for message type name.
     * @return - String depicting message type.
     */
    public String getTypeName() {
        return typeName;
    }
}
