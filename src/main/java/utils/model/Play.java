package utils.model;

import cards.Card;
import cards.Deck;
import exceptions.PlayException;
import lombok.Getter;
import utils.User;
import utils.Utils;
import utils.score.PlayUtils;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class Play {

    private final List<String> userIds;

    private final Map<String, UserDeck> cardsInHand;

    @Getter
    private final Map<String, UserDeck> cardsPut = new HashMap<>();

    private final List<Card> firstCards = new LinkedList<>();

    private final Map<String, Card> cardsOnTable = new HashMap<>();

    private final Map<String, Card> lastTrick = new HashMap<>();

    private final Map<String, String> userIdsToEmails;

    private String playingUserId;

    public Play(List<User> users) throws PlayException {
        final List<String> userIds = users.stream().map(User::getUserID).collect(Collectors.toList());
        if (userIds.size() != Room.MAX_PLAYERS) {
            throw new PlayException("Creation of game resulted in not enough players!");
        }
        this.userIds = userIds;
        this.cardsInHand = getInitialDecks();
        this.playingUserId = userIds.get(0);
        this.userIdsToEmails = users.stream().collect(Collectors.toMap(User::getUserID, User::getEmail));
        for (String userId: userIds) {
            cardsPut.put(userId, new UserDeck());
        }
    }

    private Map<String, UserDeck> getInitialDecks() {
        final List<Card> cards = Utils.getFullDeck();
        Collections.shuffle(cards);

        final int cardsPerPlayer = cards.size() / Room.MAX_PLAYERS;
        final Map<String, UserDeck> usersCards = new HashMap<>();
        for (int i = 0; i < Room.MAX_PLAYERS; i++) {
            int start = i * cardsPerPlayer;
            int end = (i + 1) * cardsPerPlayer;
            usersCards.put(
              userIds.get(i),
              new UserDeck(new ArrayList<>(cards.subList(start, end)))
            );
        }
        return usersCards;
    }

    synchronized public boolean playCard(String userId, Card card) {
        boolean cardRemovalResult = cardsInHand.get(userId).getCards().remove(card);
        if (!cardRemovalResult){
            System.out.printf("Card was not remove %s\n", card);
            return false;
        }
        System.out.println(cardsInHand.get(userId).getCards().size());
        if (!cardsPut.containsKey(userId)) {
            cardsPut.put(userId, new UserDeck(new ArrayList<>()));
        }
        if (cardsOnTable.isEmpty()) {
            firstCards.add(card);
        }
        cardsOnTable.put(userId, card);
        if (cardsOnTable.size() == Room.MAX_PLAYERS) {
            playingUserId = PlayUtils.getTrickPicker(cardsOnTable, firstCards.get(firstCards.size() - 1));
            cardsOnTable.forEach((playerUserId, playerCard) -> {
                cardsPut.get(playerUserId).addCard(playerCard);
                lastTrick.put(playerUserId, playerCard);
            });
            cardsOnTable.clear();
        } else {
            playingUserId = getNextPlayerUserId();
        }
        return true;
    }

    public boolean hasRoundFinished() {
        return cardsInHand.values().stream()
                .map(UserDeck::getCards)
                .map(List::size)
                .allMatch((cardsCount) -> cardsCount == 0);
    }

    synchronized public List<Card> getCards(String userId) {
        return new ArrayList<>(cardsInHand.get(userId).getCards());
    }

    private String getNextPlayerUserId() {
        final int nextPlayerId = (userIds.indexOf(playingUserId) + 1) % userIds.size();
        return userIds.get(nextPlayerId);
    }

    synchronized public Map<String, Card> getCardsOnTable() {
        return new HashMap<>(cardsOnTable);
    }

    public Map<String, String> getUserIdsToEmailsMapping() {
        return userIdsToEmails;
    }

    public List<String> getPlayersOrder() {
        return userIds;
    }

    public Card getFirstCardOnTable() {
        if (cardsOnTable.isEmpty()) {
            return null;
        }
        return firstCards.get(firstCards.size() - 1);
    }

    public String getCurrentPlayingUserId() {
        return playingUserId;
    }

    public Map<String, Card> getLastTrick() {
        return new HashMap<>(lastTrick);
    }
}
