package utils.model;

import cards.Card;
import exceptions.ClientRoomJoinException;
import exceptions.ClientRoomLeaveException;
import exceptions.PlayException;
import lombok.Getter;
import server.ConfigReader;
import utils.User;
import utils.score.ScoreComputer;

import java.util.*;
import java.util.function.Function;

/**
 * Class for game room.
 */
@Getter
public class Room {

    /**
     * Max number of players.
     */
    public static int MAX_PLAYERS = 4;

    /**
     * Score computers.
     */
    private final List<Function<Play, ScoreComputer>> scoreComputerGetters;

    /**
     * Room's ID.
     */
    private int id;

    /**
     * List of players in room.
     */
    private List<User> participants = new ArrayList<>();

    /**
     * Map of players and their scores.
     */
    private Map<String, Integer> scores = new HashMap<>();

    /**
     * Reference to user who is the owner to this room.
     */
    private User owner;

    /**
     * History of plays.
     */
    private List<Play> playHistory = new ArrayList<>();


    /**
     * Optional for current game.
     */
    private Optional<Play> play = Optional.empty();

    /**
     * Chat entries.
     */
    private List<ChatEntry> chatEntries = new ArrayList<>();

    /**
     * Constructor.
     * @param owner Room's owner.
     * @param id Room's ID.
     */
    public Room(User owner, int id) {
        this.owner = owner;
        this.id = id;
        this.scoreComputerGetters = new ConfigReader().readConfig();
        scores.put(owner.getEmail(), 0);
        participants.add(owner);
    }

    /**
     * Writes message to room chat.
     * @param email Message sender email.
     * @param message Message.
     * @throws PlayException Exception thrown upon failure when writing to chat.
     */
    public void writeToChat(String email, String message) throws PlayException {
        if (!isUserInRoomByEmail(email)) {
            throw new PlayException("Cannot write to chat if user is not in room");
        }
        chatEntries.add(new ChatEntry(email, message));
    }

    /**
     * Checks if game was started.
     * @return Boolean.
     */
    public boolean isStarted() {
        return play.isPresent();
    }

    /**
     * Adds user to room.
     * @param user User.
     * @return Boolean.
     * @throws ClientRoomJoinException Thrown when user can not join room.
     */
    public boolean join(User user) throws ClientRoomJoinException {
        if (participants.size() >= MAX_PLAYERS) {
            throw new ClientRoomJoinException(ClientRoomJoinException.ROOM_FULL);
        }
        if (isUserInRoom(user)) {
            throw new ClientRoomJoinException(ClientRoomJoinException.PLAYER_ALREADY_JOINED);
        }
        scores.put(user.getEmail(), 0);
        return participants.add(user);
    }

    /**
     * Removes user from room.
     * @param user User.
     * @return Boolean.
     * @throws ClientRoomLeaveException Thrown when user is not present in room.
     */
    synchronized public boolean leave(User user) throws ClientRoomLeaveException {
        if (!isUserInRoom(user)) {
            throw new ClientRoomLeaveException(ClientRoomLeaveException.USER_NOT_IN_ROOM);
        }
        scores.remove(user.getEmail());
        return participants.remove(user);
    }

    /**
     * Start game.
     * @throws PlayException Thrown when there are not enough users connected.
     */
    synchronized public void start() throws PlayException{
        if (participants.size() != MAX_PLAYERS) {
            throw new PlayException(String.format("Must be %s players to start a game", MAX_PLAYERS));
        }
        play = Optional.of(new Play(participants));
    }

    /**
     * Puts player's card on table.
     * @param userId User's ID.
     * @param card Card.
     * @return Boolean.
     * @throws PlayException PlayException.
     */
    synchronized public boolean playCard(String userId, Card card) throws PlayException {
        if (!play.isPresent()) {
            return false;
        }
        Play currentPlay = play.get();
        currentPlay.playCard(userId, card);
        if (currentPlay.hasRoundFinished()) {
            adjustScores(currentPlay);
            playHistory.add(currentPlay);
            if (!hasGameFinished()) {
                play = Optional.of(new Play(participants));
            }
        }
        return true;
    }

