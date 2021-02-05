package cn.miranda.MeowRoguelike.Core;

import cn.miranda.MeowRoguelike.Manager.PluginLoaderManager;
import com.boydti.fawe.util.EditSessionBuilder;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.entity.Entity;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.*;
import com.sk89q.worldedit.function.mask.ExistingBlockMask;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockID;
import com.sk89q.worldedit.world.block.BlockState;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static cn.miranda.MeowRoguelike.Manager.ConfigManager.config;
import static cn.miranda.MeowRoguelike.MeowRoguelike.plugin;


public class Editor {
    /**
     * @param player 进行动作的玩家
     * @return 返回空
     */
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
        if (Math.abs(fx - sx) + 1 == config.getInt("room.x") && Math.abs(fy - sy) + 1 == config.getInt("room.y") && Math.abs(fz - sz) + 1 == config.getInt("room.z")) {
            return region;
        }
        return null;
    }

    /**
     * @param region   被保存的区域
     * @param roomName 被保存的区域的文件名
     * @throws FileNotFoundException 发生错误时抛出异常
     */
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
        Mask mask = new ExistingBlockMask(editSession);
        editSession.replaceBlocks(region, mask, new BaseBlock(BlockState.getFromInternalId(BlockID.AIR)));
        List<? extends Entity> entities = editSession.getEntities(region);
        for (Entity i : entities) {
            if (Objects.equals(i.getType().toString(), "minecraft:player")) {
                continue;
            }
            i.remove();
        }
        editSession.flushQueue();
    }

    /**
     * @param roomName 被载入的区域的文件名
     * @param player   进行动作的玩家
     * @param location 生成区域的位置
     * @return 如果成功则返回 true，否则返回 false
     * @throws IOException 发生错误时抛出异常
     */
    public static boolean loadRegion(String roomName, Player player, Location location) throws IOException {
        LocalSession playerSession = ((WorldEditPlugin) PluginLoaderManager.worldEdit).getSession(player);
        File file = new File(plugin.getSchemaFolder(), String.format("%s.schema", roomName));
        if (!file.exists()) {
            return false;
        }
        ClipboardFormat format = ClipboardFormats.findByFile(file);
        try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
            Clipboard clipboard = reader.read();
            playerSession.setClipboard(new ClipboardHolder(clipboard));
            if (location == null) {
                show(player, clipboard, player.getLocation());
            } else {
                show(player, clipboard, location);
            }
            return true;
        }
    }

    /**
     * @param player    进行动作的玩家
     * @param clipboard 被粘贴的剪贴板
     * @param location  生成区域的位置
     */
    public static void show(Player player, Clipboard clipboard, Location location) {
        LocalSession playerSession = ((WorldEditPlugin) PluginLoaderManager.worldEdit).getSession(player);
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
    }

    /**
     * @return 返回房间名称的列表
     */
    public static ArrayList<String> getRoomNames(String prefix) {
        File folder = new File(plugin.getSchemaFolder().toString());
        ArrayList<String> schema = new ArrayList<>();
        if (!folder.isDirectory()) {
            return null;
        }
        File[] files = folder.listFiles();
        if (files == null) {
            return null;
        }
        if (files.length == 0) {
            return null;
        }
        for (File i : files) {
            if (i.getName().contains(prefix) || Objects.equals(prefix, "all")) {
                schema.add(i.getName().replace(".schema", ""));
            }
        }
        return schema;
    }

    /**
     * @param size -1 随机数最大值
     * @return 返回 [0 , size) 的随机数
     */
    public static int getRandom(int size) {
        Random random = new Random();
        return random.nextInt(size);
    }

    /**
     * @param roomName 被删除的房间的名称
     * @return 删除成功则返回 ture，否则返回 false
     */
    public static boolean deleteRoom(String roomName) {
        File file = new File(plugin.getSchemaFolder(), String.format("%s.schema", roomName));
        if (file.exists()) {
            file.delete();
            return true;
        }
        return false;
    }
}
