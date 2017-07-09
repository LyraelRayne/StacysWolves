package au.lyrael.stacywolves.entity.wolf;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.annotation.WolfSpawn;
import au.lyrael.stacywolves.annotation.WolfSpawnBiome;
import au.lyrael.stacywolves.client.render.IRenderableWolf;
import au.lyrael.stacywolves.lighting.WolfLightSource;
import au.lyrael.stacywolves.registry.ItemRegistry;
import au.lyrael.stacywolves.registry.WolfType;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.util.Vec3;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

import static au.lyrael.stacywolves.entity.SpawnWeights.SPAWN_WEIGHT_RARE;
import static au.lyrael.stacywolves.registry.WolfType.SUBTERRANEAN;
import static au.lyrael.stacywolves.utility.MathUtility.getFacing;
import static net.minecraftforge.common.BiomeDictionary.Type.*;

@WolfMetadata(name = "EntityTorchWolf", primaryColour = 0x665130, secondaryColour = 0xFFD800, type = SUBTERRANEAN,
		spawns = {
				@WolfSpawn(spawnBiomes = {
						@WolfSpawnBiome(requireBiomeTypes = {PLAINS}),
						@WolfSpawnBiome(requireBiomeTypes = {MESA}),
						@WolfSpawnBiome(requireBiomeTypes = {FOREST}),
						@WolfSpawnBiome(requireBiomeTypes = {MOUNTAIN}),
						@WolfSpawnBiome(requireBiomeTypes = {HILLS}),
						@WolfSpawnBiome(requireBiomeTypes = {SWAMP}),
						@WolfSpawnBiome(requireBiomeTypes = {SANDY}),
						@WolfSpawnBiome(requireBiomeTypes = {SNOWY}),
						@WolfSpawnBiome(requireBiomeTypes = {WASTELAND}),
						@WolfSpawnBiome(requireBiomeTypes = {BEACH}),
						@WolfSpawnBiome(requireBiomeTypes = {JUNGLE}),
						@WolfSpawnBiome(requireBiomeTypes = {RIVER}),
				}, weight = SPAWN_WEIGHT_RARE, min = 1, max = 4),
		})
public class EntityTorchWolf extends EntitySubterraneanWolfBase implements IRenderableWolf {

	public EntityTorchWolf(World worldObj) {
		super(worldObj);
		addLikedItem(ItemRegistry.getWolfFood("torch_bone"));
		setLightSource(new WolfLightSource(this, worldObj, 10));
	}

	@Override
	public EntityWolfBase createChild(EntityAgeable parent) {
		EntityWolfBase child = new EntityTorchWolf(this.worldObj);
		return createChild(parent, child);
	}

	@Override
	public String getTextureFolderName() {
		return "torch";
	}

	@Override
	public boolean getCanSpawnHere() {
		return getCanSpawnHere(50);
	}

	/**
	 * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
	 * use this to react to sunlight and start to burn.
	 */
	@Override
	public void onLivingUpdate() {
		if (this.worldObj.isRemote && this.rand.nextInt(10) == 9) {
			for (int count = 0; count < 2; ++count) {
				final Vec3 facing = getFacing(this.renderYawOffset, this.rotationPitch);
				this.worldObj.spawnParticle("smoke",
						this.posX + facing.xCoord * 0.3,
						this.posY + this.rand.nextDouble() * (double) this.height + 0.95,
						this.posZ + facing.zCoord * 0.3, 0.0D, 0.0D, 0.0D);
			}
		}
		super.onLivingUpdate();
	}
}
