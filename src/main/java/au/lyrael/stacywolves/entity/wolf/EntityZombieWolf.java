package au.lyrael.stacywolves.entity.wolf;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.annotation.WolfSpawn;
import au.lyrael.stacywolves.client.render.IRenderableWolf;
import au.lyrael.stacywolves.registry.ItemRegistry;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import static net.minecraftforge.common.BiomeDictionary.Type.*;

@WolfMetadata(name = "EntityZombieWolf", primaryColour = 0x04AEAE, secondaryColour = 0x447230,
        spawns = {
                @WolfSpawn(biomeTypes = PLAINS, probability = 6, min = 1, max = 4),
                @WolfSpawn(biomeTypes = FOREST, probability = 6, min = 1, max = 4),
                @WolfSpawn(biomeTypes = HILLS, probability = 6, min = 1, max = 4),
        })
public class EntityZombieWolf extends EntityWolfBase implements IRenderableWolf {

    public EntityZombieWolf(World worldObj) {
        super(worldObj);
        addLikedItem(ItemRegistry.getWolfFood("zombie_bone"));
        this.addEdibleItem(new ItemStack(Items.beef));
        this.addEdibleItem(new ItemStack(Items.chicken));
    }

    @Override
    public float getHealAmount(ItemStack itemstack) {
        if (canEat(itemstack))
            return 2F;
        else
            return 0F;
    }

    @Override
    public EntityWolfBase createChild(EntityAgeable parent) {
        EntityWolfBase child = new EntityZombieWolf(this.worldObj);
        String s = this.func_152113_b();

        if (s != null && s.trim().length() > 0) {
            child.func_152115_b(s);
            child.setTamed(true);
        }

        return child;
    }

    @Override
    public String getTextureFolderName() {
        return "zombie";
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
