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
    LogoutResponse("LogoutResponse"),
    InviteUserRequest("InviteUserRequest"),
    InviteUserResponse("InviteUserResponse"),
    GameDetailsRequest("GameDetailsRequest"),
    GameDetailsResponse("GameDetailsResponse"),
    CreateGameRequest("CreateRoomRequest"),
    CreateGameResponse("CreateGameResponse"),
    DeleteRoomRequest("DeleteRoomRequest"),
    DeleteRoomResponse("DeleteRoomResponse"),
    JoinRoomRequest("JoinRoomRequest"),
    JoinRoomResponse("JoinRoomResponse"),
    RoomInviteToServerRequest("UserInviteToServerRequest"),
    RoomInviteToReceiverRequest("RoomInviteToClientRequest"),
    RoomInviteAcceptRequest("RoomInviteAcceptRequest"),
    RoomInviteDenyRequest("RoomInviteDenyRequest"),
    RoomInviteSenderResponse("RoomInviteDenySenderResponse"),
    LeaveRoomRequest("LeaveRoomRequest"),
    LeaveRoomResponse("LeaveRoomResponse"),
    RoomDetailsRequest("RoomDetailsRequest"),
    RoomDetailsResponse("RoomDetailsResponse"),
    WriteChatRequest("WriteChatRequest"),
    WriteChatResponse("WriteChatResponse"),
    StartGameRequest("StartGameRequest"),
    StartGameResponse("StartGameResponse"),
    PlayCardRequest("PlayCardRequest"),
    PlayCardResponse("PlayCardResponse"),
    SkipGameRequest("SkipGameRequest");

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
