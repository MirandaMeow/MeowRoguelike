package cn.miranda.MeowRoguelike.Command;

import cn.miranda.MeowCraft.Manager.MessageManager;
import cn.miranda.MeowCraft.Utils.Misc;
import cn.miranda.MeowRoguelike.Core.Editor;
import cn.miranda.MeowRoguelike.Core.PathGenerator;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static cn.miranda.MeowRoguelike.Manager.ConfigManager.config;

public class EditorCommand implements TabExecutor {
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
        if (!player.hasPermission("rl.edit")) {
            MessageManager.Message(player, "§c你没有权限");
            return true;
        }
        ArrayList<String> validArgs = new ArrayList<>(Arrays.asList("save", "load", "list", "remove", "create"));
        String option = args[0];
        if (!validArgs.contains(option)) {
            MessageManager.Message(player, "§c命令参数错误");
            return true;
        }
        int argLength = args.length;
        if (Objects.equals(option, "save") && argLength == 3) {
            Region region = Editor.getSelection(player);
            String type = args[1];
            String roomName = args[2];
            ArrayList<String> validType = new ArrayList<>(Arrays.asList("origin", "normal", "boss"));
            if (!validType.contains(type)) {
                MessageManager.Message(player, "§e允许的类型为 origin normal boss");
                return true;
            }
            if (region == null) {
                MessageManager.Message(player, String.format("§e尚未选区或选区不符合要求, 选区尺寸 §b(%d, %d, %d)", config.getInt("room.x"), config.getInt("room.y"), config.getInt("room.z")));
                return true;
            }
            Editor.saveRegion(region, String.format("%s-%s", type, roomName));
            MessageManager.Message(player, String.format("§e已保存房间 §9§l%s §e- §b%s", type.toUpperCase(), roomName));
            return true;
        }
        if (Objects.equals(option, "load") && argLength == 3) {
            String type = args[1];
            String roomName = args[2];
            ArrayList<String> validType = new ArrayList<>(Arrays.asList("origin", "normal", "boss"));
            if (!validType.contains(type)) {
                MessageManager.Message(player, "§e允许的类型为 origin normal boss");
                return true;
            }
            try {
                if (Editor.loadRegion(String.format("%s-%s", type, roomName), player, null)) {
                    MessageManager.Message(player, String.format("§e已读取房间 §9§l%s §e- §b%s", type.toUpperCase(), roomName));
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            MessageManager.Message(player, "§c房间不存在");
            return true;
        }
        if (Objects.equals(option, "list") && argLength == 2) {
            String type = args[1];
            ArrayList<String> validType = new ArrayList<>(Arrays.asList("origin", "normal", "boss", "all"));
            if (!validType.contains(type)) {
                MessageManager.Message(player, "§e允许的类型为 origin normal boss all");
                return true;
            }
            ArrayList<String> rooms = Editor.getRoomNames(type);
            if (rooms == null) {
                MessageManager.Message(player, "§e没有已保存的房间");
                return true;
            }
            MessageManager.Message(player, "§e房间列表");
            for (String i : rooms) {
                MessageManager.Message(player, String.format("§e- §9§l%s §e- §b%s §e-", i.split("-")[0].toUpperCase(), i.split("-")[1]));
            }
            return true;
        }
        if (Objects.equals(option, "remove") && argLength == 3) {
            String type = args[1];
            String roomName = args[2];
            ArrayList<String> validType = new ArrayList<>(Arrays.asList("origin", "normal", "boss"));
            if (!validType.contains(type)) {
                MessageManager.Message(player, "§e允许的类型为 origin normal boss");
                return true;
            }
            if (Editor.deleteRoom(String.format("%s-%s", type, roomName))) {
                MessageManager.Message(player, String.format("§e已删除房间 §9§l%s §b- §b%s", type.toUpperCase(), roomName));
                return true;
            }
            MessageManager.Message(player, "§c房间不存在");
            return true;
        }
        if (Objects.equals(option, "create") && argLength == 2) {
            int roomCount;
            try {
                roomCount = Integer.parseInt(args[1]) - 1;
                if (roomCount < 0) {
                    MessageManager.Message(player, "§c房间长度错误");
                    return true;
                }
            } catch (NumberFormatException e) {
                MessageManager.Message(player, "§c房间数量必须为数字");
                return true;
            }
            new PathGenerator(player, roomCount);
            MessageManager.Message(player, "§e生成完成");
            return true;
        }
        MessageManager.Message(player, "§c命令参数错误");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 1) {
            return Misc.returnSelectList(new ArrayList<>(Arrays.asList("save", "load", "list", "remove", "create")), strings[0]);
        }
        if (strings.length == 2) {
            if (Objects.equals(strings[0], "save") || Objects.equals(strings[0], "remove") || Objects.equals(strings[0], "load")) {
                ArrayList<String> validType = new ArrayList<>(Arrays.asList("origin", "normal", "boss"));
                return Misc.returnSelectList(validType, strings[1]);
            }
            if (Objects.equals(strings[0], "list")) {
                ArrayList<String> validType = new ArrayList<>(Arrays.asList("origin", "normal", "boss", "all"));
                return Misc.returnSelectList(validType, strings[1]);
            }
        }
        if (strings.length == 3) {
            if (Objects.equals(strings[0], "remove") || Objects.equals(strings[0], "load")) {
                ArrayList<String> validType = new ArrayList<>(Arrays.asList("origin", "normal", "boss"));
                if (validType.contains(strings[1])) {
                    ArrayList<String> validRoomNames = Editor.getRoomNames(strings[1]);
                    if (validRoomNames == null) {
                        return new ArrayList<>();
                    }
                    if (validRoomNames.size() == 0) {
                        return new ArrayList<>();
                    }
                    ArrayList<String> formatted = new ArrayList<>();
                    for (String string : validRoomNames) {
                        formatted.add(string.split("-")[1]);
                    }
                    return Misc.returnSelectList(formatted, strings[2]);
                }
            }
        }
        return new ArrayList<>();
    }
}
