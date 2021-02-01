package cn.miranda.MeowRoguelike.Command;

import cn.miranda.MeowCraft.Manager.MessageManager;
import cn.miranda.MeowCraft.Utils.IO;
import cn.miranda.MeowCraft.Utils.Misc;
import cn.miranda.MeowRoguelike.Manager.ConfigManager;
import cn.miranda.MeowRoguelike.Manager.Session;
import cn.miranda.MeowRoguelike.RoomEditor.Room;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.*;

import static cn.miranda.MeowRoguelike.Manager.ConfigManager.rooms;
import static cn.miranda.MeowRoguelike.MeowRoguelike.plugin;

public class RoomEditor implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            MessageManager.Message(sender, "§c该命令不能在控制台运行");
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            MessageManager.Message(player, "用法xxxxxxxx");
            return true;
        }
        if (!player.hasPermission("rlroom.edit")) {
            MessageManager.Message(player, "§c你没有权限");
            return true;
        }
        ArrayList<String> validArgs = new ArrayList<>(Arrays.asList("select", "save", "load", "show", "list", "remove"));
        String option = args[0];
        if (!validArgs.contains(option)) {
            MessageManager.Message(player, "§c命令参数错误");
            return true;
        }
        if (Objects.equals(option, "select") && args.length == 1) {
            plugin.sessions.put(player, new Session(true, new ArrayList<>(), false, null, ""));
            MessageManager.Message(player, "§e请用木棍选择一个尺寸为 §b25 * 15 * 25 §e的选区");
            return true;
        }
        if (Objects.equals(option, "load") && args.length == 2) {
            String roomName = args[1];
            if (rooms.get(String.format("room.%s", roomName)) == null) {
                MessageManager.Message(player, "§c房间不存在");
                return true;
            }
            Room room = null;
            try {
                room = (Room) IO.decodeData(rooms.getString(String.format("room.%s", roomName)));
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            plugin.sessions.put(player, new Session(false, new ArrayList<>(), false, room, roomName));
            MessageManager.Message(player, String.format("§e已经载入房间 §b%s", roomName));
            return true;
        }
        if (Objects.equals(option, "save") && args.length == 2) {
            String roomName = args[1];
            if (!plugin.sessions.containsKey(player)) {
                MessageManager.Message(player, "§c请先选区再使用保存功能");
                return true;
            }
            Session playerSession = plugin.sessions.get(player);
            if (!playerSession.getReady()) {
                MessageManager.Message(player, "§c选区尚未成功创建");
                return true;
            }
            Room room = new Room(playerSession.getFirstLocation(), playerSession.getSecondLocation());
            playerSession.setRoom(room);
            try {
                rooms.set(String.format("room.%s", roomName), IO.encodeData(room));
            } catch (IOException e) {
                e.printStackTrace();
            }
            ConfigManager.saveConfigs();
            plugin.sessions.put(player, null);
            MessageManager.Message(player, String.format("§e已经保存房间 §b%s", roomName));
            return true;
        }
        if (Objects.equals(option, "show") && args.length == 1) {
            if (plugin.sessions.get(player) == null) {
                MessageManager.Message(player, "§c未选择房间");
                return true;
            }
            Session playerSession = plugin.sessions.get(player);
            if (playerSession.getRoom() == null) {
                MessageManager.Message(player, "§c未选择房间");
                return true;
            }
            playerSession.getRoom().showRoom(player.getLocation());
            MessageManager.Message(player, String.format("§e已经展现房间 §b%s", playerSession.getRoomName()));
            return true;
        }
        if (Objects.equals(option, "list") && args.length == 1) {
            Set<String> roomLib = rooms.getConfigurationSection("room").getValues(false).keySet();
            if (roomLib.size() == 0) {
                MessageManager.Message(player, "§e没有已保存的房间");
                return true;
            }
            MessageManager.Message(player, "§e已保存的房间: ");
            for (String i: roomLib) {
                MessageManager.Message(player, String.format("-- %s", i));
            }
            return true;
        }
        if (Objects.equals(option, "remove") && args.length==2) {
            String roomName = args[1];
            Set<String> roomLib = rooms.getConfigurationSection("room").getValues(false).keySet();
            if (!roomLib.contains(roomName)) {
                MessageManager.Message(player, String.format("§c房间 §b%s §c不存在", roomName));
                return true;
            }
            rooms.set(String.format("room.%s", roomName), null);
            MessageManager.Message(player, String.format("§c房间 §b%s §c已删除", roomName));
            return true;
        }
        MessageManager.Message(player, "§c命令参数错误");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 1) {
            return Misc.returnSelectList(new ArrayList<>(Arrays.asList("select", "save", "load", "show", "list", "remove")), strings[0]);
        }
        return null;
    }
}
