package messages.toClient.responses;

import lombok.Getter;
import messages.MessageType;
import messages.dto.GameView;
import messages.toClient.ToClientMessage;

import java.io.IOException;

/**
 * Response for game details request.
 */
@Getter
public class GameDetailsResponse extends ToClientMessage {
    /**
     *  Game room data.
     */
    private final GameView gameView;

    /**
     * Constructor, sets data and game room data.
     * @param gameView
     */
    public GameDetailsResponse(GameView gameView) {
        super(MessageType.GameDetailsResponse, gameView.toString());
        this.gameView = gameView;
    }

    /**
     * Default handling procedure.
     * @return false;
     * @throws IOException IOException.
     */
    @Override
    public boolean handle() throws IOException {
        return false;
    }
}
