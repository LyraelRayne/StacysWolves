package au.lyrael.stacywolves.entity.wolf;

import au.lyrael.stacywolves.entity.ai.ITemptable;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;

public interface IWolf extends ITemptable {
    /**
     * @param itemStack Item stack to test for edibility.
     * @return True if the wolf is able to eat {@code itemStack}.
     */
    boolean canEat(ItemStack itemStack);

    /**
     * Tests whether the wolf likes {@code itemStack}. A wolf may be tamed by giving it something it likes.
     *
     * @param itemStack Item stack to test.
     * @return True if {@code itemStack} is a favourite item of wolf's.
     */
    boolean likes(ItemStack itemStack);

    /**
     * @param itemstack An item stack to be fed to the wolf
     * @return The amount of health which will be restored from feeding {@code itemStack} to the wolf.
     */
    float getHealAmount(ItemStack itemstack);

    /**
     * @param itemStack Item stack to test.
     * @return True if consuming {@code itemStack} will cause the animal to fall in love,
     * thus becoming available for breeding.
     */
    boolean isWolfBreedingItem(ItemStack itemStack);

    /**
     * @param potentialMate an animal which the wolf might want to mate with.
     * @return True if the wolf can mate with {@code potentialMate}
     */
    boolean canWolfMateWith(EntityAnimal potentialMate);

    /**
     * @return True if the wolf is shaking off water.
     */
    boolean isWolfShaking();

    boolean isWolfAngry();

    boolean isWolfTamed();
}
