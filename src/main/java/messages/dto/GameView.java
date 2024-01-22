package messages.dto;

import lombok.Builder;
import lombok.Data;
import utils.GameDetails;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Data transfer object for game view.
 */
@Data
@Builder
public class GameView implements Serializable {
    private List<RoomView> rooms;

    /**
     * Creates GameView dto using gameDetails and ID of logged user.
     * @param gameDetails Game details.
     * @param loggedInUserId ID of logged user.
     * @return GameView data transfer object.
     */
    public static GameView fromGameDetails(GameDetails gameDetails, String loggedInUserId) {
        return GameView.builder()
                .rooms(gameDetails.getRooms().stream()
                        .map(room -> RoomView.fromRoom(room, loggedInUserId))
                        .collect(Collectors.toList())
                )
                .build();
    }
}
