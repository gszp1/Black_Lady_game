package messages;

public enum MessageType {
    LoginRequest("LoginRequest"),
    LoginResponse("LoginResponse"),
    RegisterRequest("RegisterRequest"),
    RegisterResponse("RegisterResponse");

    private final String typeName;

    MessageType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }
}
