package au.lyrael.needsmoredragons.entity.dragon;

import au.lyrael.needsmoredragons.annotation.DragonMetadata;
import au.lyrael.needsmoredragons.annotation.DragonSpawn;
import au.lyrael.needsmoredragons.client.render.IRenderableDragon;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import static net.minecraftforge.common.BiomeDictionary.Type.*;

@DragonMetadata(name = "EntityEarthDragon", primaryColour = 0x00FF00, secondaryColour = 0x55FF55,
        spawns = {
                @DragonSpawn(biomeType = PLAINS, probability = 20, min = 1, max = 1),
                @DragonSpawn(biomeType = HILLS, probability = 5, min = 1, max = 2),
        })
public class EntityEarthDragon extends EntityDragonBase implements IRenderableDragon {

    public EntityEarthDragon(World worldObj) {
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
    public EntityDragonBase createChild(EntityAgeable parent) {
        EntityDragonBase child = new EntityEarthDragon(this.worldObj);
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
