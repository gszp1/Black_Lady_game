package utils.model;

import cards.Card;
import exceptions.ClientRoomJoinException;
import exceptions.ClientRoomLeaveException;
import exceptions.PlayException;
import lombok.Getter;
import server.ConfigReader;
import utils.User;
import utils.score.NoTrickScoreComputer;
import utils.score.ScoreComputer;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 */
@Getter
public class Room {

    public static int MAX_PLAYERS = 4;

    private final List<Function<Play, ScoreComputer>> scoreComputerGetters;

    private int id;

    private List<User> participants = new ArrayList<>();

    private Map<String, Integer> scores = new HashMap<>();

    private User owner;

    private List<Play> playHistory = new ArrayList<>();

    private Optional<Play> play = Optional.empty();

    private List<ChatEntry> chatEntries = new ArrayList<>();

    public Room(User owner, int id) {
        this.owner = owner;
        this.id = id;
        this.scoreComputerGetters = new ConfigReader().readConfig();
        scores.put(owner.getEmail(), 0);
        participants.add(owner);
    }

    public void writeToChat(String email, String message) throws PlayException {
        if (!isUserInRoomByEmail(email)) {
            throw new PlayException("Cannot write to chat if user is not in room");
        }
        chatEntries.add(new ChatEntry(email, message));
    }

    public boolean isStarted() {
        return play.isPresent();
    }

    public boolean join(User user) throws ClientRoomJoinException {
        if (participants.size() >= MAX_PLAYERS) {
            throw new ClientRoomJoinException(ClientRoomJoinException.ROOM_FULL);
        }
        if (isUserInRoom(user)) {
            throw new ClientRoomJoinException(ClientRoomJoinException.PLAYER_ALREADY_JOINED);
        }
        scores.put(user.getEmail(), 0);
        // TODO exception handling
        return participants.add(user);
    }

    synchronized public boolean leave(User user) throws ClientRoomLeaveException {
        if (!isUserInRoom(user)) {
            throw new ClientRoomLeaveException(ClientRoomLeaveException.USER_NOT_IN_ROOM);
        }
        scores.remove(user.getEmail());
        return participants.remove(user);
    }

    synchronized public void start() throws PlayException{
        if (participants.size() != MAX_PLAYERS) {
            throw new PlayException(String.format("Must be %s players to start a game", MAX_PLAYERS));
        }
        play = Optional.of(new Play(participants));
    }

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

    public boolean hasGameFinished() {
        return playHistory.size() == scoreComputerGetters.size();
    }

//    synchronized public void playCard(User user, Card card) throws PlayException {
//        if (!userHasCard(user.getUserID(), card)) {
//            throw new PlayException(String.format("%s does not have card %s", user.getUsername(), card.toString()));
//        }
//    }
//
//    private boolean userHasCard(String userId, Card card) {
//        return play.map(value -> value.getCards(userId).stream()
//                .anyMatch(c -> c.getCardType().equals(card.getCardType()) && c.getCardSet().equals(card.getCardSet()))
//        ).orElse(false);
//    }

    public Optional<User> getParticipantByEmail(String email) {
        return participants.stream()
                .filter(participant -> participant.getEmail().equals(email))
                .findFirst();
    }

    public boolean isUserTurn(String userId) {
        return play.map(value -> value.getCurrentPlayingUserId().equals(userId)).orElse(false);
    }

    public boolean isUserInRoomByEmail(String email) {
        return participants.stream().map(User::getEmail).anyMatch(email::equals);
    }

    public boolean isUserInRoom(User user) {
        return participants.stream()
                .anyMatch(participant -> participant.getUserID().equals(user.getUserID()));
    }

    public boolean isMaxParticipants() {
        return participants.size() == MAX_PLAYERS;
    }

    public boolean isUserOwner(User user) {
        return isUserIdOwner(user.getUserID());
    }

    public boolean isUserIdOwner(String userId) {
        return owner.getUserID().equals(userId);
    }

    public Map<String, Card> getCardsOnTable() {
        return play.map(Play::getCardsOnTable).orElse(new HashMap<>());
    }

    public Map<String, Card> getLastTrick() {
        return play.map(Play::getLastTrick).orElse(new HashMap<>());
    }

    public Card getFirstCardOnTable() {
        return play.map(Play::getFirstCardOnTable).orElse(null);
    }

    public List<String> getPlayersOrder() {
        return play.map(Play::getPlayersOrder).orElse(new ArrayList<>());
    }

    public Map<String, String> getUserIdsToEmailsMapping() {
        return play.map(Play::getUserIdsToEmailsMapping).orElse(new HashMap<>());
    }

    public String getCurrentPlayingUserId() {
        return play.map(Play::getCurrentPlayingUserId).orElse(null);
    }
}
