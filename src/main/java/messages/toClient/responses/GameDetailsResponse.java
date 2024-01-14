package messages.toClient.responses;

import lombok.Getter;
import messages.MessageType;
import messages.dto.GameView;
import messages.toClient.ToClientMessage;

import java.io.IOException;
import java.util.Objects;

@Getter
public class GameDetailsResponse extends ToClientMessage {

    private final GameView gameView;

    public GameDetailsResponse(GameView gameView) {
        super(MessageType.GameDetailsResponse, gameView.toString());
        this.gameView = gameView;
    }

    @Override
    public boolean handle() throws IOException {
        return false;
    }
}
