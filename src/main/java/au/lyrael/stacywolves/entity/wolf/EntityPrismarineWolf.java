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

@WolfMetadata(name = "EntityPrismarineWolf", primaryColour = 0x42689B, secondaryColour = 0x68516F,
        spawns = {
                @WolfSpawn(biomeTypes = {HOT, DRY}, probability = 5, min = 1, max = 4),
                @WolfSpawn(biomeTypes = PLAINS, probability = 20, min = 1, max = 1),
        })
public class EntityPrismarineWolf extends EntityWolfBase implements IRenderableWolf {

    public EntityPrismarineWolf(World worldObj) {
        super(worldObj);
        addLikedItem(ItemRegistry.getWolfFood("prismarine_bone"));
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
        EntityWolfBase child = new EntityPrismarineWolf(this.worldObj);
        String s = this.func_152113_b();

        if (s != null && s.trim().length() > 0) {
            child.func_152115_b(s);
            child.setTamed(true);
        }

        return child;
    }

    @Override
    public String getTextureFolderName() {
        return "prismarine";
    }
}