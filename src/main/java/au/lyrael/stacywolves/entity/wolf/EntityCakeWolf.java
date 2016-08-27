package au.lyrael.stacywolves.entity.wolf;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.client.render.IRenderableWolf;
import au.lyrael.stacywolves.registry.ItemRegistry;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.village.Village;
import net.minecraft.world.World;

@WolfMetadata(name = "EntityCakeWolf", primaryColour = 0xB35922, secondaryColour = 0xE41717)
public class EntityCakeWolf extends EntityWolfBase implements IRenderableWolf, IWolf {

    public EntityCakeWolf(World worldObj) {
        super(worldObj);
        addLikedItem(ItemRegistry.getWolfFood("cake_bone"));
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
            return super.getCanSpawnHere();
    }

    @Override
    public EntityWolfBase createChild(EntityAgeable parent) {
        EntityWolfBase child = new EntityCakeWolf(this.worldObj);
        return createChild(parent, child);
    }

    @Override
    public String getTextureFolderName() {
        return "cake";
    }
}
