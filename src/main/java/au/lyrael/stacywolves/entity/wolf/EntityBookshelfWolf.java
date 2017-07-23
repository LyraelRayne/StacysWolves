package au.lyrael.stacywolves.entity.wolf;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.client.render.IRenderableWolf;
import au.lyrael.stacywolves.item.WolfPeriodicItemDrop;
import au.lyrael.stacywolves.registry.ItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.village.Village;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static au.lyrael.stacywolves.StacyWolves.MOD_ID;
import static au.lyrael.stacywolves.entity.SpawnWeights.SPAWN_PROBABILITY_ALWAYS;
import static au.lyrael.stacywolves.utility.WorldHelper.canSeeTheSky;

@WolfMetadata(name = "EntityBookshelfWolf", primaryColour = 0x254975, secondaryColour = 0xB50F0F, probability = SPAWN_PROBABILITY_ALWAYS)
public class EntityBookshelfWolf extends EntityWolfBase implements IRenderableWolf {

	private static final Logger LOGGER = LogManager.getLogger(MOD_ID + ".spawn");

	public EntityBookshelfWolf(World worldObj) {
		super(worldObj);
		addLikedItem(ItemRegistry.getWolfFood("book_bone"));
		setPeriodicDrop(new WolfPeriodicItemDrop(300, 5, new ItemStack(Items.book)));
	}

	@Override
	public boolean getCanSpawnHere() {
		final Village village = scanForVillage(1);

		LOGGER.info("bookshelf: Village found [{}], csh[{}], isbsnear[{}], isd[{}],ccs[{}], isf[{}], cssk[{}]", village, getCanSpawnHere(true), isBookshelfNearby(), isSuitableDimension(), creatureCanSpawnHere(), isStandingOnSuitableFloor(), canSeeTheSky(getWorldObj(), posX, posY, posZ) ^ true);

		return getCanSpawnHere(true) &&
				village != null &&
				isBookshelfNearby();
	}

	private boolean isBookshelfNearby() {
		final Vec3 position = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
		int myX = (int) position.xCoord;
		int myY = (int) position.yCoord;
		int myZ = (int) position.zCoord;
		int scanRadius = 2;
		int scanHeight = 2;

		for (int x = myX - scanRadius; x < myX + scanRadius; x++) {
			for (int y = myY; y < myY + scanHeight; y++) {
				for (int z = myZ - scanRadius; z < myZ + scanRadius; z++) {
					final Block block = this.worldObj.getBlock(x, y, z);
					if (block == Blocks.bookshelf)
						return true;
				}
			}
		}
		return false;
	}

	@Override
	protected List<Block> getFloorBlocks() {
		return INDOOR_FLOOR_BLOCKS;
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
