package cn.miranda.MeowRoguelike.Core;

import cn.miranda.MeowRoguelike.Room.Direction;
import cn.miranda.MeowRoguelike.Room.Node;
import cn.miranda.MeowRoguelike.Room.NodeLocation;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PathGenerator {
    private final Player player;
    private final int roomCount;
    private ArrayList<Node> allNode;

    public PathGenerator(Player player, int roomCount) {
        this.player = player;
        this.roomCount = roomCount;
        generator(this.player, this.roomCount);
    }

    private void generator(Player player, int count) {
        ArrayList<Node> mainNodes = new ArrayList<>();
        ArrayList<Node> subNodes = new ArrayList<>();
        mainNodes.add(new Node(new NodeLocation(0, 0, 0), new ArrayList<>(), null));
        for (int i = 0; i < count; i++) {
            Node nowNode = mainNodes.get(mainNodes.size() - 1);
            Node newNode = nowNode.link(mainNodes);
            if (newNode != null) {
                mainNodes.add(newNode);
            }
        }
        allNode = new ArrayList<>(mainNodes);
        for (Node nowNode : mainNodes) {
            if (!canLink(70)) {
                continue;
            }
            Node newNode = nowNode.link(allNode);
            if (newNode != null) {
                subNodes.add(newNode);
                allNode.add(newNode);
            }
        }
        for (Node nowNode : subNodes) {
            if (!canLink(40)) {
                continue;
            }
            Node newNode = nowNode.link(allNode);
            if (newNode != null) {
                allNode.add(newNode);
            }
        }
        for (Node node : allNode) {
            try {
                Editor.loadRegion(selectRoom(), player, node.getRealLocation(player.getLocation()));
                HashMap<Direction, Location> doors = node.getDoors(player.getLocation());//开门
                for (Map.Entry<Direction, Location> door : doors.entrySet()) {
                    Location doorLocation = door.getValue();
                    if (door.getKey().isFlat()) {
                        doorLocation.getBlock().setType(Material.AIR);
                        doorLocation.add(0, 1, 0).getBlock().setType(Material.AIR);
                    } else {
                        if (door.getKey().equals(Direction.DOWN)) {
                            doorLocation.getBlock().setType(Material.WATER);
                        } else {
                            doorLocation.clone().getBlock().setType(Material.AIR);
                            doorLocation.clone().add(1, -13, 0).getBlock().setType(Material.STONE);
                            doorLocation.clone().add(-1, -13, 0).getBlock().setType(Material.STONE);
                            doorLocation.clone().add(0, -13, 1).getBlock().setType(Material.STONE);
                            doorLocation.clone().add(0, -13, -1).getBlock().setType(Material.STONE);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean canLink(int chance) {
        Random random = new Random();
        return random.nextInt(100) > chance;
    }

    private String selectRoom() {
        ArrayList<String> rooms = Editor.getRoomNames();
        if (rooms == null) {
            return null;
        }
        Random random = new Random();
        return rooms.get(random.nextInt(rooms.size()));
    }

    public ArrayList<Node> getNodes() {
        return this.allNode;
    }
}