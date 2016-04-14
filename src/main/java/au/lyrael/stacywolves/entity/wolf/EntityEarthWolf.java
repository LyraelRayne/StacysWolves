package au.lyrael.stacywolves.entity.wolf;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.annotation.WolfSpawn;
import au.lyrael.stacywolves.client.render.IRenderableWolf;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import static net.minecraftforge.common.BiomeDictionary.Type.*;

@WolfMetadata(name = "EntityEarthWolf", primaryColour = 0x00FF00, secondaryColour = 0x55FF55,
        spawns = {
                @WolfSpawn(biomeType = PLAINS, probability = 20, min = 1, max = 1),
                @WolfSpawn(biomeType = HILLS, probability = 5, min = 1, max = 2),
        })
public class EntityEarthWolf extends EntityWolfBase implements IRenderableWolf {

    public EntityEarthWolf(World worldObj) {
        super(worldObj);
        addEdibleItem(new ItemStack(Blocks.grass));
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
        EntityWolfBase child = new EntityEarthWolf(this.worldObj);
        String s = this.func_152113_b();

        if (s != null && s.trim().length() > 0) {
            child.func_152115_b(s);
            child.setTamed(true);
        }

        return child;
    }

    @Override
    public String getTextureFolderName() {
        return "earth";
    }
}
