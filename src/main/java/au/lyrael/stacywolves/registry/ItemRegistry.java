package au.lyrael.stacywolves.registry;

import au.lyrael.stacywolves.blocks.BlockWolfsbaneTorch;
import au.lyrael.stacywolves.item.ItemWolfFood;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static au.lyrael.stacywolves.StacyWolves.MOD_ID;
import static au.lyrael.stacywolves.blocks.BlockWolfsbaneTorch.WOLFSBANE_TORCH;
import static au.lyrael.stacywolves.item.ItemWolfFood.WOLF_FOOD;

public class ItemRegistry {

    @GameRegistry.ObjectHolder(MOD_ID + ":" + WOLF_FOOD)
    public static final ItemWolfFood wolf_food = null;

    @GameRegistry.ObjectHolder(MOD_ID + ":tile." + WOLFSBANE_TORCH)
    public static final BlockWolfsbaneTorch wolfsbane_torch = null;

    private static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static ItemStack getWolfFood(final String id) {
        return wolf_food.getFood(id);
    }

}
