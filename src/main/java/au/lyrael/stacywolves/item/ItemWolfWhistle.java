package au.lyrael.stacywolves.item;


import au.lyrael.stacywolves.entity.wolf.EntityWolfBase;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static au.lyrael.stacywolves.StacyWolves.CREATIVE_TAB;
import static au.lyrael.stacywolves.StacyWolves.MOD_ID;

public class ItemWolfWhistle extends ItemStacyWolves implements IRegisterMyOwnRecipes {
	private static final Logger LOGGER = LogManager.getLogger(MOD_ID);


	public static final String WOLF_WHISTLE_NAME = "wolf_whistle";

	public ItemWolfWhistle() {
		super();
		setHasSubtypes(false);
		maxStackSize = 1;
		this.setUnlocalizedName(WOLF_WHISTLE_NAME);
		setCreativeTab(CREATIVE_TAB);
		this.showTooltipsAlways(true);
	}

	@Override
	public void registerIcons(IIconRegister iconRegister) {
		final String unlocalizedName = unwrapUnlocalizedName(this.getUnlocalizedName());
		this.itemIcon = iconRegister.registerIcon(String.format("%s/%s", unlocalizedName, WOLF_WHISTLE_NAME));
	}

	@Override
	public void registerRecipes() {
		GameRegistry.addRecipe(new ItemStack(this), new Object[]{" B ", "BRB", "BBB", 'R', Items.redstone, 'B', Items.bone});
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is pressed.
	 * Args: itemStack, world, entityPlayer
	 */
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world,
									  EntityPlayer player) {
		if (world.isRemote || !WorldServer.class.isAssignableFrom(world.getClass())) {
			return stack;
		} else {
			try {
				final List entities = world.getLoadedEntityList();
				for (Object entity : entities) {
					if(EntityWolfBase.class.isAssignableFrom(entity.getClass())) {
						final EntityWolfBase wolf = (EntityWolfBase) entity;
						wolf.respondToWhistle(player);
					}
				}
			} catch (Exception e) {
				LOGGER.error("Failed to call wolves", e);
			}
			return stack;
		}
	}
}