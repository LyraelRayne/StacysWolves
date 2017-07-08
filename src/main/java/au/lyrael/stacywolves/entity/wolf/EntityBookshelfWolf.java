package au.lyrael.stacywolves.entity.wolf;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.annotation.WolfSpawn;
import au.lyrael.stacywolves.annotation.WolfSpawnBiome;
import au.lyrael.stacywolves.client.render.IRenderableWolf;
import au.lyrael.stacywolves.item.WolfPeriodicItemDrop;
import au.lyrael.stacywolves.registry.ItemRegistry;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.village.Village;
import net.minecraft.world.World;

import static au.lyrael.stacywolves.entity.SpawnWeights.SPAWN_WEIGHT_COMMON;
import static net.minecraftforge.common.BiomeDictionary.Type.SAVANNA;

@WolfMetadata(name = "EntityBookshelfWolf", primaryColour = 0x254975, secondaryColour = 0xB50F0F,
		spawns = {
				@WolfSpawn(spawnBiomes = {
						// TODO Gotta make them spawn only in village library
						@WolfSpawnBiome(requireBiomeTypes = {SAVANNA}),
				}, weight = SPAWN_WEIGHT_COMMON, min = 1, max = 4),
		})
public class EntityBookshelfWolf extends EntityWolfBase implements IRenderableWolf {

	public EntityBookshelfWolf(World worldObj) {
		super(worldObj);
		addLikedItem(ItemRegistry.getWolfFood("book_bone"));
		setPeriodicDrop(new WolfPeriodicItemDrop(300, 5, new ItemStack(Items.book)));
	}

	@Override
	public boolean getCanSpawnHere() {
		Village nearestVillage = getWorldObj().villageCollectionObj.findNearestVillage(
				MathHelper.floor_double(this.posX),
				MathHelper.floor_double(this.posY),
				MathHelper.floor_double(this.posZ), 8);

		if (nearestVillage == null)
			return false;
		else
			return super.getCanSpawnHere();
	}
	
	@Override
	public EntityWolfBase createChild(EntityAgeable parent) {
		EntityWolfBase child = new EntityBookshelfWolf(this.worldObj);
		return createChild(parent, child);
	}

	@Override
	public String getTextureFolderName() {
		return "bookshelf";
	}
}
