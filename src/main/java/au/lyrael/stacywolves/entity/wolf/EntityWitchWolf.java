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

@WolfMetadata(name = "EntityWitchWolf", primaryColour = 0x340000, secondaryColour = 0x51a03e,
		spawns = {
				@WolfSpawn(spawnBiomes = {
						@WolfSpawnBiome(requireBiomeTypes = {SWAMP}),
				}, weight = SPAWN_WEIGHT_RARE, min = 1, max = 1),
		})
public class EntityWitchWolf extends EntityWolfBase implements IRenderableWolf {

	public EntityWitchWolf(World worldObj) {
		super(worldObj);
		addLikedItem(ItemRegistry.getWolfFood("glow_bone"));
		setPeriodicDrop(new WolfPeriodicItemDrop(800, 10,
				getPotionStack(8258), // Swiftness II
				getPotionStack(16392), // Splash weakness
				getPotionStack(16417), // Splash regen II
				getPotionStack(8201) // Strength
		));
	}

	private ItemStack getPotionStack(int potionId) {
		return new ItemStack(Items.potionitem, 1, potionId);
	}

	@Override
	public EntityWolfBase createChild(EntityAgeable parent) {
		EntityWolfBase child = new EntityWitchWolf(this.worldObj);
		return createChild(parent, child);
	}

	@Override
	public String getTextureFolderName() {
		return "witch";
	}
}
