package au.lyrael.stacywolves.entity.wolf;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.annotation.WolfSpawn;
import au.lyrael.stacywolves.annotation.WolfSpawnBiome;
import au.lyrael.stacywolves.client.render.IRenderableWolf;
import au.lyrael.stacywolves.registry.ItemRegistry;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.world.World;

import static au.lyrael.stacywolves.entity.SpawnWeights.SPAWN_WEIGHT_SUPER_RARE;
import static net.minecraftforge.common.BiomeDictionary.Type.JUNGLE;

@WolfMetadata(name = "EntityOcelotWolf", primaryColour = 0xD99C53, secondaryColour = 0x1D6D30,
		spawns = {
				@WolfSpawn(spawnBiomes = {
						@WolfSpawnBiome(requireBiomeTypes = {JUNGLE}),
				}, weight = SPAWN_WEIGHT_SUPER_RARE, min = 1, max = 1),
		})
public class EntityOcelotWolf extends EntityWolfBase implements IRenderableWolf {

	public EntityOcelotWolf(World worldObj) {
		super(worldObj);
		addLikedItem(ItemRegistry.getWolfFood("fish_bone"));
	}

	@Override
	public EntityWolfBase createChild(EntityAgeable parent) {
		EntityWolfBase child = new EntityOcelotWolf(this.worldObj);
		return createChild(parent, child);
	}

	@Override
	public String getTextureFolderName() {
		return "ocelot";
	}
}
