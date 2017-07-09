package au.lyrael.stacywolves.entity.wolf;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.annotation.WolfSpawn;
import au.lyrael.stacywolves.annotation.WolfSpawnBiome;
import au.lyrael.stacywolves.client.render.IRenderableWolf;
import au.lyrael.stacywolves.registry.ItemRegistry;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.world.World;

import static au.lyrael.stacywolves.entity.SpawnWeights.SPAWN_WEIGHT_COMMON;
import static net.minecraftforge.common.BiomeDictionary.Type.JUNGLE;
import static net.minecraftforge.common.BiomeDictionary.Type.SAVANNA;

@WolfMetadata(name = "EntityMelonWolf", primaryColour = 0x444F0F, secondaryColour = 0xB92013,
		spawns = {
				@WolfSpawn(spawnBiomes = {
						@WolfSpawnBiome(requireBiomeTypes = {JUNGLE}),
				}, weight = SPAWN_WEIGHT_COMMON, min = 1, max = 4),
		})
public class EntityMelonWolf extends EntityWolfBase implements IRenderableWolf {

	public EntityMelonWolf(World worldObj) {
		super(worldObj);
		addLikedItem(ItemRegistry.getWolfFood("melon_bone"));
	}

	@Override
	public EntityWolfBase createChild(EntityAgeable parent) {
		EntityWolfBase child = new EntityMelonWolf(this.worldObj);
		return createChild(parent, child);
	}

	@Override
	public String getTextureFolderName() {
		return "melon";
	}
}
