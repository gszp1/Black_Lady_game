package messages.dto;

import cards.Card;
import lombok.Builder;
import lombok.Data;
import utils.model.Play;
import utils.model.Room;
import utils.model.UserDeck;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Builder
public class TrickDetails {
    private List<Card> cards;

    public static TrickDetails fromRoom(Room room) {
        Optional<Play> play = room.getPlay();
        if (!play.isPresent()) {
            return TrickDetails.builder().cards(new ArrayList<>()).build();
        }
        List<Card> cards = play.get().getCardsPut().values().stream()
                .map(UserDeck::getLast)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        return TrickDetails.builder()
                .cards(cards)
                .build();
    }
}
