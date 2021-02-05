package cn.miranda.MeowRoguelike.Room;

import cn.miranda.MeowRoguelike.Core.Editor;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static cn.miranda.MeowRoguelike.Manager.ConfigManager.config;

public class Node {
    private final NodeLocation nodeLocation;
    private final ArrayList<Direction> directions;
    private final Node lastNode;
    private RoomType roomType;

    public Node(NodeLocation nodeLocation, ArrayList<Direction> directions, Node lastNode, RoomType roomType) {
        this.nodeLocation = nodeLocation;
        this.directions = directions;
        this.lastNode = lastNode;
        this.roomType = roomType;
    }

    /**
     * 获取该节点的相对坐标
     *
     * @return 该节点的相对坐标
     */
    public NodeLocation getNodeLocation() {
        return this.nodeLocation;
    }

    /**
     * 设置房间类型
     *
     * @param roomType 房间类型
     */
    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    /**
     * 获取房间类型
     *
     * @return 房间类型
     */
    public RoomType getRoomType() {
        return this.roomType;
    }

    /**
     * 获取门的绝对位置列表
     *
     * @param location 基准坐标
     * @return 绝对位置列表
     */
    public HashMap<Direction, Location> getDoors(Location location) {
        HashMap<Direction, Location> doors = new HashMap<>();
        Location realLocation = this.getRealLocation(location);
        for (Direction direction : this.directions) {
            doors.put(direction, realLocation.clone().add(direction.getDoorLocation().getX(), direction.getDoorLocation().getY(), direction.getDoorLocation().getZ()));
        }
        return doors;
    }

    /**
     * 获取所有方位的列表
     *
     * @return 方位列表
     */
    public ArrayList<Direction> getDirections() {
        return this.directions;
    }

    /**
     * 获取基准坐标
     *
     * @param location 绝对坐标
     * @return 基准坐标
     */
    public Location getRealLocation(Location location) {
        int x = config.getInt("room.x");
        int y = config.getInt("room.y");
        int z = config.getInt("room.z");
        return location.add(this.nodeLocation.getX() * x, this.nodeLocation.getY() * y, this.nodeLocation.getZ() * z);
    }

    /**
     * 将该房间连接到一个新的房间
     *
     * @param nodes 所有节点的列表
     * @return 新的节点
     */
    public Node link(ArrayList<Node> nodes) {
        ArrayList<Direction> directions = new ArrayList<>(Arrays.asList(Direction.values()));
        Direction opposite;
        for (Direction direction : Direction.values()) {
            NodeLocation tempLocation = this.nodeLocation.add(direction.getOffset());
            for (Node node : nodes) {
                if (node.getNodeLocation().equals(tempLocation)) {
                    directions.remove(direction);
                }
            }
            if (lastNode != null) {
                if (tempLocation.equals(lastNode.getNodeLocation())) {
                    if (!this.directions.contains(direction)) {
                        this.directions.add(direction);
                    }
                }
            }
        }
        Direction newDirection;
        if (directions.size() > 0) {
            newDirection = directions.get(Editor.getRandom(directions.size()));
            if (!this.directions.contains(newDirection)) {
                this.directions.add(newDirection);
            }
            opposite = newDirection.opposite();
            ArrayList<Direction> newDirections = new ArrayList<>();
            newDirections.add(opposite);
            return new Node(this.nodeLocation.add(newDirection.getOffset()), newDirections, this, null);
        }
        return null;
    }
}
