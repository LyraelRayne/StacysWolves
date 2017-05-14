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

@WolfMetadata(name = "EntityEarthWolf", primaryColour = 0xB8845B, secondaryColour = 0x583C28,
        spawns = {
                @WolfSpawn(spawnBiomes = {
                        @WolfSpawnBiome(requireBiomeTypes = {PLAINS}, excludeBiomeTypes = {MOUNTAIN, SANDY, NETHER, END, COLD, SAVANNA}),
                        @WolfSpawnBiome(requireBiomeTypes = {FOREST}, excludeBiomeTypes = {MOUNTAIN, SANDY, NETHER, END, COLD, SAVANNA}),
                        @WolfSpawnBiome(requireBiomeTypes = {HILLS}, excludeBiomeTypes = {MOUNTAIN, SANDY, NETHER, END, COLD, SAVANNA}),
                }, weight = SPAWN_WEIGHT_COMMON, min = 1, max = 4),
        })
public class EntityEarthWolf extends EntityWolfBase implements IRenderableWolf {

    public EntityEarthWolf(World worldObj) {
        super(worldObj);
        addLikedItem(ItemRegistry.getWolfFood("earth_bone"));
    }

    @Override
    public EntityWolfBase createChild(EntityAgeable parent) {
        EntityWolfBase child = new EntityEarthWolf(this.worldObj);
        return createChild(parent, child);
    }

    @Override
    public String getTextureFolderName() {
        return "earth";
    }
}
