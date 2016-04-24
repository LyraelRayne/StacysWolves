package au.lyrael.stacywolves.entity.wolf;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.annotation.WolfSpawn;
import au.lyrael.stacywolves.client.render.IRenderableWolf;
import au.lyrael.stacywolves.registry.ItemRegistry;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import static au.lyrael.stacywolves.utility.WorldHelper.canSeeTheSky;
import static net.minecraft.entity.EnumCreatureType.waterCreature;
import static net.minecraftforge.common.BiomeDictionary.Type.BEACH;
import static net.minecraftforge.common.BiomeDictionary.Type.OCEAN;

@WolfMetadata(name = "EntityWaterWolf", primaryColour = 0xDDD9DA, secondaryColour = 0x91C5B7,
        spawns = {
                @WolfSpawn(biomeTypes = OCEAN, probability = 5, min = 1, max = 4),
                @WolfSpawn(biomeTypes = BEACH, probability = 5, min = 1, max = 4),
        })
public class EntityWaterWolf extends EntityWolfBase implements IRenderableWolf {

    public EntityWaterWolf(World worldObj) {
        super(worldObj);
        addLikedItem(ItemRegistry.getWolfFood("water_bone"));
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
        EntityWolfBase child = new EntityWaterWolf(this.worldObj);
        String s = this.func_152113_b();

        if (s != null && s.trim().length() > 0) {
            child.func_152115_b(s);
            child.setTamed(true);
        }

        return child;
    }

      @Override
    public boolean canSpawnHereAndNow(World world, float x, float y, float z) {
        return world.isDaytime();
    }

    @Override
    public String getTextureFolderName() {
        return "water";
    }

    @Override
    public boolean normallyAvoidsWater() {
        return false;
    }
}
