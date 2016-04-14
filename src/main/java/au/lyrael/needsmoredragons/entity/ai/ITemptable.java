package au.lyrael.needsmoredragons.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Created by Lyrael on 25/03/2016.
 */
public interface ITemptable {

    /**
     * Tests whether the dragon is tempted by {@code itemStack}. If tempted the dragon will follow players holding
     * the item. Used to prime the {@link EntityAIDragonTempt} AI routines.
     *
     * @param itemStack Item stack to test.
     * @return True if the dragon is tempted by this item.
     */
    boolean isTemptedBy(ItemStack itemStack);

    World getWorldObj();

    EntityLiving asEntityLiving();

    /**
     * @return True if the temptable entity doesn't normally go into water. Can be coaxed if not {@link #alwaysAvoidsWater()}
     */
    boolean normallyAvoidsWater();

    /**
     * @return True if the temptable entity doesn't EVER go into water, even when tempted.
     */
    boolean alwaysAvoidsWater();

}
