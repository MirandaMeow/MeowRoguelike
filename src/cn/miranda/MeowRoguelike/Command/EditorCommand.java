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

import java.io.FileNotFoundException;
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
        ArrayList<String> validArgs = new ArrayList<>(Arrays.asList("save", "load", "list", "remove", "test", "create"));
        String option = args[0];
        if (!validArgs.contains(option)) {
            MessageManager.Message(player, "§c命令参数错误");
            return true;
        }
        int argLength = args.length;
        if (Objects.equals(option, "save") && argLength == 2) {
            Region region = Editor.getSelection(player);
            String roomName = args[1];
            if (region == null) {
                MessageManager.Message(player, String.format("§e尚未选区或选区不符合要求, 选区尺寸 §b(%d, %d, %d)", config.getInt("room.x"), config.getInt("room.y"), config.getInt("room.z")));
                return true;
            }
            try {
                Editor.saveRegion(region, roomName);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            MessageManager.Message(player, String.format("§e已保存房间 §b%s", roomName));
            return true;
        }
        if (Objects.equals(option, "load") && argLength == 2) {
            String roomName = args[1];
            try {
                if (Editor.loadRegion(roomName, player, null)) {
                    MessageManager.Message(player, String.format("§e已读取房间 §b%s", roomName));
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            MessageManager.Message(player, "§c房间不存在");
            return true;
        }
        if (Objects.equals(option, "list") && argLength == 1) {
            ArrayList<String> rooms = Editor.getRoomNames();
            if (rooms == null) {
                MessageManager.Message(player, "§e没有已保存的房间");
                return true;
            }
            MessageManager.Message(player, "§e房间列表");
            for (String i : rooms) {
                MessageManager.Message(player, String.format("§b--- §e%s", i.replace(".schema", "")));
            }
            return true;
        }
        if (Objects.equals(option, "remove") && argLength == 2) {
            String roomName = args[1];
            if (Editor.deleteRoom(roomName)) {
                MessageManager.Message(player, String.format("§e已删除房间 §b%s", roomName));
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
            return Misc.returnSelectList(new ArrayList<>(Arrays.asList("save", "load", "list", "remove", "test", "create")), strings[0]);
        }
        return null;
    }
}
