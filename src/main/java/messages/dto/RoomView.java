package messages.dto;

import lombok.Builder;
import lombok.Data;
import utils.User;
import utils.model.Room;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class RoomView implements Serializable {
    private final int roomId;
    private final int numberOfParticipants;
    private final String roomCreatorEmail;
    private final boolean isUserInRoom;
    private final boolean isUserCreator;

    public static RoomView fromRoom(Room room, String loggedInUserId) {
        return RoomView.builder()
                .isUserCreator(isUserCreator(room, loggedInUserId))
                .isUserInRoom(isUserInRoom(room, loggedInUserId))
                .roomId(room.getId())
                .numberOfParticipants(room.getParticipants().size())
                .roomCreatorEmail(room.getOwner().getEmail())
                .build();
    }

    private static boolean isUserCreator(Room room, String loggedInUserId) {
        return room.getOwner().getUserID().equals(loggedInUserId);
    }

    private static boolean isUserInRoom(Room room, String loggedInUserId) {
        final List<String> participantIds = room.getParticipants().stream()
                .map(User::getUserID)
                .collect(Collectors.toList());
        return participantIds.contains(loggedInUserId);
    }
}
