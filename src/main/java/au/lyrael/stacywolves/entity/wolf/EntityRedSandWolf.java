package au.lyrael.stacywolves.entity.wolf;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.annotation.WolfSpawn;
import au.lyrael.stacywolves.annotation.WolfSpawnBiome;
import au.lyrael.stacywolves.client.render.IRenderableWolf;
import au.lyrael.stacywolves.registry.ItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.world.World;

import java.util.List;

import static au.lyrael.stacywolves.entity.SpawnWeights.SPAWN_WEIGHT_RARE;
import static net.minecraftforge.common.BiomeDictionary.Type.MESA;

@WolfMetadata(name = "EntityRedSandWolf", primaryColour = 0xA2501C, secondaryColour = 0x5A3305,
		spawns = {
				@WolfSpawn(spawnBiomes = {
						@WolfSpawnBiome(requireBiomeTypes = {MESA}),
				}, weight = SPAWN_WEIGHT_RARE, min = 1, max = 4),
		})
public class EntityRedSandWolf extends EntityWolfBase implements IRenderableWolf {

	public EntityRedSandWolf(World worldObj) {
		super(worldObj);
		addLikedItem(ItemRegistry.getWolfFood("red_sand_bone"));
	}

	@Override
	public EntityWolfBase createChild(EntityAgeable parent) {
		EntityWolfBase child = new EntityRedSandWolf(this.worldObj);
		return createChild(parent, child);
	}

	@Override
	public String getTextureFolderName() {
		return "red_sand";
	}

	@Override
	protected List<Block> getFloorBlocks() {
		return MESA_FLOOR_BLOCKS;
	}
}
