package au.lyrael.stacywolves.integration;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;


@GameRegistry.ObjectHolder("etfuturum")
public class EtFuturumHolder {
    public static final Block prismarine_block = null;
    public static final Item prismarine_crystals = null;
    public static final Item prismarine_shard = null;

    public static ItemStack getPrismarineCrystalItemStack() {
        return prismarine_crystals != null ? new ItemStack(prismarine_crystals) : null;
    }

    public static ItemStack getPrismarineShardItemStack() {
        return prismarine_shard != null ? new ItemStack(prismarine_shard) : null;
    }
}
