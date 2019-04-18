package au.lyrael.stacywolves.integration;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import static au.lyrael.stacywolves.integration.EtFuturumHolder.prismarine_crystals;


@GameRegistry.ObjectHolder("harvestcraft")
public class PamsHarvestcraftHolder {
    public static final Item blackberryjellysandwichItem = null;

    public static ItemStack getBlackberryJellySandwichItemStack() {
        return blackberryjellysandwichItem != null ? new ItemStack(blackberryjellysandwichItem) : null;
    }
}
