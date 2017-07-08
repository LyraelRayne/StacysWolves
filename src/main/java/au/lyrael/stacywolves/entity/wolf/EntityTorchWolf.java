package au.lyrael.stacywolves.entity.wolf;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.annotation.WolfSpawn;
import au.lyrael.stacywolves.annotation.WolfSpawnBiome;
import au.lyrael.stacywolves.client.render.IRenderableWolf;
import au.lyrael.stacywolves.registry.ItemRegistry;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.world.World;

import static au.lyrael.stacywolves.entity.SpawnWeights.SPAWN_WEIGHT_RARE;
import static net.minecraftforge.common.BiomeDictionary.Type.*;

@WolfMetadata(name = "EntityTorchWolf", primaryColour = 0x665130, secondaryColour = 0xFFD800,
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
}
