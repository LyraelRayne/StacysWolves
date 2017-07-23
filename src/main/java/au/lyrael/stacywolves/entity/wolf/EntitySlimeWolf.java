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

import static au.lyrael.stacywolves.entity.SpawnWeights.SPAWN_WEIGHT_RARE;
import static net.minecraftforge.common.BiomeDictionary.Type.SWAMP;

@WolfMetadata(name = "EntitySlimeWolf", primaryColour = 0x8ABD7C, secondaryColour = 0x2E4628,
		spawns = {
				@WolfSpawn(spawnBiomes = {
						@WolfSpawnBiome(requireBiomeTypes = {SWAMP}),
				}, weight = SPAWN_WEIGHT_RARE, min = 1, max = 4),
		})
public class EntitySlimeWolf extends EntityWolfBase implements IRenderableWolf {

	public EntitySlimeWolf(World worldObj) {
		super(worldObj);
		addLikedItem(ItemRegistry.getWolfFood("slime_bone"));
		setPeriodicDrop(new WolfPeriodicItemDrop(800, 20, new ItemStack(Items.slime_ball)));
	}

	@Override
	public EntityWolfBase createChild(EntityAgeable parent) {
		EntityWolfBase child = new EntitySlimeWolf(this.worldObj);
		return createChild(parent, child);
	}

	@Override
	public String getTextureFolderName() {
		return "slime";
	}
}
