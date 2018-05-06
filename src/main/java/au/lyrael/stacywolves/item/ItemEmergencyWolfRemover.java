package au.lyrael.stacywolves.item;


import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;

import static au.lyrael.stacywolves.StacyWolves.CREATIVE_TAB;

public class ItemEmergencyWolfRemover extends ItemStacyWolves implements IRegisterMyOwnRecipes {

	public static final String WOLF_REMOVER_NAME = "wolf_remover";

	public ItemEmergencyWolfRemover() {
		super();
		setHasSubtypes(false);
		maxStackSize = 1;
		this.setUnlocalizedName(WOLF_REMOVER_NAME);
		setCreativeTab(CREATIVE_TAB);
		this.showTooltipsAlways(true);
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister) {
		super.registerIcons(par1IconRegister);
	}

	public int getColorFromItemStack(ItemStack par1ItemStack, int parColorType) {
		return 0xff0000;
	}

	@Override
	public void registerRecipes() {
	}
}