package au.lyrael.stacywolves.entity.wolf;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.annotation.WolfSpawn;
import au.lyrael.stacywolves.annotation.WolfSpawnBiome;
import au.lyrael.stacywolves.client.render.IRenderableWolf;
import au.lyrael.stacywolves.registry.ItemRegistry;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import static au.lyrael.stacywolves.utility.WorldHelper.canSeeTheSky;
import static net.minecraftforge.common.BiomeDictionary.Type.*;

@WolfMetadata(name = "EntityDiamondWolf", primaryColour = 0x7F7F7F, secondaryColour = 0x5DECF5,
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
                }, probability = 2, min = 1, max = 4)
        })
public class EntityDiamondWolf extends EntityWolfBase implements IRenderableWolf {

    public EntityDiamondWolf(World worldObj) {
        super(worldObj);
        addLikedItem(ItemRegistry.getWolfFood("diamond_bone"));
        this.addEdibleItem(new ItemStack(Items.beef));
        this.addEdibleItem(new ItemStack(Items.chicken));
    }

    @Override
    public EntityWolfBase createChild(EntityAgeable parent) {
        EntityWolfBase child = new EntityDiamondWolf(this.worldObj);
        return createChild(parent, child);
    }

    @Override
    public String getTextureFolderName() {
        return "diamond";
    }

    @Override
    public boolean getCanSpawnHere() {
        return !canSeeTheSky(getWorldObj(), posX, posY, posZ) && this.posY < 15 && creatureCanSpawnHere();
    }

    @Override
    public boolean canSpawnNow(World world, float x, float y, float z) {
        return true;
    }

}