    /**
     * Skips game.
     * @param userId User's ID.
     * @return Boolean.
     * @throws PlayException Failed to start game.
     */
    synchronized public boolean skipPlay(String userId) throws PlayException {
        if (!play.isPresent()) {
            throw new PlayException("Play does not exist");
        }
        Play currentPlay = play.get();
        if (!owner.getUserID().equals(userId)) {
            throw new PlayException("You are not room owner!");
        }
        final Map<String, String> mapping = getUserIdsToEmailsMapping();
        participants.stream().map(User::getUserID).forEach(id -> {
            final String email = mapping.get(id);
            scores.put(email, scores.get(email) + (-1) * new Random().nextInt(100));
        });
        playHistory.add(currentPlay);
        if (!hasGameFinished()) {
            play = Optional.of(new Play(participants));
        } else {
            play = Optional.empty();
            playHistory = new ArrayList<>();
        }
        return true;
    }

    /**
     * Updates users' scores.
     * @param play Current game.
     * @throws PlayException PlayException.
     */
    private void adjustScores(Play play) throws PlayException {
        Map<String, Integer> playScores = scoreComputerGetters.get(playHistory.size()).apply(play).computeScores();
        playScores.forEach((userId, score) -> {
            final String email = getUserIdsToEmailsMapping().get(userId);
            if (!scores.containsKey(email)) {
                scores.put(email, 0);
            }
            scores.put(email, scores.get(email) + playScores.get(userId));
        });
    }

    /**
     * Checks if all games are finished.
     * @return Boolean.
     */
    public boolean hasGameFinished() {
        return playHistory.size() == scoreComputerGetters.size();
    }


    /**
     * Retrieves a participant in the room with given email.
     * @param email The email of the participant.
     * @return An Optional containing the participant with the specified email.
     */
    public Optional<User> getParticipantByEmail(String email) {
        return participants.stream()
                .filter(participant -> participant.getEmail().equals(email))
                .findFirst();
    }

    /**
     * Checks if it is currently the turn of the user with the specified user ID.
     * @param userId The user ID to check for the turn.
     * @return true if it is the turn of the user with the given ID.
     */
    public boolean isUserTurn(String userId) {
        return play.map(value -> value.getCurrentPlayingUserId().equals(userId)).orElse(false);
    }

    /**
     * Checks if a user with the specified email is present in the room.
     * @param email The email of the user to check for presence.
     * @return true if a user with the given email is in the room.
     */
    public boolean isUserInRoomByEmail(String email) {
        return participants.stream().map(User::getEmail).anyMatch(email::equals);
    }

    /**
     * Checks if a user is present in the room.
     * @param user The user to check for presence.
     * @return true if the user is in the room.
     */
    public boolean isUserInRoom(User user) {
        return participants.stream()
                .anyMatch(participant -> participant.getUserID().equals(user.getUserID()));
    }

    /**
     * Checks if the number of participants has reached the maximum allowed.
     * @return true if the number of participants equals the maximum allowed players; otherwise, false.
     */
    public boolean isMaxParticipants() {
        return participants.size() == MAX_PLAYERS;
    }

    /**
     * Checks if the given user is the owner of the room.
     * @param user The user to check for ownership.
     * @return true if the given user is the owner; otherwise, false.
     */
    public boolean isUserOwner(User user) {
        return isUserIdOwner(user.getUserID());
    }

    /**
     * Checks if the user with the specified user ID is the owner of the room.
     * @param userId The user ID to check for ownership.
     * @return true if the user with the given ID is the owner; otherwise, false.
     */
    public boolean isUserIdOwner(String userId) {
        return owner.getUserID().equals(userId);
    }

    /**
     * Gets the cards currently on the table.
     * @return Cards on table.
     */
    public Map<String, Card> getCardsOnTable() {
        return play.map(Play::getCardsOnTable).orElse(new HashMap<>());
    }

    /**
     * Gets the cards from the last trick.
     * @return Last trick.
     */
    public Map<String, Card> getLastTrick() {
        return play.map(Play::getLastTrick).orElse(new HashMap<>());
    }

    /**
     * Gets the first card played on the table in this game
     * @return First card on the table.
     */
    public Card getFirstCardOnTable() {
        return play.map(Play::getFirstCardOnTable).orElse(null);
    }

    /**
     * Gets the order of players in the game.
     * @return A list of user IDs.
     */
    public List<String> getPlayersOrder() {
        return play.map(Play::getPlayersOrder).orElse(new ArrayList<>());
    }

    /**
     * Gets a mapping of user IDs to their corresponding emails in the game.
     * @return A map representing the mapping of user IDs to emails.
     */
    public Map<String, String> getUserIdsToEmailsMapping() {
        return play.map(Play::getUserIdsToEmailsMapping).orElse(new HashMap<>());
    }

    /**
     * Gets the userID of the player currently making a move.
     * @return UserID of the current player.
     */
    public String getCurrentPlayingUserId() {
        return play.map(Play::getCurrentPlayingUserId).orElse(null);
    }
}
