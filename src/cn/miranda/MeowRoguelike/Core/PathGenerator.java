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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static cn.miranda.MeowRoguelike.Manager.ConfigManager.config;

public class PathGenerator {
    private final ArrayList<Node> nodes = new ArrayList<>();
    private final ArrayList<Integer> result = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0));

    public PathGenerator(Player player, int roomCount, int bonusCount) {
        generator(player, roomCount, bonusCount);
    }

    private void generator(Player player, int roomCount, int bonusCount) {
        nodes.add(new Node(new NodeLocation(0, 0, 0), new ArrayList<>(), null, RoomType.ORIGIN));
        addMainNode(roomCount);
        nodes.get(nodes.size() - 1).setRoomType(RoomType.BOSS);
        addSubNode(70, RoomType.MAIN);
        addSubNode(40, RoomType.SUB);
        addBonusNode(bonusCount);
        show(player);
    }

    /**
     * 根据随机数得出该节点是否允许连接新的节点
     *
     * @param chance 概率
     * @return 成功则返回 true，否则返回 false
     */
    private boolean canLink(int chance) {
        return Editor.getRandom(100) > chance;
    }

    /**
     * 返回根据类型筛选的房间名称
     *
     * @param prefix 筛选的类型
     * @return 房间名称
     */
    private String selectRoom(String prefix) {
        ArrayList<String> rooms = Editor.getRoomNames(prefix);
        if (rooms == null) {
            return null;
        }
        if (rooms.size() == 0) {
            return null;
        }
        return rooms.get(Editor.getRandom(rooms.size()));
    }

    /**
     * 返回所有节点
     *
     * @return 节点集合
     */
    @Deprecated
    public ArrayList<Node> getNodes() {
        return this.nodes;
    }

    /**
     * 添加指定个数的主路
     *
     * @param count 主路长度
     */
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

    /**
     * 根据概率添加支路
     *
     * @param chance   生成支路的概率
     * @param roomType 生成支路的房间类型
     */
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

    /**
     * 根据指定数量添加奖励房间，最终生成的奖励房间可能会少于给定的数量
     *
     * @param bonusCount 奖励房间的数量
     */
    private void addBonusNode(int bonusCount) {
        ArrayList<Node> normalNode = new ArrayList<>();
        for (Node node : nodes) {
            if (node.getRoomType() == RoomType.MAIN || node.getRoomType() == RoomType.SUB) {
                normalNode.add(node);
            }
        }
        for (int i = 0; i < bonusCount + 1; i++) {
            if (normalNode.size() == 0) {
                break;
            }
            Node nowNode = normalNode.get(Editor.getRandom(normalNode.size()));
            Node newNode = nowNode.link(nodes);
            if (newNode != null) {
                newNode.setRoomType(RoomType.BONUS);
                nodes.add(newNode);
            }
        }
    }

    /**
     * 为每个房间生成合理的门
     *
     * @param node     节点
     * @param location 基准坐标
     */
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
                    int floor = -(config.getInt("room.y") - 2);
                    doorLocation.clone().getBlock().setType(Material.AIR);
                    doorLocation.clone().add(1, floor, 0).getBlock().setType(Material.STONE);
                    doorLocation.clone().add(-1, floor, 0).getBlock().setType(Material.STONE);
                    doorLocation.clone().add(0, floor, 1).getBlock().setType(Material.STONE);
                    doorLocation.clone().add(0, floor, -1).getBlock().setType(Material.STONE);
                }
            }
        }
    }

    /**
     * 展现出整个迷宫
     *
     * @param player 展现在指定玩家的位置
     */
    private void show(Player player) {
        for (Node node : nodes) {
            try {
                String roomName = selectRoom(node.getRoomType().getPrefix());
                if (roomName == null) {
                    MessageManager.Message(player, String.format("§c房间没有成功生成, 因为不存在类型为 §9§l%s §c的房间", node.getRoomType()));
                    continue;
                }
                Editor.loadRegion(roomName, player, node.getRealLocation(player.getLocation()));
                creatDoors(node, player.getLocation());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 返回生成房间的结果
     *
     * @return 生成房间的结果
     */
    public ArrayList<Integer> getResult() {
        for (Node node : nodes) {
            switch (node.getRoomType()) {
                case ORIGIN:
                    result.set(0, result.get(0) + 1);
                    break;
                case MAIN:
                    result.set(1, result.get(1) + 1);
                    break;
                case SUB:
                    result.set(2, result.get(2) + 1);
                    break;
                case BONUS:
                    result.set(3, result.get(3) + 1);
                    break;
                case BOSS:
                    result.set(4, result.get(4) + 1);
                    break;
                default:
                    break;
            }
        }
        return result;
    }
}