package au.lyrael.needsmoredragons.entity.dragon;

import au.lyrael.needsmoredragons.entity.ai.ITemptable;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;

public interface IDragon extends ITemptable {
    /**
     * @param itemStack Item stack to test for edibility.
     * @return True if the dragon is able to eat {@code itemStack}.
     */
    boolean canEat(ItemStack itemStack);

    /**
     * Tests whether the dragon likes {@code itemStack}. A dragon may be tamed by giving it something it likes.
     *
     * @param itemStack Item stack to test.
     * @return True if {@code itemStack} is a favourite item of dragon's.
     */
    boolean likes(ItemStack itemStack);

    /**
     * @param itemstack An item stack to be fed to the dragon
     * @return The amount of health which will be restored from feeding {@code itemStack} to the dragon.
     */
    float getHealAmount(ItemStack itemstack);

    /**
     * @param itemStack Item stack to test.
     * @return True if consuming {@code itemStack} will cause the animal to fall in love,
     * thus becoming available for breeding.
     */
    boolean isDragonBreedingItem(ItemStack itemStack);

    /**
     * @param potentialMate an animal which the dragon might want to mate with.
     * @return True if the dragon can mate with {@code potentialMate}
     */
    boolean canDragonMateWith(EntityAnimal potentialMate);

    /**
     * @return True if the dragon is shaking off water.
     */
    boolean isDragonShaking();

    boolean isDragonAngry();

    boolean isDragonTamed();
}
