package cn.miranda.MeowRoguelike.RoomEditor;

import cn.miranda.MeowRoguelike.Manager.PluginLoaderManager;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.World;
import org.bukkit.entity.Player;


public class Editor {
    public static Region getSelection(Player player) {
        Region region;
        LocalSession playerSession = ((WorldEditPlugin) PluginLoaderManager.worldEdit).getSession(player);
        World selectWorld = playerSession.getSelectionWorld();
        if (selectWorld == null) {
            return null;
        }
        region = playerSession.getSelection(selectWorld);
        int fx = region.getMinimumPoint().getX();
        int fy = region.getMinimumPoint().getY();
        int fz = region.getMinimumPoint().getZ();
        int sx = region.getMaximumPoint().getX();
        int sy = region.getMaximumPoint().getY();
        int sz = region.getMaximumPoint().getZ();
        if (Math.abs(fx - sx) + 1 == 25 && Math.abs(fy - sy) + 1 == 15 && Math.abs(fz - sz) + 1 == 25) {
            return region;
        }
        return null;
    }
}
