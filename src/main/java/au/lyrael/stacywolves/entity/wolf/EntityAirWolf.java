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

import static net.minecraftforge.common.BiomeDictionary.Type.MOUNTAIN;

@WolfMetadata(name = "EntityAirWolf", primaryColour = 0xEDECEC, secondaryColour = 0xC9EFF1,
        spawns = {
                @WolfSpawn(spawnBiomes = {
                        @WolfSpawnBiome(requireBiomeTypes = {MOUNTAIN})
                }, probability = 5, min = 1, max = 4),
        })
public class EntityAirWolf extends EntityWolfBase implements IRenderableWolf {

    public EntityAirWolf(World worldObj) {
        super(worldObj);
        addLikedItem(ItemRegistry.getWolfFood("air_bone"));
    }

    @Override
    public EntityWolfBase createChild(EntityAgeable parent) {
        EntityWolfBase child = new EntityAirWolf(this.worldObj);
        return createChild(parent, child);
    }

    @Override
    public String getTextureFolderName() {
        return "air";
    }

    @Override
    public boolean getCanSpawnHere() {
        return this.posY > 95 &&
                super.getCanSpawnHere();
    }

    @Override
    protected boolean isStandingOnSuitableFloor() {
        return true;
    }
}
