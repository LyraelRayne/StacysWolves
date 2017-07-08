package au.lyrael.stacywolves.lighting;

import au.lyrael.stacywolves.entity.wolf.EntityWolfBase;
import net.minecraft.util.Vec3;
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
			final Vec3 position = getPosition();
			int myX = (int) position.xCoord;
			int myY = (int) position.yCoord;
			int myZ = (int) position.zCoord;

			updateLightForPreviousBlock();

			this.worldObj.setLightValue(EnumSkyBlock.Block, myX, myY, myZ, getLightLevel());
			this.worldObj.markBlockRangeForRenderUpdate(myX, myY, myZ, 12, 12, 12);
			this.worldObj.markBlockForUpdate(myX, myY, myZ);
			int scanRadius = 1;
			for (int x = myX - scanRadius; x < myX + scanRadius; x++) {
				for (int y = myY - scanRadius; y < myY + scanRadius; y++) {
					for (int z = myZ - scanRadius; z < myZ + scanRadius; z++) {
						if (!(x == myX && y == myY && z == myZ))
							this.worldObj.updateLightByType(EnumSkyBlock.Block, x, y, z);
					}
				}
			}
		}
	}

	private Vec3 getPosition() {
		return wolf.getPosition(1f);
	}

	private Integer lightingPreviousX;
	private Integer lightingPreviousY;
	private Integer lightingPreviousZ;

	protected void updateLightForPreviousBlock() {
		if (lightingEnabled) {
			if (hasMovedSinceLastTick()) {
				this.worldObj.updateLightByType(EnumSkyBlock.Block, lightingPreviousX, lightingPreviousY, lightingPreviousZ);
			}
			final Vec3 position = getPosition();
			this.lightingPreviousX = (int) position.xCoord;
			this.lightingPreviousY = (int) position.yCoord;
			this.lightingPreviousZ = (int) position.zCoord;
		}
	}

	private boolean hasMovedSinceLastTick() {
		final Vec3 position = getPosition();
		int myX = (int) position.xCoord;
		int myY = (int) position.yCoord;
		int myZ = (int) position.zCoord;

		return (lightingPreviousX != null && lightingPreviousY != null && lightingPreviousZ != null) &&
				(lightingPreviousX != myX || lightingPreviousY != myY || lightingPreviousZ != myZ);
	}

	public void clearLighting() {
		if (lightingEnabled) {
			final Vec3 position = getPosition();
			int myX = (int) position.xCoord;
			int myY = (int) position.yCoord;
			int myZ = (int) position.zCoord;
			this.worldObj.updateLightByType(EnumSkyBlock.Block, myX, myY, myZ);
			updateLightForPreviousBlock();
		}
	}

	public int getLightLevel() {
		return lightLevel;
	}
}
