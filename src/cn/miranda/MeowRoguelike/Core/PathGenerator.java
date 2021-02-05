package cn.miranda.MeowRoguelike.Core;

import cn.miranda.MeowRoguelike.Room.Direction;
import cn.miranda.MeowRoguelike.Room.Node;
import cn.miranda.MeowRoguelike.Room.NodeLocation;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class PathGenerator {
    private final Player player;
    private final int roomCount;

    public PathGenerator(Player player, int roomCount) {
        this.player = player;
        this.roomCount = roomCount;
        generator(this.player, this.roomCount);
    }

    private void generator(Player player, int count) {
        ArrayList<Node> mainNodes = new ArrayList<>();
        mainNodes.add(new Node(new NodeLocation(0, 0, 0), new ArrayList<>(), null));
        for (int i = 0; i < count; i++) {
            Node nowNode = mainNodes.get(mainNodes.size() - 1);
            Node newNode = nowNode.link(mainNodes);
            if (newNode != null) {
                mainNodes.add(newNode);
            }
        }
        ArrayList<Node> allNode = new ArrayList<>(mainNodes);
        int possibleCount = mainNodes.size();
        for (int i = 0; i < possibleCount; i++) {
            Node nowNode = allNode.get(i);
            if (!canLink()) {
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
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.print(node.getDirections());
        }
    }

    private boolean canLink() {
        Random random = new Random();
        return random.nextInt(100) > 70;
    }

    private String selectRoom() {
        ArrayList<String> rooms = Editor.getRoomNames();
        if (rooms == null) {
            return null;
        }
        Random random = new Random();
        return rooms.get(random.nextInt(rooms.size())).replace(".schema", "");
    }
}