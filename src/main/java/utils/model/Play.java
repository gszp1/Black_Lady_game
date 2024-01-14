package utils.model;

import cards.Card;

import java.util.HashMap;
import java.util.Map;

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
}
