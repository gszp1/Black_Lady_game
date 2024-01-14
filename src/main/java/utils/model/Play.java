package utils.model;

import cards.Card;
import cards.Deck;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class Play {

    private Map<String, UserDeck> cardsInHand = new HashMap<>();

    private Map<String, UserDeck> cardsPut = new HashMap<>();

    synchronized public boolean playCard(String userId, Card card) {
        boolean cardRemovalResult = cardsInHand.get(userId).getCards().remove(card);
        if (!cardRemovalResult){
            return false;
        }
        cardsPut.get(userId).addCard(card);
        return true;
    }

    synchronized public List<Card> getCards(String userId) {
        return cardsInHand.get(userId).getCards();
    }

    synchronized public List<Card> getCardsOnTable() {
        List<UserDeck> decks = new ArrayList<>(cardsPut.values());
        int minCardsPutByEachPlayer = decks.stream().map(UserDeck::getCardsCount).reduce(0, Integer::min);
        int maxCardsPutByEachPlayer = decks.stream().map(UserDeck::getCardsCount).reduce(0, Integer::max);
        if (minCardsPutByEachPlayer == maxCardsPutByEachPlayer) {
            return new ArrayList<>();
        }
        List<Card> cardsOnTable = new ArrayList<>();
        for (UserDeck deck : decks) {
            if (deck.getCardsCount() == maxCardsPutByEachPlayer) {
                deck.getLast().ifPresent(cardsOnTable::add);
            }
        }
        return cardsOnTable;
    }
}
