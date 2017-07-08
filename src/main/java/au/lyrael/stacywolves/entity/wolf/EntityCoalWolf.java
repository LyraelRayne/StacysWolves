package au.lyrael.stacywolves.entity.wolf;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.annotation.WolfSpawn;
import au.lyrael.stacywolves.annotation.WolfSpawnBiome;
import au.lyrael.stacywolves.client.render.IRenderableWolf;
import au.lyrael.stacywolves.item.WolfPeriodicItemDrop;
import au.lyrael.stacywolves.registry.ItemRegistry;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import static au.lyrael.stacywolves.entity.SpawnWeights.SPAWN_PROBABILITY_SOMETIMES;
import static au.lyrael.stacywolves.entity.SpawnWeights.SPAWN_WEIGHT_COMMON;
import static au.lyrael.stacywolves.registry.WolfType.SUBTERRANEAN;
import static net.minecraftforge.common.BiomeDictionary.Type.*;

@WolfMetadata(name = "EntityCoalWolf", primaryColour = 0x7F7F7F, secondaryColour = 0x343434, type = SUBTERRANEAN, probability = SPAWN_PROBABILITY_SOMETIMES,
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
				}, weight = SPAWN_WEIGHT_COMMON, min = 1, max = 6),
		})
public class EntityCoalWolf extends EntitySubterraneanWolfBase implements IRenderableWolf {

	public EntityCoalWolf(World worldObj) {
		super(worldObj);
		addLikedItem(ItemRegistry.getWolfFood("coal_bone"));
		setPeriodicDrop(new WolfPeriodicItemDrop(600, 20, new ItemStack(Items.coal)));

	}

	@Override
	public EntityWolfBase createChild(EntityAgeable parent) {
		EntityWolfBase child = new EntityCoalWolf(this.worldObj);
		return createChild(parent, child);
	}

	@Override
	public String getTextureFolderName() {
		return "coal";
	}

	@Override
	public boolean getCanSpawnHere() {
		return getCanSpawnHere(50);
	}
}
