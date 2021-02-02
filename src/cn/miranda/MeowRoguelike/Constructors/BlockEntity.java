package cn.miranda.MeowRoguelike.Constructors;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.io.Serializable;

public class BlockEntity implements Serializable {
    private final LocationXYZ locationXYZ;
    private final EntityType entityType;

    public BlockEntity(LocationXYZ locationXYZ, EntityType entityType) {
        this.locationXYZ = locationXYZ;
        this.entityType = entityType;
    }

    public LocationXYZ getLocation() {
        return locationXYZ;
    }

    public EntityType getEntityType() {
        return entityType;
    }
}
