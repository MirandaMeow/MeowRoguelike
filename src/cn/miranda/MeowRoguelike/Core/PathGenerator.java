package cn.miranda.MeowRoguelike.Core;

import cn.miranda.MeowCraft.Manager.MessageManager;
import cn.miranda.MeowRoguelike.Room.Direction;
import cn.miranda.MeowRoguelike.Room.Node;
import cn.miranda.MeowRoguelike.Room.NodeLocation;
import cn.miranda.MeowRoguelike.Room.RoomType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PathGenerator {
    private final ArrayList<Node> nodes = new ArrayList<>();

    public PathGenerator(Player player, int roomCount) {
        generator(player, roomCount);
    }

    private void generator(Player player, int count) {
        nodes.add(new Node(new NodeLocation(0, 0, 0), new ArrayList<>(), null, RoomType.ORIGIN));
        addMainNode(count);
        nodes.get(nodes.size() - 1).setRoomType(RoomType.BOSS);
        addSubNode(70, RoomType.MAIN);
        addSubNode(40, RoomType.SUB);
        show(player);
    }

    private boolean canLink(int chance) {
        Random random = new Random();
        return random.nextInt(100) > chance;
    }

    private String selectRoom(String prefix) {
        ArrayList<String> rooms = Editor.getRoomNames(prefix);
        if (rooms == null) {
            return null;
        }
        if (rooms.size() == 0) {
            return null;
        }
        Random random = new Random();
        return rooms.get(random.nextInt(rooms.size()));
    }

    public ArrayList<Node> getNodes() {
        return this.nodes;
    }

    private void addMainNode(int count) {
        for (int i = 0; i < count; i++) {
            Node nowNode = nodes.get(nodes.size() - 1);
            Node newNode = nowNode.link(nodes);
            if (newNode != null) {
                newNode.setRoomType(RoomType.MAIN);
                nodes.add(newNode);
            }
        }
    }

    private void addSubNode(int chance, RoomType roomType) {
        ArrayList<Node> subNodes = new ArrayList<>();
        for (Node nowNode : nodes) {
            if (!canLink(chance)) {
                continue;
            }
            if (nowNode.getRoomType() != roomType) {
                continue;
            }
            Node newNode = nowNode.link(nodes);
            if (newNode != null) {
                newNode.setRoomType(RoomType.SUB);
                subNodes.add(newNode);
            }
        }
        nodes.addAll(subNodes);
    }

    private void creatDoors(Node node, Location location) {
        HashMap<Direction, Location> doors = node.getDoors(location);
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
    }

    private void show(Player player) {
        for (Node node : nodes) {
            try {
                String roomName = selectRoom(node.getRoomType().getPrefix());
                if (roomName == null) {
                    MessageManager.Message(player, String.format("§c房间没有成功生成, 因为不存在类型为 §9§l%s §c的房间", node.getRoomType()));
                }
                Editor.loadRegion(roomName, player, node.getRealLocation(player.getLocation()));
                creatDoors(node, player.getLocation());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}