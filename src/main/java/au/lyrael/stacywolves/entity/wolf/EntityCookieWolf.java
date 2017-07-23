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

import static au.lyrael.stacywolves.entity.SpawnWeights.SPAWN_WEIGHT_COMMON;
import static net.minecraftforge.common.BiomeDictionary.Type.JUNGLE;

@WolfMetadata(name = "EntityCookieWolf", primaryColour = 0xD7813C, secondaryColour = 0x894929,
		spawns = {
				@WolfSpawn(spawnBiomes = {
						@WolfSpawnBiome(requireBiomeTypes = {JUNGLE}),
				}, weight = SPAWN_WEIGHT_COMMON, min = 1, max = 4),
		})
public class EntityCookieWolf extends EntityWolfBase implements IRenderableWolf {

	public EntityCookieWolf(World worldObj) {
		super(worldObj);
		addLikedItem(ItemRegistry.getWolfFood("cookie_bone"));
		setPeriodicDrop(new WolfPeriodicItemDrop(300, 5, new ItemStack(Items.cookie)));
	}

	@Override
	public EntityWolfBase createChild(EntityAgeable parent) {
		EntityWolfBase child = new EntityCookieWolf(this.worldObj);
		return createChild(parent, child);
	}

	@Override
	public String getTextureFolderName() {
		return "cookie";
	}
}
