package au.lyrael.needsmoredragons.entity.dragon;

import au.lyrael.needsmoredragons.annotation.DragonMetadata;
import au.lyrael.needsmoredragons.annotation.DragonSpawn;
import au.lyrael.needsmoredragons.client.render.IRenderableDragon;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import static net.minecraftforge.common.BiomeDictionary.Type.*;

@DragonMetadata(name = "EntityFireDragon", primaryColour = 0xFF0000, secondaryColour = 0xFFFF00,
        spawns = {
                @DragonSpawn(biomeType = {HOT, DRY}, probability = 5, min = 1, max = 4),
                @DragonSpawn(biomeType = PLAINS, probability = 20, min = 1, max = 1),
        })
public class EntityFireDragon extends EntityDragonBase implements IRenderableDragon {

    public EntityFireDragon(World worldObj) {
        super(worldObj);
        addEdibleItem(new ItemStack(Items.blaze_rod));
        addEdibleItem(new ItemStack(Items.blaze_powder));
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
        EntityDragonBase child = new EntityFireDragon(this.worldObj);
        String s = this.func_152113_b();

        if (s != null && s.trim().length() > 0) {
            child.func_152115_b(s);
            child.setTamed(true);
        }

        return child;
    }

    @Override
    public String getTextureFolderName() {
        return "fire";
    }
}
