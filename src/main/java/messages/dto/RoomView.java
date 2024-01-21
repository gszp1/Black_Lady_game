package messages.dto;

import lombok.Builder;
import lombok.Data;
import utils.User;
import utils.model.Room;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Data transfer object for information about a room.
 */
@Data
@Builder
public class RoomView implements Serializable {

    /**
     * ID of room.
     */
    private final int roomId;

    /**
     * Number of players in room.
     */
    private final int numberOfParticipants;

    /**
     * Email of user who created room.
     */
    private final String roomCreatorEmail;

    /**
     * Flag stating if user is in, or not in room.
     */
    private final boolean isUserInRoom;

    /**
     * Flag stating is user created this room.
     */
    private final boolean isUserCreator;

    /**
     * Creates RoomView with data stored in Room instance.
     * @param room Room with data used to create RoomView.
     * @param loggedInUserId ID of logged-in user.
     * @return RoomView instance.
     */
    public static RoomView fromRoom(Room room, String loggedInUserId) {
        return RoomView.builder()
                .isUserCreator(isUserCreator(room, loggedInUserId))
                .isUserInRoom(isUserInRoom(room, loggedInUserId))
                .roomId(room.getId())
                .numberOfParticipants(room.getParticipants().size())
                .roomCreatorEmail(room.getOwner().getEmail())
                .build();
    }

    /**
     * Checks if given user is room's creator.
     * @param room Room.
     * @param loggedInUserId ID of checked user.
     * @return Boolean stating if user is a creator of room.
     */
    private static boolean isUserCreator(Room room, String loggedInUserId) {
        return room.getOwner().getUserID().equals(loggedInUserId);
    }

    /**
     * Checks if user is in room.
     * @param room Room in which user is searched for.
     * @param loggedInUserId ID of searched user.
     * @return Boolean stating if user is in room.
     */
    private static boolean isUserInRoom(Room room, String loggedInUserId) {
        final List<String> participantIds = room.getParticipants().stream()
                .map(User::getUserID)
                .collect(Collectors.toList());
        return participantIds.contains(loggedInUserId);
    }
}
