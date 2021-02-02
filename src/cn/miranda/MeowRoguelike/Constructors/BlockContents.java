package cn.miranda.MeowRoguelike.Constructors;

import org.bukkit.inventory.ItemStack;

import java.io.Serializable;

public class BlockContents implements Serializable {
    private final LocationXYZ locationXYZ;
    private final ItemStack[] contents;

    public BlockContents(LocationXYZ locationXYZ, ItemStack[] contents) {
        this.locationXYZ = locationXYZ;
        this.contents = contents;
    }

    public LocationXYZ getLocation() {
        return this.locationXYZ;
    }

    public ItemStack[] getContents() {
        return this.contents;
    }
}
