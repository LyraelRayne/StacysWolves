package au.lyrael.stacywolves.entity.wolf;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.annotation.WolfSpawn;
import au.lyrael.stacywolves.annotation.WolfSpawnBiome;
import au.lyrael.stacywolves.client.render.IRenderableWolf;
import au.lyrael.stacywolves.registry.ItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.world.World;

import java.util.List;

import static au.lyrael.stacywolves.registry.WolfType.ORE;
import static au.lyrael.stacywolves.utility.WorldHelper.canSeeTheSky;
import static net.minecraftforge.common.BiomeDictionary.Type.*;

@WolfMetadata(name = "EntityGoldWolf", primaryColour = 0x7F7F7F, secondaryColour = 0xF8AF2B, type = ORE,
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
                }, probability = 7, min = 1, max = 4)
        })
public class EntityGoldWolf extends EntityWolfBase implements IRenderableWolf {

    public EntityGoldWolf(World worldObj) {
        super(worldObj);
        addLikedItem(ItemRegistry.getWolfFood("gold_bone"));
    }

    @Override
    public EntityWolfBase createChild(EntityAgeable parent) {
        EntityWolfBase child = new EntityGoldWolf(this.worldObj);
        return createChild(parent, child);
    }

    @Override
    public String getTextureFolderName() {
        return "gold";
    }

    @Override
    public boolean getCanSpawnHere() {
        return isSuitableDimension()
                && !canSeeTheSky(getWorldObj(), posX, posY, posZ)
                && this.posY < 30
                && creatureCanSpawnHere()
                && isStandingOnSuitableFloor();
    }

    @Override
    public boolean canSpawnNow(World world, float x, float y, float z) {
        return true;
    }

    @Override
    protected List<Block> getFloorBlocks() {
        return ORE_FLOOR_BLOCKS;
    }

}
