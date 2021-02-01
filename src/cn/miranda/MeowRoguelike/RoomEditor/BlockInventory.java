package cn.miranda.MeowRoguelike.RoomEditor;

import org.bukkit.inventory.ItemStack;

import java.io.Serializable;

public class BlockInventory implements Serializable {
    private final LocationXYZ locationXYZ;
    private final ItemStack[] itemStacks;

    public BlockInventory(LocationXYZ locationXYZ, ItemStack[] itemStacks) {
        this.locationXYZ = locationXYZ;
        this.itemStacks = itemStacks;
    }

    public LocationXYZ getLocation() {
        return this.locationXYZ;
    }

    public ItemStack[] getItemStacks() {
        return this.itemStacks;
    }
}
