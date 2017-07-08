package au.lyrael.stacywolves.entity.wolf;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.annotation.WolfSpawn;
import au.lyrael.stacywolves.annotation.WolfSpawnBiome;
import au.lyrael.stacywolves.client.render.IRenderableWolf;
import au.lyrael.stacywolves.registry.ItemRegistry;
import net.minecraft.entity.EntityAgeable;
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
	public String getTextureFolderName() {
		return "cow";
	}
}
