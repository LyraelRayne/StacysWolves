package au.lyrael.stacywolves.entity.wolf;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.annotation.WolfSpawn;
import au.lyrael.stacywolves.client.render.IRenderableWolf;
import au.lyrael.stacywolves.registry.ItemRegistry;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.village.Village;
import net.minecraft.world.World;

import static net.minecraft.entity.EnumCreatureType.creature;
import static net.minecraftforge.common.BiomeDictionary.Type.*;

@WolfMetadata(name = "EntityCakeWolf", primaryColour = 0xB35922, secondaryColour = 0xE41717,
        spawns = {
                @WolfSpawn(biomeTypes = {PLAINS}, probability = 1, min = 1, max = 2, creatureType = creature),
                @WolfSpawn(biomeTypes = {SAVANNA}, probability = 1, min = 1, max = 2, creatureType = creature),
                @WolfSpawn(biomeTypes = {SANDY, HOT, DRY}, probability = 1, min = 1, max = 2, creatureType = creature),
        })
public class EntityCakeWolf extends EntityWolfBase implements IRenderableWolf {

    public EntityCakeWolf(World worldObj) {
        super(worldObj);
        addLikedItem(ItemRegistry.getWolfFood("cake_bone"));
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
    public boolean getCanSpawnHere() {
        Village nearestVillage = getWorldObj().villageCollectionObj.findNearestVillage(
                MathHelper.floor_double(this.posX),
                MathHelper.floor_double(this.posY),
                MathHelper.floor_double(this.posZ), 8);

        if (nearestVillage == null)
            return false;
        else
            return creatureCanSpawnHere();
    }

    @Override
    public EntityWolfBase createChild(EntityAgeable parent) {
        EntityWolfBase child = new EntityCakeWolf(this.worldObj);
        String s = this.func_152113_b();

        if (s != null && s.trim().length() > 0) {
            child.func_152115_b(s);
            child.setTamed(true);
        }

        return child;
    }

    @Override
    public String getTextureFolderName() {
        return "cake";
    }
}
