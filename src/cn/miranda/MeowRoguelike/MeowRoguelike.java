package cn.miranda.MeowRoguelike;

import cn.miranda.MeowRoguelike.Manager.CommandRegister;
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
        System.out.print("[猫与地下城] 正在载入");
        PluginLoaderManager.loads();
        CommandRegister.registerCommands();
        ConfigManager.loadConfigs();
    }

    public void onDisable() {
        System.out.print("[猫与地下城] 正在禁用");
        ConfigManager.saveConfigs();
    }

    public File getSchemaFolder() {
        File folder = new File(plugin.getDataFolder() + "/rooms");
        if (!folder.exists()) {
            folder.mkdir();
        }
        return folder;
    }
}
