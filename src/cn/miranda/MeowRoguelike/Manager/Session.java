package cn.miranda.MeowRoguelike.Manager;

import cn.miranda.MeowRoguelike.RoomEditor.Room;
import org.bukkit.Location;

import java.util.ArrayList;

public class Session {
    private boolean inSelect;
    private ArrayList<Location> selectLocations;
    private boolean ready;
    private Room room;
    private String roomName;

    public Session(boolean inSelect, ArrayList<Location> selectLocations, boolean ready, Room room, String roomName) {
        this.inSelect = inSelect;
        this.selectLocations = selectLocations;
        this.ready = ready;
        this.room = room;
        this.roomName = roomName;
    }

    public void setSelect(boolean set) {
        this.inSelect = set;
    }

    public boolean getInSelect() {
        return this.inSelect;
    }

    public void setFirstLocation(Location location) {
        this.selectLocations = new ArrayList<>();
        this.selectLocations.add(location);
    }

    public void setSecondLocation(Location location) {
        if (this.selectLocations.size() == 1) {
            this.selectLocations.add(location);
        }
    }

    public void resetLocation() {
        this.selectLocations = new ArrayList<>();
    }

    public boolean getFirstLocationReady() {
        return this.selectLocations.size() == 1;
    }

    public Location getFirstLocation() {
        if (this.selectLocations.size() == 0) {
            return null;
        }
        return this.selectLocations.get(0);
    }

    public Location getSecondLocation() {
        if (this.selectLocations.size() == 1) {
            return null;
        }
        return this.selectLocations.get(1);
    }

    public boolean getReady() {
        return this.ready;
    }

    public void setReady(boolean set) {
        this.ready = set;
    }

    public Room getRoom() {
        return this.room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomName() {
        return this.roomName;
    }
}
