package cn.miranda.MeowRoguelike.Room;

import cn.miranda.MeowRoguelike.Core.Editor;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Arrays;

import static cn.miranda.MeowRoguelike.Manager.ConfigManager.config;

public class Node {
    private final NodeLocation nodeLocation;
    private final ArrayList<Direction> directions;
    private final Node lastNode;

    public Node(NodeLocation nodeLocation, ArrayList<Direction> directions, Node lastNode) {
        this.nodeLocation = nodeLocation;
        this.directions = directions;
        this.lastNode = lastNode;
    }

    public NodeLocation getNodeLocation() {
        return this.nodeLocation;
    }

    public Node getLastNode() {
        return this.lastNode;
    }

    public ArrayList<Direction> getDirections() {
        return this.directions;
    }

    public Location getRealLocation(Location location) {
        int x = config.getInt("room.x");
        int y = config.getInt("room.y");
        int z = config.getInt("room.z");
        return location.add(this.nodeLocation.getX() * x, this.nodeLocation.getY() * y, this.nodeLocation.getZ() * z);
    }

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
            return new Node(this.nodeLocation.add(newDirection.getOffset()), newDirections, this);
        }
        return null;
    }


}
