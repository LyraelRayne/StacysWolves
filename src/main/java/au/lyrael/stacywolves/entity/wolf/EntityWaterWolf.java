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

import static net.minecraftforge.common.BiomeDictionary.Type.BEACH;
import static net.minecraftforge.common.BiomeDictionary.Type.OCEAN;

@WolfMetadata(name = "EntityWaterWolf", primaryColour = 0xDDD9DA, secondaryColour = 0x91C5B7,
        spawns = {
                @WolfSpawn(spawnBiomes = {
                        @WolfSpawnBiome(requireBiomeTypes = {OCEAN}),
                        @WolfSpawnBiome(requireBiomeTypes = {BEACH}),
                }, probability = 5, min = 1, max = 4),
        })
public class EntityWaterWolf extends EntityWolfBase implements IRenderableWolf {

    public EntityWaterWolf(World worldObj) {
        super(worldObj);
        addLikedItem(ItemRegistry.getWolfFood("water_bone"));
        this.addEdibleItem(new ItemStack(Items.beef));
        this.addEdibleItem(new ItemStack(Items.chicken));
    }

    @Override
    public EntityWolfBase createChild(EntityAgeable parent) {
        EntityWolfBase child = new EntityWaterWolf(this.worldObj);
        return createChild(parent, child);
    }

    @Override
    public boolean isPushedByWater() {
        return false;
    }

    @Override
    public boolean canBreatheUnderwater()
    {
        return true;
    }

    @Override
    public boolean canSpawnNow(World world, float x, float y, float z) {
        return world.isDaytime();
    }

    @Override
    public String getTextureFolderName() {
        return "water";
    }

    @Override
    public boolean normallyAvoidsWater() {
        return false;
    }

    @Override
    public boolean alwaysAvoidsWater() {
        return false;
    }
}
