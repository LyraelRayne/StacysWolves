package au.lyrael.stacywolves.registry;

import au.lyrael.stacywolves.entity.wolf.EntityWolfBase;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EnumCreatureType;
import net.minecraftforge.common.util.EnumHelper;

import java.util.HashMap;
import java.util.Map;

public enum WolfType {
    NORMAL("normalStacyWolf", 10, 20 * 2, 200, Material.air, false, false),
    ORE("oreStacyWolf", 6, 20 * 2, 100, Material.air, false, false),
    WATER("waterStacyWolf", 5, 20 / 2 , 100, Material.water, false, false),
    MOB(EnumCreatureType.monster);

    private final EnumCreatureType creatureType;

    private static final Map<EnumCreatureType, WolfType> reverseMapping = new HashMap<>();

    /**
     * Time (in ticks) between allowing this type to be added to a spawn list.
     */
    private final long throttlePeriod;
    /**
     * Chance out of 1000 that this type should spawn at all.
     */
    private final int chanceToSpawn;

    WolfType(String typeName, int spawnCap, long throttlePeriod, int chanceToSpawn, Material spawnMaterial, boolean peaceful, boolean animal) {
        this.creatureType = EnumHelper.addCreatureType(typeName, EntityWolfBase.class, spawnCap, spawnMaterial, peaceful, animal);
        this.throttlePeriod = throttlePeriod;
        this.chanceToSpawn = chanceToSpawn;
    }

    WolfType(EnumCreatureType creatureType) {
        this.creatureType = creatureType;
        this.throttlePeriod = 1;
        this.chanceToSpawn = 1000;
    }

    public EnumCreatureType creatureType() {
        return creatureType;
    }

    public static WolfType valueOf(EnumCreatureType type) {
        if (reverseMapping.containsKey(type))
            return reverseMapping.get(type);
        else {
            return longValueOf(type);
        }
    }

    private static WolfType longValueOf(EnumCreatureType type) {
        for (WolfType wolfType : values()) {
            reverseMapping.put(wolfType.creatureType(), wolfType);
            if (type == wolfType.creatureType())
                return wolfType;
        }
        reverseMapping.put(type, null);
        return null;
    }

    public long getThrottlePeriod() {
        return throttlePeriod;
    }

    public int getChanceToSpawn() {
        return chanceToSpawn;
    }
}
