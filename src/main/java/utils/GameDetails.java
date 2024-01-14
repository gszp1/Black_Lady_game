package utils;

import utils.model.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GameDetails {

    int currentRoomId = 0;

    private List<Room> rooms = new ArrayList<>();

    public Room createRoom(User user) {
        currentRoomId++;
        final Room newRoom = new Room(user, currentRoomId);
        rooms.add(newRoom);
        return newRoom;
    }

    public boolean deleteRoom(int roomId) {
        return rooms.removeIf(room -> room.getId() == roomId);
    }

    public Optional<Room> getRoomById(int id) {
        return rooms.stream().filter(room -> room.getId() == id).findFirst();
    }

    public List<Room> getRooms() {
        return rooms;
    }
}
