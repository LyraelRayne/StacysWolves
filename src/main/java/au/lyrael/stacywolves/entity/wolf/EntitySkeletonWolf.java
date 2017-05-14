package au.lyrael.stacywolves.entity.wolf;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.annotation.WolfSpawn;
import au.lyrael.stacywolves.annotation.WolfSpawnBiome;
import au.lyrael.stacywolves.client.render.IRenderableWolf;
import au.lyrael.stacywolves.registry.ItemRegistry;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.world.World;

import static au.lyrael.stacywolves.entity.SpawnWeights.SPAWN_WEIGHT_MOB_COMMON;
import static au.lyrael.stacywolves.registry.WolfType.MOB;
import static net.minecraftforge.common.BiomeDictionary.Type.*;

@WolfMetadata(name = "EntitySkeletonWolf", primaryColour = 0xDBD8D8, secondaryColour = 0x737373, type = MOB,
        spawns = {
                @WolfSpawn(spawnBiomes = {
                        @WolfSpawnBiome(requireBiomeTypes = {PLAINS}),
                        @WolfSpawnBiome(requireBiomeTypes = {MESA}),
                        @WolfSpawnBiome(requireBiomeTypes = {FOREST}),
                        @WolfSpawnBiome(requireBiomeTypes = {MOUNTAIN}),
                        @WolfSpawnBiome(requireBiomeTypes = {HILLS}),
                        @WolfSpawnBiome(requireBiomeTypes = {SWAMP}),
                        @WolfSpawnBiome(requireBiomeTypes = {SANDY}),
                        @WolfSpawnBiome(requireBiomeTypes = {SNOWY}),
                        @WolfSpawnBiome(requireBiomeTypes = {WASTELAND}),
                        @WolfSpawnBiome(requireBiomeTypes = {BEACH}),
                        @WolfSpawnBiome(requireBiomeTypes = {JUNGLE}),
                        @WolfSpawnBiome(requireBiomeTypes = {RIVER}),
                }, weight = SPAWN_WEIGHT_MOB_COMMON, min = 1, max = 4),
        })
public class EntitySkeletonWolf extends EntityWolfBase implements IRenderableWolf {

    public EntitySkeletonWolf(World worldObj) {
        super(worldObj);
        addLikedItem(ItemRegistry.getWolfFood("skeleton_bone"));
    }

    @Override
    public EntityWolfBase createChild(EntityAgeable parent) {
        EntityWolfBase child = new EntitySkeletonWolf(this.worldObj);
        return createChild(parent, child);
    }

    @Override
    public String getTextureFolderName() {
        return "skeleton";
    }

    @Override
    public void onLivingUpdate() {
        if (!this.worldObj.isRemote) {
            this.burnIfInSunlight();
        }

        super.onLivingUpdate();
    }

    @Override
    public boolean canSpawnNow(World world, float x, float y, float z) {
        return !world.isDaytime();
    }
}
