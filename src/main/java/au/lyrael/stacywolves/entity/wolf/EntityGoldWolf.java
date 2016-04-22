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
import static net.minecraftforge.common.BiomeDictionary.Type.DRY;
import static net.minecraftforge.common.BiomeDictionary.Type.HOT;

@WolfMetadata(name = "EntityGoldWolf", primaryColour = 0x7F7F7F, secondaryColour = 0xF8AF2B,
        spawns = {
                @WolfSpawn(biomeTypes = {HOT, DRY}, probability = 5, min = 1, max = 4),
        })
public class EntityGoldWolf extends EntityWolfBase implements IRenderableWolf {

    public EntityGoldWolf(World worldObj) {
        super(worldObj);
        addLikedItem(ItemRegistry.getWolfFood("gold_bone"));
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
        EntityWolfBase child = new EntityGoldWolf(this.worldObj);
        String s = this.func_152113_b();

        if (s != null && s.trim().length() > 0) {
            child.func_152115_b(s);
            child.setTamed(true);
        }

        return child;
    }

    @Override
    public String getTextureFolderName() {
        return "gold";
    }

    @Override
    public boolean canSpawnHereAndNow(World world, float x, float y, float z) {
        return !canSeeTheSky(world, x, y, z) && y < 30;
    }
}
