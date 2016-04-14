package au.lyrael.stacywolves.entity.wolf;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.annotation.WolfSpawn;
import au.lyrael.stacywolves.client.render.IRenderableWolf;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import static net.minecraftforge.common.BiomeDictionary.Type.*;

@WolfMetadata(name = "EntityWaterWolf", primaryColour = 0x0000FF, secondaryColour = 0xFF00FF,
        spawns = {
                @WolfSpawn(biomeType = WET, probability = 5, min = 1, max = 4),
                @WolfSpawn(biomeType = PLAINS, probability = 20, min = 1, max = 1),
        })
public class EntityWaterWolf extends EntityWolfBase implements IRenderableWolf {

    public EntityWaterWolf(World worldObj) {
        super(worldObj);
        addEdibleItem(new ItemStack(Items.cooked_fished));
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
        EntityWolfBase child = new EntityWaterWolf(this.worldObj);
        String s = this.func_152113_b();

        if (s != null && s.trim().length() > 0) {
            child.func_152115_b(s);
            child.setTamed(true);
        }

        return child;
    }

    @Override
    public String getTextureFolderName() {
        return "water";
    }
}
