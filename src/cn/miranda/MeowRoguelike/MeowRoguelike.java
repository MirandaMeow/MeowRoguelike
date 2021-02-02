package cn.miranda.MeowRoguelike;

import cn.miranda.MeowCraft.Manager.MessageManager;
import cn.miranda.MeowRoguelike.Manager.*;
import org.bukkit.plugin.java.JavaPlugin;

public class MeowRoguelike extends JavaPlugin {
    public static MeowRoguelike plugin;
    public final String Version;

    public MeowRoguelike() {
        plugin = this;
        Version = this.getDescription().getVersion();
    }

    public void onEnable() {
        MessageManager.ConsoleMessage("[猫与地下城] 正在载入");
        PluginLoaderManager.loads();
        CommandRegister.registerCommands();
        ConfigManager.loadConfigs();
    }

    public void onDisable() {
        MessageManager.ConsoleMessage("[猫与地下城] 正在禁用");
        ConfigManager.saveConfigs();
    }
}
