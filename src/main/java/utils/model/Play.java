package utils.model;

import cards.Card;
import exceptions.PlayException;
import lombok.Getter;
import utils.User;
import utils.Utils;
import utils.score.PlayUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class for one full game.
 */

@Getter
public class Play {

    /**
     * List of users' ID.
     */
    private final List<String> userIds;

    /**
     * Map relating user's ID with his card in this game.
     */
    private final Map<String, UserDeck> cardsInHand;

    /**
     * Map of cards put on table.
     */
    @Getter
    private final Map<String, UserDeck> cardsPut = new HashMap<>();

    /**
     * First cards in tricks.
     */
    private final List<Card> firstCards = new LinkedList<>();

    /**
     * Map associating users IDs' with cards put on table.
     */
    private final Map<String, Card> cardsOnTable = new HashMap<>();

    /**
     * Cards from last trick.
     */
    private final Map<String, Card> lastTrick = new HashMap<>();

    /**
     * Map associating user's ID's with their emails.
     */
    private final Map<String, String> userIdsToEmails;

    /**
     * ID of player currently making a move.
     */
    private String playingUserId;

    /**
     * Constructor
     * @param users List of users.
     * @throws PlayException Thrown when something went wrong with starting game.
     */
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

    /**
     * Distributes cards between players.
     * @return Map <UserID, Cards>
     */
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

    /**
     * Puts player's card on table.
     * @param userId ID of user.
     * @param card User's card.
     * @return Boolean.
     */
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

    /**
     * Checks if all users used their cards.
     * @return Boolean.
     */
    public boolean hasRoundFinished() {
        return cardsInHand.values().stream()
                .map(UserDeck::getCards)
                .map(List::size)
                .allMatch((cardsCount) -> cardsCount == 0);
    }

    /**
     * Returns copy of cards held by user.
     * @param userId User's ID.
     * @return Copy of cards.
     */
    synchronized public List<Card> getCards(String userId) {
        return new ArrayList<>(cardsInHand.get(userId).getCards());
    }

    /**
     * Returns next player's ID.
     * @return userID.
     */
    private String getNextPlayerUserId() {
        final int nextPlayerId = (userIds.indexOf(playingUserId) + 1) % userIds.size();
        return userIds.get(nextPlayerId);
    }

    /**
     * Returns cards on table.
     * @return Cards on table.
     */
    synchronized public Map<String, Card> getCardsOnTable() {
        return new HashMap<>(cardsOnTable);
    }

    /**
     * Maps users' IDs to their emails
     * @return Map <ID, email>.
     */
    public Map<String, String> getUserIdsToEmailsMapping() {
        return userIdsToEmails;
    }

    /**
     * Returns order of players.
     * @return List of users.
     */
    public List<String> getPlayersOrder() {
        return userIds;
    }

    /**
     * Returns first card that was put on table.
     * @return First card on table.
     */
    public Card getFirstCardOnTable() {
        if (cardsOnTable.isEmpty()) {
            return null;
        }
        return firstCards.get(firstCards.size() - 1);
    }

    /**
     * Returns ID of player how currently makes a move.
     * @return UserId.
     */
    public String getCurrentPlayingUserId() {
        return playingUserId;
    }

    /**
     * Getter for last trick.
     * @return Last trick.
     */
    public Map<String, Card> getLastTrick() {
        return new HashMap<>(lastTrick);
    }
}
