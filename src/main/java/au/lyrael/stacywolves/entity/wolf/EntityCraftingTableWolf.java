package au.lyrael.stacywolves.entity.wolf;

import au.lyrael.stacywolves.StacyWolves;
import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.annotation.WolfSpawn;
import au.lyrael.stacywolves.annotation.WolfSpawnBiome;
import au.lyrael.stacywolves.client.gui.StacysWolvesGuiHandler;
import au.lyrael.stacywolves.client.render.IRenderableWolf;
import au.lyrael.stacywolves.registry.ItemRegistry;
import net.minecraft.client.main.Main;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import static au.lyrael.stacywolves.client.gui.StacysWolvesGuiHandler.GuiIds.WORKBENCH;
import static au.lyrael.stacywolves.entity.SpawnWeights.SPAWN_WEIGHT_RARE;
import static net.minecraftforge.common.BiomeDictionary.Type.FOREST;

@WolfMetadata(name = "EntityCraftingTableWolf", primaryColour = 0xB38F59, secondaryColour = 0x572C19,
		spawns = {
				@WolfSpawn(spawnBiomes = {
						@WolfSpawnBiome(requireBiomeTypes = {FOREST}),
				}, weight = SPAWN_WEIGHT_RARE, min = 1, max = 2),
		})
public class EntityCraftingTableWolf extends EntityWolfBase implements IRenderableWolf {

	public EntityCraftingTableWolf(World worldObj) {
		super(worldObj);
		addLikedItem(ItemRegistry.getWolfFood("crafty_bone"));
	}

	@Override
	public EntityWolfBase createChild(EntityAgeable parent) {
		EntityWolfBase child = new EntityCraftingTableWolf(this.worldObj);
		return createChild(parent, child);
	}

	@Override
	public boolean interact(EntityPlayer player) {
		final boolean didInteract = super.interact(player);
		if (!this.worldObj.isRemote && !didInteract && this.isTamed() && isOwnedBy(player) && player.isSneaking()) {
			int x = (int)player.posX;
			int y = (int)player.posY - 1;
			int z = (int)player.posZ;
			player.openGui(StacyWolves.INSTANCE, WORKBENCH.ordinal(), player.worldObj, x, y, z);
			return true;
		}
		return didInteract;
	}

	@Override
	public String getTextureFolderName() {
		return "crafting_table";
	}
}
