package cn.miranda.MeowRoguelike.Command;

import cn.miranda.MeowCraft.Manager.MessageManager;
import cn.miranda.MeowCraft.Utils.Misc;
import cn.miranda.MeowRoguelike.RoomEditor.Editor;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
        ArrayList<String> validArgs = new ArrayList<>(Arrays.asList("save", "load", "list", "remove"));
        String option = args[0];
        if (!validArgs.contains(option)) {
            MessageManager.Message(player, "§c命令参数错误");
            return true;
        }
        if (Objects.equals(option, "save") && args.length == 2) {
            Region region = Editor.getSelection(player);
            if (region == null) {
                MessageManager.Message(player, "§e尚未选区或选区不符合要求");
            }

            return true;
        }
        MessageManager.Message(player, "§c命令参数错误");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 1) {
            return Misc.returnSelectList(new ArrayList<>(Arrays.asList("save", "load", "list", "remove")), strings[0]);
        }
        return null;
    }
}
