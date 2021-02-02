package cn.miranda.MeowRoguelike.Constructors;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

import java.io.Serializable;
import java.util.ArrayList;

public class Room implements Serializable {
    public final Location firstLocation;
    public final Location secondLocation;
    private final ArrayList<BlockInfo> blockInfos = new ArrayList<>();
    private final ArrayList<BlockContents> blockContents = new ArrayList<>();
    private final ArrayList<BlockEntity> blockEntities = new ArrayList<>();

    public Room(Location firstLocation, Location secondLocation) {
        this.firstLocation = firstLocation;
        this.secondLocation = secondLocation;
        double temp;
        double fx = firstLocation.getX();
        double fy = firstLocation.getY();
        double fz = firstLocation.getZ();
        double sx = secondLocation.getX();
        double sy = secondLocation.getY();
        double sz = secondLocation.getZ();
        if (fx > sx) {
            temp = fx;
            fx = sx;
            sx = temp;
        }
        if (fy > sy) {
            temp = fy;
            fy = sy;
            sy = temp;
        }
        if (fz > sz) {
            temp = fz;
            fz = sz;
            sz = temp;
        }
        int tempY = -1;
        for (double currentY = fy; currentY <= sy; currentY++) {
            tempY++;
            int tempZ = -1;
            for (double currentZ = fz; currentZ <= sz; currentZ++) {
                tempZ++;
                int tempX = -1;
                for (double currentX = fx; currentX <= sx; currentX++) {
                    tempX++;
                    Location currentLocation = new Location(firstLocation.getWorld(), currentX, currentY, currentZ);
                    Block currentBlock = currentLocation.getBlock();
                    BlockData blockData = currentBlock.getBlockData();
                    if (blockData.getMaterial().equals(Material.AIR)) {
                        continue;
                    }
                    LocationXYZ locationXYZ = new LocationXYZ(tempX, tempY, tempZ);
                    if (currentBlock.getState() instanceof InventoryHolder) {
                        InventoryHolder inventoryHolder = (InventoryHolder) currentBlock.getState();
                        blockContents.add(new BlockContents(locationXYZ, inventoryHolder.getInventory().getContents()));
                        inventoryHolder.getInventory().clear();
                    }
                    blockInfos.add(new BlockInfo(locationXYZ, blockData));
                    currentBlock.setType(Material.AIR);
                }
            }
        }
        ArrayList<Entity> entities = checkForEntity(firstLocation, secondLocation);
        for (Entity i : entities) {
            Location tempLocation = i.getLocation().subtract(firstLocation);
            LocationXYZ locationXYZ = new LocationXYZ(((int) tempLocation.getX()), ((int) tempLocation.getY()), ((int) tempLocation.getZ()));
            blockEntities.add(new BlockEntity(locationXYZ, i.getType()));
            i.remove();
        }
    }

    private ArrayList<Entity> checkForEntity(Location firstLocation, Location secondLocation) {
        double fx = firstLocation.getX();
        double fy = firstLocation.getY();
        double fz = firstLocation.getZ();
        double sx = secondLocation.getX();
        double sy = secondLocation.getY();
        double sz = secondLocation.getZ();
        Location middle = new Location(firstLocation.getWorld(), (fx + sx) / 2, (fy + sy) / 2, (fz + sz) / 2);
        ArrayList<Entity> entities = (ArrayList<Entity>) firstLocation.getWorld().getNearbyEntities(middle, 12.5, 7.5, 12.5);
        for (int i = entities.size() - 1; i >= 0; i--) {
            if (entities.get(i) instanceof Player || entities.get(i) instanceof Item) {
                entities.remove(i);
            }
        }
        return entities;
    }

    public void showRoom(Location origin) {
        for (BlockInfo i : blockInfos) {
            int x = i.getLocation().getX();
            int y = i.getLocation().getY();
            int z = i.getLocation().getZ();
            Location currentLocation = origin.clone().add(x, y, z);
            currentLocation.getBlock().setBlockData(i.getBlockData());
        }
        for (BlockContents i : blockContents) {
            if (i.getContents().length != 27) {
                continue;
            }
            int x = i.getLocation().getX();
            int y = i.getLocation().getY();
            int z = i.getLocation().getZ();
            Location currentLocation = origin.clone().add(x, y, z);
            InventoryHolder inventoryHolder = (InventoryHolder) currentLocation.getBlock().getState();
            inventoryHolder.getInventory().setContents(i.getContents());
            currentLocation.getBlock().getState().update();
        }
        for (BlockEntity i : blockEntities) {
            int x = i.getLocation().getX();
            int y = i.getLocation().getY();
            int z = i.getLocation().getZ();
            Location currentLocation = origin.clone().add(x, y, z);
            currentLocation.getWorld().spawnEntity(currentLocation, i.getEntityType());
        }
    }
}
