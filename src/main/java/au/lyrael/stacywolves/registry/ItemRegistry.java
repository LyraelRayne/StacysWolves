package au.lyrael.stacywolves.registry;

import au.lyrael.stacywolves.blocks.BlockWolfsbaneTorch;
import au.lyrael.stacywolves.item.ItemWolfFood;
import au.lyrael.stacywolves.item.ItemWolfTransporter;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static au.lyrael.stacywolves.StacyWolves.MOD_ID;
import static au.lyrael.stacywolves.blocks.BlockWolfsbaneTorch.WOLFSBANE_TORCH_NAME;
import static au.lyrael.stacywolves.item.ItemWolfFood.WOLF_FOOD_NAME;
import static au.lyrael.stacywolves.item.ItemWolfTransporter.WOLF_TRANSPORTER_NAME;

public class ItemRegistry {

    @GameRegistry.ObjectHolder(MOD_ID + ":" + WOLF_FOOD_NAME)
    public static final ItemWolfFood wolf_food = null;

    @GameRegistry.ObjectHolder(MOD_ID + ":tile." + WOLFSBANE_TORCH_NAME)
    public static final BlockWolfsbaneTorch wolfsbane_torch = null;

    @GameRegistry.ObjectHolder(MOD_ID + ":" + WOLF_TRANSPORTER_NAME)
    public static final ItemWolfTransporter wolf_transporter = null;

    private static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static ItemStack getWolfFood(final String id) {
        return wolf_food.getFood(id);
    }

}
