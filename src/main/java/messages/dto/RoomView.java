package messages.dto;

import lombok.Builder;
import lombok.Data;
import utils.User;
import utils.model.Room;

import java.io.Serializable;

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
                .isUserCreator(room.getOwner().getUserID().equals(loggedInUserId))
                .isUserInRoom(room.getParticipants().stream().map(User::getUserID).anyMatch(loggedInUserId::equals))
                .roomId(room.getId())
                .numberOfParticipants(room.getParticipants().size())
                .roomCreatorEmail(room.getOwner().getEmail())
                .build();
    }
}
