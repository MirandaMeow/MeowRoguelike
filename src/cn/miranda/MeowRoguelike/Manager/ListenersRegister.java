package cn.miranda.MeowRoguelike.Manager;


import cn.miranda.MeowRoguelike.Listeners.RoomSelectorEvent;
import cn.miranda.MeowRoguelike.MeowRoguelike;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.LinkedList;
import java.util.List;

public class ListenersRegister {
    private static final List<Listener> list = new LinkedList<>();

    private static void register(Listener listener) {
        Bukkit.getServer().getPluginManager().registerEvents(listener, MeowRoguelike.plugin);
    }

    public static void registerAllEvents() {
        list.add(new RoomSelectorEvent());
        for (Listener i : list) {
            register(i);
        }
    }
}
