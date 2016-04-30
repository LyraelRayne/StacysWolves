package au.lyrael.stacywolves.entity.wolf;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.annotation.WolfSpawn;
import au.lyrael.stacywolves.annotation.WolfSpawnBiome;
import au.lyrael.stacywolves.client.render.IRenderableWolf;
import au.lyrael.stacywolves.registry.ItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

import static net.minecraftforge.common.BiomeDictionary.Type.END;
import static net.minecraftforge.common.BiomeDictionary.Type.NETHER;

@WolfMetadata(name = "EntityNetherWolf", primaryColour = 0x830D0D, secondaryColour = 0xD55910,
        spawns = {
                @WolfSpawn(spawnBiomes = {
                        @WolfSpawnBiome(requireBiomeTypes = {NETHER}, excludeBiomeTypes = {END}),
                }, probability = 10, min = 1, max = 4),
        })
public class EntityNetherWolf extends EntityWolfBase implements IRenderableWolf {

    public EntityNetherWolf(World worldObj) {
        super(worldObj);
        addLikedItem(ItemRegistry.getWolfFood("nether_bone"));
        this.addEdibleItem(new ItemStack(Items.beef));
        this.addEdibleItem(new ItemStack(Items.chicken));
    }

    @Override
    public EntityWolfBase createChild(EntityAgeable parent) {
        EntityWolfBase child = new EntityNetherWolf(this.worldObj);
        return createChild(parent, child);
    }


    @Override
    public boolean getCanSpawnHere() {
        return livingCanSpawnHere();
    }

    @Override
    public String getTextureFolderName() {
        return "nether";
    }

    @Override
    public boolean canSpawnNow(World world, float x, float y, float z) {
        return true;
    }
}
