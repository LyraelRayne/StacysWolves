package au.lyrael.stacywolves.item;


import au.lyrael.stacywolves.entity.wolf.EntityWolfBase;
import au.lyrael.stacywolves.registry.WolfType;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.ForgeEventFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

import static au.lyrael.stacywolves.StacyWolves.CREATIVE_TAB;
import static au.lyrael.stacywolves.StacyWolves.MOD_ID;
import static net.minecraft.world.SpawnerAnimals.canCreatureTypeSpawnAtLocation;

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

	@Override
	public void registerIcons(IIconRegister iconRegister) {
		final String unlocalizedName = unwrapUnlocalizedName(this.getUnlocalizedName());
		this.itemIcon = iconRegister.registerIcon(String.format("%s/%s", unlocalizedName, WOLF_CLICKER_NAME));
	}

	@Override
	public void registerRecipes() {
		GameRegistry.addRecipe(new ItemStack(this), new Object[]{" S ", "SRS", "SSS", 'R', Items.redstone, 'S', Blocks.stone});
	}
}