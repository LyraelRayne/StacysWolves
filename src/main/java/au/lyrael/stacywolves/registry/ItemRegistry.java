package au.lyrael.stacywolves.registry;

import au.lyrael.stacywolves.item.ItemWolfFood;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static au.lyrael.stacywolves.StacyWolves.MOD_ID;

public class ItemRegistry {

    @GameRegistry.ObjectHolder(MOD_ID + ":wolf_food")
    public static final ItemWolfFood wolf_food = null;

    private static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static ItemStack getWolfFood(final String id)
    {
        return wolf_food.getFood(id);
    }

}
