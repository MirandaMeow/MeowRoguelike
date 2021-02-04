package cn.miranda.MeowRoguelike.Generator;

import cn.miranda.MeowRoguelike.Room.Editor;
import cn.miranda.MeowRoguelike.Room.Node;
import cn.miranda.MeowRoguelike.Room.NodeLocation;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;

import static cn.miranda.MeowRoguelike.Manager.ConfigManager.config;


public class PathGenerator {
    public static void generator(Player player) {
        ArrayList<Node> nodes = new ArrayList<>();
        nodes.add(new Node(new NodeLocation(0, 0, 0), new ArrayList<>(), null));
        int N = config.getInt("room.n") - 1;
        for (int i = 0; i < N; i++) {
            Node nowNode = nodes.get(nodes.size() - 1);
            Node newNode = nowNode.link(nodes);
            nodes.add(newNode);
        }
        for (Node node : nodes) {
            try {
                Editor.loadRegion("1", player, node.getRealLocation(player.getLocation()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
