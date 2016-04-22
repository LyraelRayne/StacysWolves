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

@WolfMetadata(name = "EntityFireWolf", primaryColour = 0xFF0000, secondaryColour = 0xFFFF00,
        spawns = {
                @WolfSpawn(biomeType = {HOT, DRY}, probability = 5, min = 1, max = 4),
                @WolfSpawn(biomeType = PLAINS, probability = 20, min = 1, max = 1),
        })
public class EntityFireWolf extends EntityWolfBase implements IRenderableWolf {

    public EntityFireWolf(World worldObj) {
        super(worldObj);
        addEdibleItem(ItemRegistry.getWolfFood("fire_bone"));
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
        EntityWolfBase child = new EntityFireWolf(this.worldObj);
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
