package au.lyrael.stacywolves.entity.wolf;

import net.minecraft.block.Block;
import net.minecraft.world.World;

import java.util.List;

public abstract class EntitySubterraneanWolfBase extends EntityWolfBase {
	public EntitySubterraneanWolfBase(World world) {
		super(world);
	}

	protected boolean getCanSpawnHere(int maxY) {
		return getCanSpawnHere(true)
				&& this.posY < maxY;
	}

	@Override
	public boolean canSpawnNow(World world, float x, float y, float z) {
		return true;
	}

	@Override
	protected List<Block> getFloorBlocks() {
		return SUBTERRANEAN_FLOOR_BLOCKS;
	}
}
