package au.lyrael.stacywolves.entity.wolf;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.annotation.WolfSpawn;
import au.lyrael.stacywolves.annotation.WolfSpawnBiome;
import au.lyrael.stacywolves.client.render.IRenderableWolf;
import au.lyrael.stacywolves.registry.ItemRegistry;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import static au.lyrael.stacywolves.entity.SpawnWeights.SPAWN_WEIGHT_COMMON;
import static net.minecraftforge.common.BiomeDictionary.Type.PLAINS;

@WolfMetadata(name = "EntityCowWolf", primaryColour = 0xA0A0A0, secondaryColour = 0x3E2F23,
		spawns = {
				@WolfSpawn(spawnBiomes = {
						@WolfSpawnBiome(requireBiomeTypes = {PLAINS}),
				}, weight = SPAWN_WEIGHT_COMMON, min = 1, max = 4),
		})
public class EntityCowWolf extends EntityWolfBase implements IRenderableWolf {

	public EntityCowWolf(World worldObj) {
		super(worldObj);
		addLikedItem(ItemRegistry.getWolfFood("wheat_bone"));
	}

	@Override
	public EntityWolfBase createChild(EntityAgeable parent) {
		EntityWolfBase child = new EntityCowWolf(this.worldObj);
		return createChild(parent, child);
	}

	@Override
	public boolean interact(EntityPlayer player) {
		ItemStack itemstack = player.inventory.getCurrentItem();
		if (this.isTamed() && isOwnedBy(player) && itemstack != null && itemstack.getItem() == Items.bucket && !player.capabilities.isCreativeMode) {
			if (itemstack.stackSize-- == 1) {
				player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(Items.milk_bucket));
			} else if (!player.inventory.addItemStackToInventory(new ItemStack(Items.milk_bucket))) {
				player.dropPlayerItemWithRandomChoice(new ItemStack(Items.milk_bucket, 1, 0), false);
			}

			return true;
		} else {
			return super.interact(player);
		}
	}

	@Override
	public String getTextureFolderName() {
		return "cow";
	}
}
