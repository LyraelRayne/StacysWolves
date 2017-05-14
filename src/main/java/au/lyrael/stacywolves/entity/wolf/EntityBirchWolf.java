package au.lyrael.stacywolves.entity.wolf;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.annotation.WolfSpawn;
import au.lyrael.stacywolves.annotation.WolfSpawnBiome;
import au.lyrael.stacywolves.client.render.IRenderableWolf;
import au.lyrael.stacywolves.registry.ItemRegistry;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.world.World;

import static au.lyrael.stacywolves.entity.SpawnWeights.SPAWN_WEIGHT_COMMON;
import static net.minecraftforge.common.BiomeDictionary.Type.*;

@WolfMetadata(name = "EntityBirchWolf", primaryColour = 0x2F332C, secondaryColour = 0xEEEEE9,
        spawns = {@WolfSpawn(spawnBiomes = {
                @WolfSpawnBiome(requireBiomeTypes = {FOREST}, excludeBiomeTypes = {CONIFEROUS, DENSE, JUNGLE, MOUNTAIN, MESA, END, NETHER, MESA}, excludeBiomeNames = {"Mesa Plateau F M"}),
        }, weight = SPAWN_WEIGHT_COMMON, min = 1, max = 4),
        })
public class EntityBirchWolf extends EntityWolfBase implements IRenderableWolf {

    public EntityBirchWolf(World worldObj) {
        super(worldObj);
        addLikedItem(ItemRegistry.getWolfFood("birch_bone"));
    }

    @Override
    public EntityWolfBase createChild(EntityAgeable parent) {
        EntityWolfBase child = new EntityBirchWolf(this.worldObj);
        return createChild(parent, child);
    }

    @Override
    public String getTextureFolderName() {
        return "birch";
    }

}
