package cn.miranda.MeowRoguelike.Manager;

import cn.miranda.MeowRoguelike.Command.RoomEditor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;

import java.util.HashMap;
import java.util.Map;

import static cn.miranda.MeowRoguelike.MeowRoguelike.plugin;

public class CommandRegister {
    private static final HashMap<String, CommandExecutor> map = new HashMap<>();

    private static void register(String commandName, CommandExecutor Executor) {
        PluginCommand command = plugin.getCommand(commandName);
        command.setExecutor(Executor);
    }

    public static void registerCommands() {
        map.put("rlroom", new RoomEditor());
        for (Map.Entry<String, CommandExecutor> entry : map.entrySet()) {
            register(entry.getKey(), entry.getValue());
        }
    }
}
