package au.lyrael.stacywolves.integration;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;


@GameRegistry.ObjectHolder("etfuturum")
public class EtFuturumHolder {
    public static final Block prismarine_block = null;
    public static final Item prismarine_crystals = null;

    public static ItemStack getPrismarineCrystalItemStack() {
        return prismarine_crystals != null ? new ItemStack(prismarine_crystals) : null;
    }
}
