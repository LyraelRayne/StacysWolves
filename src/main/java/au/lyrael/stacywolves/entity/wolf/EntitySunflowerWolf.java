package au.lyrael.stacywolves.entity.wolf;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.annotation.WolfSpawn;
import au.lyrael.stacywolves.annotation.WolfSpawnBiome;
import au.lyrael.stacywolves.client.render.IRenderableWolf;
import au.lyrael.stacywolves.registry.ItemRegistry;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.world.World;

import static au.lyrael.stacywolves.entity.SpawnWeights.SPAWN_WEIGHT_COMMON;

@WolfMetadata(name = "EntitySunflowerWolf", primaryColour = 0xEBBD32, secondaryColour = 0x5A9841,
		spawns = {
				@WolfSpawn(spawnBiomes = {
						@WolfSpawnBiome(specificBiomes = "Sunflower Plains"),
				}, weight = SPAWN_WEIGHT_COMMON, min = 1, max = 4),
		})
public class EntitySunflowerWolf extends EntityWolfBase implements IRenderableWolf {

	public EntitySunflowerWolf(World worldObj) {
		super(worldObj);
		addLikedItem(ItemRegistry.getWolfFood("sunflower_bone"));
	}

	@Override
	public EntityWolfBase createChild(EntityAgeable parent) {
		EntityWolfBase child = new EntitySunflowerWolf(this.worldObj);
		return createChild(parent, child);
	}

	@Override
	public String getTextureFolderName() {
		return "sunflower";
	}
}
