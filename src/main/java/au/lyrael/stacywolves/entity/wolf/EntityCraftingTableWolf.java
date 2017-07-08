package au.lyrael.stacywolves.entity.wolf;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.annotation.WolfSpawn;
import au.lyrael.stacywolves.annotation.WolfSpawnBiome;
import au.lyrael.stacywolves.client.render.IRenderableWolf;
import au.lyrael.stacywolves.registry.ItemRegistry;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.world.World;

import static au.lyrael.stacywolves.entity.SpawnWeights.SPAWN_WEIGHT_RARE;
import static net.minecraftforge.common.BiomeDictionary.Type.FOREST;

@WolfMetadata(name = "EntityCraftingTableWolf", primaryColour = 0xB38F59, secondaryColour = 0x572C19,
		spawns = {
				@WolfSpawn(spawnBiomes = {
						@WolfSpawnBiome(requireBiomeTypes = {FOREST}),
				}, weight = SPAWN_WEIGHT_RARE, min = 1, max = 2),
		})
public class EntityCraftingTableWolf extends EntityWolfBase implements IRenderableWolf {

	public EntityCraftingTableWolf(World worldObj) {
		super(worldObj);
		addLikedItem(ItemRegistry.getWolfFood("crafty_bone"));
	}

	@Override
	public EntityWolfBase createChild(EntityAgeable parent) {
		EntityWolfBase child = new EntityCraftingTableWolf(this.worldObj);
		return createChild(parent, child);
	}

	@Override
	public String getTextureFolderName() {
		return "crafting_table";
	}
}
