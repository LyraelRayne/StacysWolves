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

import static au.lyrael.stacywolves.entity.SpawnWeights.SPAWN_PROBABILITY_RARE;
import static au.lyrael.stacywolves.entity.SpawnWeights.SPAWN_PROBABILITY_SUPER_RARE;
import static au.lyrael.stacywolves.entity.SpawnWeights.SPAWN_WEIGHT_SUPER_RARE;
import static au.lyrael.stacywolves.registry.WolfType.SUBTERRANEAN;
import static net.minecraftforge.common.BiomeDictionary.Type.*;

@WolfMetadata(name = "EntityLapisWolf", primaryColour = 0x7F7F7F, secondaryColour = 0x102CB5, type = SUBTERRANEAN, probability = SPAWN_PROBABILITY_RARE,
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
				}, weight = SPAWN_WEIGHT_SUPER_RARE, min = 1, max = 4)
		})
public class EntityLapisWolf extends EntitySubterraneanWolfBase implements IRenderableWolf {

	public EntityLapisWolf(World worldObj) {
		super(worldObj);
		addLikedItem(ItemRegistry.getWolfFood("lapis_bone"));
		setPeriodicDrop(new WolfPeriodicItemDrop(300, 5, new ItemStack(Items.dye, 1, 4)));
	}

	@Override
	public EntityWolfBase createChild(EntityAgeable parent) {
		EntityWolfBase child = new EntityLapisWolf(this.worldObj);
		return createChild(parent, child);
	}

	@Override
	public String getTextureFolderName() {
		return "lapis";
	}

	@Override
	public boolean getCanSpawnHere() {
		return getCanSpawnHere(17);
	}
}
