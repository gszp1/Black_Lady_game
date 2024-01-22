package utils;

import utils.model.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *  Class containing data about active game rooms.
 */
public class GameDetails {

    int currentRoomId = 0;

    /**
     * List of rooms.
     */
    private List<Room> rooms = new ArrayList<>();

    /**
     * Creates new game room.
     * @param user Room owner.
     * @return Reference to new room object.
     */
    public Room createRoom(User user) {
        currentRoomId++;
        final Room newRoom = new Room(user, currentRoomId);
        rooms.add(newRoom);
        return newRoom;
    }

    /**
     * Removes room with given ID.
     * @param roomId Room's ID.
     * @return Boolean, true if success, otherwise false.
     */
    public boolean deleteRoom(int roomId) {
        return rooms.removeIf(room -> room.getId() == roomId);
    }

    /**
     * Finds room with given ID.
     * @param id Room's ID.
     * @return Reference to room.
     */
    public Optional<Room> getRoomById(int id) {
        return rooms.stream().filter(room -> room.getId() == id).findFirst();
    }

    /**
     * Getter for list of rooms.
     * @return List of rooms.
     */
    public List<Room> getRooms() {
        return rooms;
    }
}
