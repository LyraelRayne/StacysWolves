package au.lyrael.stacywolves.utility;

import au.lyrael.stacywolves.entity.wolf.EntityWolfBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;

public class WolfTransporterUtility {
    public static final String CAPTURED_ENTITY_DETAILS_TAG = "capturedEntityDetails";
    /**
     * Must be "id" because that's what {@link EntityList#createEntityFromNBT} uses
     */
    public static final String WOLF_TYPE_TAG = "id";

    public static boolean containsWolf(ItemStack container) {
        final NBTTagCompound tagCompound = container.getTagCompound();
        return container != null
                && tagCompound != null
                && tagCompound.getCompoundTag(CAPTURED_ENTITY_DETAILS_TAG) != null;
    }

    public static EntityWolfBase loadWolf(ItemStack container, World world) {
        final Entity entityFromNBT = EntityList.createEntityFromNBT(container.getTagCompound().getCompoundTag(CAPTURED_ENTITY_DETAILS_TAG), world);
        if (entityFromNBT instanceof EntityWolfBase)
            return (EntityWolfBase) entityFromNBT;
        else
            return null;
    }
}
