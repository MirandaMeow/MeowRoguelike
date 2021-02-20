package cn.miranda.MeowRoguelike.Manager;

import cn.miranda.MeowRoguelike.MeowRoguelike;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import static org.bukkit.Bukkit.getServer;

public class PluginLoaderManager {
    public static Plugin worldEdit;

    public static void loadMeowCraft() {
        if (getServer().getPluginManager().getPlugin("MeowCraft") == null) {
            System.out.print("[猫与地下城] 未找到 MeowCraft, 组件即将禁用\n");
            Bukkit.getServer().getPluginManager().disablePlugin(MeowRoguelike.plugin);
        } else {
            System.out.print("[猫与地下城] 发现 MeowCraft，焊接成功\n");
        }
    }

    public static void loadWorldEdit() {
        worldEdit = getServer().getPluginManager().getPlugin("WorldEdit");
        if (worldEdit == null) {
            System.out.print("[猫与地下城] 未找到 WorldEdit, 组件即将禁用\n");
            Bukkit.getServer().getPluginManager().disablePlugin(MeowRoguelike.plugin);
        } else {
            System.out.print("[猫与地下城] 发现 WorldEdit，焊接成功\n");
        }
    }

    public static void loads() {
        loadMeowCraft();
        loadWorldEdit();
    }
}
