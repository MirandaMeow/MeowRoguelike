package cn.miranda.MeowRoguelike.Constructors;

import org.bukkit.block.data.BlockData;

import java.io.Serializable;

public class BlockInfo implements Serializable {
    private final LocationXYZ locationXYZ;
    private final String blockData;


    public BlockInfo(LocationXYZ locationXYZ, BlockData blockData) {
        this.locationXYZ = locationXYZ;
        this.blockData = blockData.getAsString();
    }

    public LocationXYZ getLocation() {
        return locationXYZ;
    }

    public BlockData getBlockData() {
        return org.bukkit.Bukkit.createBlockData(this.blockData);
    }

}
