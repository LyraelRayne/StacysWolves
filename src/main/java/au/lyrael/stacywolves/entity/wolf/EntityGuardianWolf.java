package au.lyrael.stacywolves.entity.wolf;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.annotation.WolfSpawn;
import au.lyrael.stacywolves.annotation.WolfSpawnBiome;
import au.lyrael.stacywolves.client.render.IRenderableWolf;
import au.lyrael.stacywolves.registry.ItemRegistry;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.world.World;

import static au.lyrael.stacywolves.entity.SpawnWeights.SPAWN_WEIGHT_RARE;
import static au.lyrael.stacywolves.entity.SpawnWeights.SPAWN_WEIGHT_SUPER_RARE;
import static au.lyrael.stacywolves.registry.WolfType.WATER;

@WolfMetadata(name = "EntityGuardianWolf", primaryColour = 0x395F4E, secondaryColour = 0xD7793A, type = WATER,
		spawns = {
				@WolfSpawn(spawnBiomes = {
						@WolfSpawnBiome(specificBiomes = "Deep Ocean"),
				}, weight = SPAWN_WEIGHT_SUPER_RARE, min = 1, max = 2),
		})
public class EntityGuardianWolf extends EntityPrismarineWolfBase implements IRenderableWolf {

	public EntityGuardianWolf(World worldObj) {
		super(worldObj);
		addLikedItem(ItemRegistry.getWolfFood("solid_prismarine_bone"));
	}

	@Override
	public EntityWolfBase createChild(EntityAgeable parent) {
		EntityWolfBase child = new EntityGuardianWolf(this.worldObj);
		return createChild(parent, child);
	}

	@Override
	public String getTextureFolderName() {
		return "guardian";
	}

}
