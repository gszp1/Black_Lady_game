package utils.score;

import cards.Card;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Util functions for plays.
 */
public class PlayUtils {

    /**
     * Gets player who takes trick.
     * @param cardsOnTable Cards put on table.
     * @param firstCard first card of trick.
     * @return UserID.
     */
    public static String getTrickPicker(Map<String, Card> cardsOnTable, Card firstCard) {
        final Card winningCard = getWinningCard(cardsOnTable, firstCard);
        final Map<Card, String> cardToUserIdsMap = reverseMap(cardsOnTable);
        return cardToUserIdsMap.get(winningCard);
    }

    /**
     * Gets winning card from trick.
     * @param cardsOnTable Cards on table.
     * @param firstCard First card of trick.
     * @return Winning card.
     */
    private static Card getWinningCard(Map<String, Card> cardsOnTable, Card firstCard) {
        return cardsOnTable.values().stream()
                .filter(card -> card.getCardSet().equals(firstCard.getCardSet()))
                .reduce(firstCard, (strongestCard, nextCard) -> {
                    final int currentMaxRank = strongestCard.getCardType().getRank();
                    if (nextCard.getCardType().getRank() > currentMaxRank) {
                        return nextCard;
                    }
                    return strongestCard;
                });
    }

    /**
     * Swaps keys and values in map.
     * @param cardsOnTable Cards on table.
     * @return Swapped map.
     */
    private static Map<Card, String> reverseMap(Map<String, Card> cardsOnTable) {
        return cardsOnTable.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    }
}
