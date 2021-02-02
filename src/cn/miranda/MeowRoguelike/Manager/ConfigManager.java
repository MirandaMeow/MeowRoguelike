package cn.miranda.MeowRoguelike.Manager;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static cn.miranda.MeowRoguelike.MeowRoguelike.plugin;

public class ConfigManager {
    public static HashMap<YamlConfiguration, File> configList = new HashMap<>();
    public static File configFile;
    public static YamlConfiguration config;

    public static YamlConfiguration loadFile(String fileName) {
        configFile = new File(plugin.getDataFolder(), fileName);
        if (!configFile.exists()) {
            plugin.saveResource(fileName, false);
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        configList.put(config, configFile);
        return config;
    }

    public static void loadConfigs() {
        configList = new HashMap<>();
        config = loadFile("config.yml");
    }

    public static void saveConfigs() {
        try {
            for (Map.Entry<YamlConfiguration, File> current : configList.entrySet()) {
                YamlConfiguration currentYaml = current.getKey();
                File currentFile = current.getValue();
                currentYaml.save(currentFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
