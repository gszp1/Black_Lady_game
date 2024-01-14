package utils;

import utils.model.Room;

import java.util.ArrayList;
import java.util.List;

public class GameDetails {

    private List<Room> rooms = new ArrayList<>();

    public boolean createRoom(User user) {
        return rooms.add(new Room(user));
    }

    public Room deleteRoom(int roomId) {
        return rooms.remove(roomId);
    }

    public List<Room> getRooms() {
        return rooms;
    }
}
