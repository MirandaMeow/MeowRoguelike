package cn.miranda.MeowRoguelike.Manager;

import cn.miranda.MeowRoguelike.MeowRoguelike;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import static org.bukkit.Bukkit.getServer;

public class PluginLoaderManager {
    public static Plugin worldEdit;
    public static void loadMeowCraft() {
        if (getServer().getPluginManager().getPlugin("MeowCraft") == null) {
            System.out.print("§b[§6猫子组件§b] §e未找到 MeowCraft, 组件即将禁用");
            Bukkit.getServer().getPluginManager().disablePlugin(MeowRoguelike.plugin);
        } else {
            System.out.print("§b[§6猫子组件§b] §e发现 MeowCraft，焊接成功");
        }
    }

    public static void loadWorldEdit() {
        worldEdit = getServer().getPluginManager().getPlugin("WorldEdit");
        if (worldEdit == null) {
            System.out.print("§b[§6猫子组件§b] §e未找到 WorldEdit, 组件即将禁用");
            Bukkit.getServer().getPluginManager().disablePlugin(MeowRoguelike.plugin);
        } else {
            System.out.print("§b[§6猫子组件§b] §e发现 WorldEdit，焊接成功");
        }
    }

    public static void loads() {
        loadMeowCraft();
        loadWorldEdit();
    }
}
