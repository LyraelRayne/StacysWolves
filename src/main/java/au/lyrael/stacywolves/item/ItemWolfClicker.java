package au.lyrael.stacywolves.item;


import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static au.lyrael.stacywolves.StacyWolves.CREATIVE_TAB;

public class ItemWolfClicker extends ItemStacyWolves implements IRegisterMyOwnRecipes {

	public static final String WOLF_CLICKER_NAME = "wolf_clicker";

	public ItemWolfClicker() {
		super();
		setHasSubtypes(false);
		maxStackSize = 1;
		this.setUnlocalizedName(WOLF_CLICKER_NAME);
		setCreativeTab(CREATIVE_TAB);
		this.showTooltipsAlways(true);
	}

// TODO Make a JSON for texture

	@Override
	public void registerRecipes() {
		GameRegistry.addRecipe(new ItemStack(this), new Object[]{" S ", "SRS", "SSS", 'R', Items.redstone, 'S', Blocks.stone});
	}
}