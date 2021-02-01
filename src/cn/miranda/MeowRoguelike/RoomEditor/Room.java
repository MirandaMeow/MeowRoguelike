package cn.miranda.MeowRoguelike.RoomEditor;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.InventoryHolder;

import java.io.Serializable;
import java.util.ArrayList;

public class Room implements Serializable {
    public final Location firstLocation;
    public final Location secondLocation;
    private final ArrayList<BlockInfo> blockInfos = new ArrayList<>();
    private final ArrayList<BlockInventory> blockInventories = new ArrayList<>();

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
                        blockInventories.add(new BlockInventory(locationXYZ, inventoryHolder.getInventory().getContents()));
                    }
                    blockInfos.add(new BlockInfo(locationXYZ, blockData));
                    currentBlock.setType(Material.AIR);
                }
            }
        }
    }

    public void showRoom(Location origin) {
        for (BlockInfo i : blockInfos) {
            int x = i.getLocation().getX();
            int y = i.getLocation().getY();
            int z = i.getLocation().getZ();
            Location currentLocation = origin.clone().add(x, y, z);
            currentLocation.getBlock().setBlockData(i.getBlockData());
        }
        for (BlockInventory i : blockInventories) {
            int x = i.getLocation().getX();
            int y = i.getLocation().getY();
            int z = i.getLocation().getZ();
            Location currentLocation = origin.clone().add(x, y, z);
            InventoryHolder inventoryHolder = (InventoryHolder) currentLocation.getBlock().getState();
            inventoryHolder.getInventory().setContents(i.getItemStacks());
            System.out.print(inventoryHolder.getInventory().getContents()[0]);
            //bug
        }
    }
}
