package au.lyrael.stacywolves.entity.wolf;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.annotation.WolfSpawn;
import au.lyrael.stacywolves.annotation.WolfSpawnBiome;
import au.lyrael.stacywolves.client.render.IRenderableWolf;
import au.lyrael.stacywolves.registry.ItemRegistry;
import au.lyrael.stacywolves.registry.WolfType;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.world.World;

import static au.lyrael.stacywolves.entity.SpawnWeights.SPAWN_WEIGHT_COMMON;
import static au.lyrael.stacywolves.utility.WorldHelper.canSeeTheSky;
import static net.minecraftforge.common.BiomeDictionary.Type.*;

@WolfMetadata(name = "EntitySquidWolf", primaryColour = 0x000000, secondaryColour = 0x2222AA, type = WolfType.WATER,
		spawns = {
				@WolfSpawn(spawnBiomes = {
						@WolfSpawnBiome(requireBiomeTypes = {OCEAN}),
						@WolfSpawnBiome(requireBiomeTypes = {BEACH}),
						@WolfSpawnBiome(requireBiomeTypes = {RIVER}),
				}, weight = SPAWN_WEIGHT_COMMON, min = 1, max = 4),
		})
public class EntitySquidWolf extends EntityWolfBase implements IRenderableWolf {

	public EntitySquidWolf(World worldObj) {
		super(worldObj);
		addLikedItem(ItemRegistry.getWolfFood("inky_bone"));
	}

	@Override
	public EntityWolfBase createChild(EntityAgeable parent) {
		EntityWolfBase child = new EntitySquidWolf(this.worldObj);
		return createChild(parent, child);
	}

	@Override
	public String getTextureFolderName() {
		return "squid";
	}

	@Override
	public boolean isPushedByWater() {
		return false;
	}

	@Override
	public boolean canBreatheUnderwater() {
		return true;
	}

	@Override
	public boolean canSpawnNow(World world, float x, float y, float z) {
		return world.isDaytime();
	}

	@Override
	public boolean normallyAvoidsWater() {
		return false;
	}

	@Override
	public boolean alwaysAvoidsWater() {
		return false;
	}

	/**
	 * Standard spawn conditions
	 *
	 * @param noSky If true, wolf shouldn't be able to see the sky, otherwise they should.
	 */
	@Override
	protected boolean getCanSpawnHere(boolean noSky) {
		return isSuitableDimension() &&
				this.posY > 45.0D &&
				this.posY < 63.0D &&
				this.worldObj.checkNoEntityCollision(this.boundingBox);
	}

}
