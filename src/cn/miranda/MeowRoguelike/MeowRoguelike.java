package cn.miranda.MeowRoguelike;

import cn.miranda.MeowCraft.Manager.MessageManager;
import cn.miranda.MeowRoguelike.Manager.CommandRegister;
import cn.miranda.MeowRoguelike.Manager.ConfigManager;
import cn.miranda.MeowRoguelike.Manager.ListenersRegister;
import cn.miranda.MeowRoguelike.Manager.Session;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class MeowRoguelike extends JavaPlugin {
    public static MeowRoguelike plugin;
    public final String Version;
    public final HashMap<Player, Session> sessions = new HashMap<>();

    public MeowRoguelike() {
        plugin = this;
        Version = this.getDescription().getVersion();
    }

    public void onEnable() {
        MessageManager.ConsoleMessage("[猫与地下城] 正在载入");
        CommandRegister.registerCommands();
        ListenersRegister.registerAllEvents();
        ConfigManager.loadConfigs();
    }

    public void onDisable() {
        MessageManager.ConsoleMessage("[猫与地下城] 正在禁用");
        ConfigManager.saveConfigs();
    }
}
