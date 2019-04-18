package au.lyrael.stacywolves.lighting;

import au.lyrael.stacywolves.entity.wolf.EntityWolfBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

import static au.lyrael.stacywolves.config.RuntimeConfiguration.lightingEnabled;

public class WolfLightSource {

	private final int lightLevel;
	private final World worldObj;
	private final EntityWolfBase wolf;

	public WolfLightSource(EntityWolfBase wolf, World world, int lightLevel) {
		this.lightLevel = lightLevel;
		this.worldObj = world;
		this.wolf = wolf;
	}

	// Adapted from http://www.minecraftforge.net/forum/topic/28699-making-an-entity-as-a-light-source/
	public void addLight() {
		if (lightingEnabled) {
			final Vec3d position = new Vec3d(wolf.posX, wolf.posY, wolf.posZ);
			int myX = (int) position.x;
			int myY = (int) position.y;
			int myZ = (int) position.z;

			updateLightForPreviousBlock();

			final BlockPos pos = new BlockPos(myX, myY, myZ);
			this.worldObj.setLightFor(EnumSkyBlock.BLOCK, pos, getLightLevel());
			this.worldObj.markBlockRangeForRenderUpdate(myX, myY, myZ, 12, 12, 12);
			this.worldObj.notifyLightSet(pos);
			int scanRadius = 1;
			for (int x = myX - scanRadius; x < myX + scanRadius; x++) {
				for (int y = myY - scanRadius; y < myY + scanRadius; y++) {
					for (int z = myZ - scanRadius; z < myZ + scanRadius; z++) {
						if (!(x == myX && y == myY && z == myZ))
							this.worldObj.checkLightFor(EnumSkyBlock.BLOCK, new BlockPos(x, y, z));
					}
				}
			}
		}
	}

	private Integer lightingPreviousX;
	private Integer lightingPreviousY;
	private Integer lightingPreviousZ;

	protected void updateLightForPreviousBlock() {
		if (lightingEnabled) {
			if (hasMovedSinceLastTick()) {
				this.worldObj.checkLightFor(EnumSkyBlock.BLOCK, new BlockPos(lightingPreviousX, lightingPreviousY, lightingPreviousZ));
			}
			final Vec3d position = new Vec3d(wolf.posX, wolf.posY, wolf.posZ);
			this.lightingPreviousX = (int) position.x;
			this.lightingPreviousY = (int) position.y;
			this.lightingPreviousZ = (int) position.z;
		}
	}

	private boolean hasMovedSinceLastTick() {
		final Vec3d position = new Vec3d(wolf.posX, wolf.posY, wolf.posZ);
		int myX = (int) position.x;
		int myY = (int) position.y;
		int myZ = (int) position.z;

		return (lightingPreviousX != null && lightingPreviousY != null && lightingPreviousZ != null) &&
				(lightingPreviousX != myX || lightingPreviousY != myY || lightingPreviousZ != myZ);
	}

	public void clearLighting() {
		if (lightingEnabled) {
			final Vec3d position = new Vec3d(wolf.posX, wolf.posY, wolf.posZ);
			int myX = (int) position.x;
			int myY = (int) position.y;
			int myZ = (int) position.z;
			this.worldObj.checkLightFor(EnumSkyBlock.BLOCK, new BlockPos(myX, myY, myZ));
			updateLightForPreviousBlock();
		}
	}

	public int getLightLevel() {
		return lightLevel;
	}
}
