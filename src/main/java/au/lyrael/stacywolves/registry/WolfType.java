package au.lyrael.stacywolves.registry;

import au.lyrael.stacywolves.entity.wolf.EntityWolfBase;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraftforge.common.util.EnumHelper;

import java.util.HashMap;
import java.util.Map;

import static net.minecraft.entity.EnumCreatureType.waterCreature;

public enum WolfType {
    NORMAL("normalStacyWolf", 10, Material.air, false, true),
    ORE("oreStacyWolf", 6, Material.air, false, true),
    WATER("waterStacyWolf", 5, Material.water, false, false),
    MOB(EnumCreatureType.monster);

    private final EnumCreatureType creatureType;

    private static final Map<EnumCreatureType, WolfType> reverseMapping = new HashMap<>();

    WolfType(String typeName, int spawnCap, Material spawnMaterial, boolean peaceful, boolean animal) {
        this.creatureType = EnumHelper.addCreatureType(typeName, EntityWolfBase.class, spawnCap, spawnMaterial, peaceful, animal);
    }

    WolfType(EnumCreatureType creatureType) {
        this.creatureType = creatureType;
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
            if(type == wolfType.creatureType())
                return wolfType;
        }
        reverseMapping.put(type, null);
        return null;
    }
}
