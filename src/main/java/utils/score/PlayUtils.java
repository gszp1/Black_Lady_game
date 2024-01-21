package utils.score;

import cards.Card;

import java.util.Map;
import java.util.stream.Collectors;

public class PlayUtils {

    public static String getTrickPicker(Map<String, Card> cardsOnTable, Card firstCard) {
        final Card winningCard = getWinningCard(cardsOnTable, firstCard);
        final Map<Card, String> cardToUserIdsMap = reverseMap(cardsOnTable);
        return cardToUserIdsMap.get(winningCard);
    }

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

    private static Map<Card, String> reverseMap(Map<String, Card> cardsOnTable) {
        return cardsOnTable.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    }
}
