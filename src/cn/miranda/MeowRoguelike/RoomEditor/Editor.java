package cn.miranda.MeowRoguelike.RoomEditor;

import cn.miranda.MeowRoguelike.Manager.PluginLoaderManager;
import com.boydti.fawe.util.EditSessionBuilder;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.*;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.*;

import static cn.miranda.MeowRoguelike.MeowRoguelike.plugin;


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

    public static void saveRegion(Region region, String roomName) throws FileNotFoundException {
        BlockArrayClipboard clipboard = new BlockArrayClipboard(region);
        File file = new File(plugin.getSchemaFolder(), String.format("%s.schema", roomName));
        EditSession editSession = new EditSession(new EditSessionBuilder(region.getWorld()));
        ForwardExtentCopy forwardExtentCopy = new ForwardExtentCopy(editSession, region, clipboard.getOrigin(), clipboard, region.getMinimumPoint());
        forwardExtentCopy.setCopyingEntities(true);
        Operations.complete(forwardExtentCopy);
        try (ClipboardWriter writer = BuiltInClipboardFormat.FAST.getWriter(new FileOutputStream(file))) {
            writer.write(clipboard);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean loadRegion(String roomName, Player player) throws IOException {
        LocalSession playerSession = ((WorldEditPlugin) PluginLoaderManager.worldEdit).getSession(player);
        File file = new File(plugin.getSchemaFolder(), String.format("%s.schema", roomName));
        Location location = player.getLocation();
        if (!file.exists()) {
            return false;
        }
        ClipboardFormat format = ClipboardFormats.findByFile(file);
        try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
            Clipboard clipboard = reader.read();
            playerSession.setClipboard(new ClipboardHolder(clipboard));
            try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(player.getWorld()), -1)) {
                Operation operation = new ClipboardHolder(clipboard)
                        .createPaste(editSession)
                        .copyEntities(true)
                        .to(BlockVector3.at(location.getX(), location.getY(), location.getZ()))
                        .ignoreAirBlocks(false)
                        .build();
                Operations.complete(operation);
            }
            return true;
        }
    }
}
