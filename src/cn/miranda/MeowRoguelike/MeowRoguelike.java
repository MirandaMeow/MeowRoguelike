package cn.miranda.MeowRoguelike;

import cn.miranda.MeowRoguelike.Command.EditorCommand;
import cn.miranda.MeowRoguelike.Manager.ConfigManager;
import cn.miranda.MeowRoguelike.Manager.PluginLoaderManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class MeowRoguelike extends JavaPlugin {
    public static MeowRoguelike plugin;
    public final String Version;

    public MeowRoguelike() {
        plugin = this;
        Version = this.getDescription().getVersion();
    }

    public void onEnable() {
        System.out.print("[猫与地下城] 载入中\n");
        PluginLoaderManager.loads();
        plugin.getCommand("rlroom").setExecutor(new EditorCommand());
        ConfigManager.loadConfigs();
    }

    public void onDisable() {
        System.out.print("[猫与地下城] 已禁用\n");
    }

    public File getSchemaFolder() {
        File folder = new File(plugin.getDataFolder() + "/rooms");
        if (!folder.exists()) {
            folder.mkdir();
        }
        return folder;
    }
}
