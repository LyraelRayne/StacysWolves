package au.lyrael.stacywolves.registry;

import au.lyrael.stacywolves.blocks.BlockWolfsbaneTorch;
import au.lyrael.stacywolves.item.*;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static au.lyrael.stacywolves.StacyWolves.MOD_ID;
import static au.lyrael.stacywolves.blocks.BlockWolfsbaneTorch.WOLFSBANE_TORCH_NAME;
import static au.lyrael.stacywolves.item.ItemEmergencyWolfRemover.WOLF_REMOVER_NAME;
import static au.lyrael.stacywolves.item.ItemWolfClicker.WOLF_CLICKER_NAME;
import static au.lyrael.stacywolves.item.ItemWolfFood.WOLF_FOOD_NAME;
import static au.lyrael.stacywolves.item.ItemWolfTransporter.WOLF_TRANSPORTER_NAME;
import static au.lyrael.stacywolves.item.ItemWolfWhistle.WOLF_WHISTLE_NAME;

public class ItemRegistry {

    @GameRegistry.ObjectHolder(MOD_ID + ":" + WOLF_FOOD_NAME)
    public static final ItemWolfFood wolf_food = null;

    @GameRegistry.ObjectHolder(MOD_ID + ":tile." + WOLFSBANE_TORCH_NAME)
    public static final BlockWolfsbaneTorch wolfsbane_torch = null;

    @GameRegistry.ObjectHolder(MOD_ID + ":" + WOLF_TRANSPORTER_NAME)
    public static final ItemWolfTransporter wolf_transporter = null;

    @GameRegistry.ObjectHolder(MOD_ID + ":" + WOLF_CLICKER_NAME)
    public static final ItemWolfClicker wolf_clicker = null;

    @GameRegistry.ObjectHolder(MOD_ID + ":" + WOLF_REMOVER_NAME)
    public static final ItemEmergencyWolfRemover wolf_remover = null;

    @GameRegistry.ObjectHolder(MOD_ID + ":" + WOLF_WHISTLE_NAME)
    public static final ItemWolfWhistle wolf_whistle = null;

    public static ItemStack getWolfFood(final String id) {
        return wolf_food.getFood(id);
    }

    public static boolean isClicker(ItemStack itemstack) {
        return itemstack != null && itemstack.getItem() == wolf_clicker;
    }

    public static boolean isRemover(ItemStack itemstack) {
        return itemstack != null && itemstack.getItem() == wolf_remover;
    }
}
