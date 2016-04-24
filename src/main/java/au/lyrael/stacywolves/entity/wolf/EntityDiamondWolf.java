package au.lyrael.stacywolves.entity.wolf;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.annotation.WolfSpawn;
import au.lyrael.stacywolves.client.render.IRenderableWolf;
import au.lyrael.stacywolves.registry.ItemRegistry;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import static au.lyrael.stacywolves.utility.WorldHelper.canSeeTheSky;
import static net.minecraftforge.common.BiomeDictionary.Type.*;

@WolfMetadata(name = "EntityDiamondWolf", primaryColour = 0x7F7F7F, secondaryColour = 0x5DECF5,
        spawns = {
                @WolfSpawn(biomeTypes = PLAINS, probability = 2, min = 1, max = 4),
                @WolfSpawn(biomeTypes = FOREST, probability = 2, min = 1, max = 4),
                @WolfSpawn(biomeTypes = HILLS, probability = 2, min = 1, max = 4),
                @WolfSpawn(biomeTypes = SANDY, probability = 2, min = 1, max = 4),
        })
public class EntityDiamondWolf extends EntityWolfBase implements IRenderableWolf {

    public EntityDiamondWolf(World worldObj) {
        super(worldObj);
        addLikedItem(ItemRegistry.getWolfFood("diamond_bone"));
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
        EntityWolfBase child = new EntityDiamondWolf(this.worldObj);
        String s = this.func_152113_b();

        if (s != null && s.trim().length() > 0) {
            child.func_152115_b(s);
            child.setTamed(true);
        }

        return child;
    }

    @Override
    public String getTextureFolderName() {
        return "diamond";
    }

    @Override
    public boolean getCanSpawnHere() {
        return !canSeeTheSky(getWorldObj(), posX, posY, posZ) && this.posY < 15 && creatureCanSpawnHere();
    }

}
