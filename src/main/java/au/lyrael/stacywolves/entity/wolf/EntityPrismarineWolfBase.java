package au.lyrael.stacywolves.entity.wolf;

import au.lyrael.stacywolves.integration.EtFuturumHolder;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class EntityPrismarineWolfBase extends EntityWolfBase {

	public EntityPrismarineWolfBase(World world) {
		super(world);
	}

	@Override
	public boolean canSpawnNow(World world, float x, float y, float z) {
		return true;
	}

	@Override
	public boolean getCanSpawnHere() {
		// Naming this variable "down" will cause the vector to have some weird value. No, really. It's insane.
		final Vec3d downwards = new Vec3d(0D, -5.0D, 0D);
		final Vec3d up = new Vec3d(0D, 5D, 0D);
		return isSuitableDimension()
				&& getWorldObj().checkNoEntityCollision(getEntityBoundingBox())
				&& isBlockInDirectionPrismarine(downwards)
				&& isBlockInDirectionPrismarine(up);
	}

	protected boolean isBlockInDirectionPrismarine(Vec3d direction) {
		final Block prismarine_block = EtFuturumHolder.prismarine_block;
		if (prismarine_block != null) {
			final Block blockBelow = getBlockInDirection(direction);
			return blockBelow == prismarine_block;
		}
		return false;
	}

	private Block getBlockInDirection(Vec3d direction) {
		final Vec3d location = new Vec3d(posX, posY, posZ);
		final Vec3d target = location.addVector(direction.x, direction.y, direction.z);
		final BlockPos blockPosition = getWorldObj().rayTraceBlocks(location, target).getBlockPos();
		return blockPosition != null
				? getWorldObj().getBlockState(blockPosition).getBlock()
				: null;
	}

	@Override
	protected void doWolfShaking() {

	}

	@Override
	public boolean handleWaterMovement() {
		if (!shouldSwimToSurface()) {
			this.setIsInWater(false);
			return false;
		} else
			return super.handleWaterMovement();
	}

	@Override
	public boolean shouldSwimToSurface() {
		final Block blockInDirection = getBlockInDirection(new Vec3d(0D, 30D, 0D));
		return blockInDirection == null;
	}

	@Override
	public boolean normallyAvoidsWater() {
		return false;
	}

	@Override
	public boolean alwaysAvoidsWater() {
		return false;
	}

	@Override
	public boolean isPushedByWater() {
		return false;
	}

	@Override
	public boolean canBreatheUnderwater() {
		return true;
	}
}
