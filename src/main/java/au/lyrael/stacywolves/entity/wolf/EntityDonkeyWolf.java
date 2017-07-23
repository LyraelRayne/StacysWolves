package au.lyrael.stacywolves.entity.wolf;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.annotation.WolfSpawn;
import au.lyrael.stacywolves.annotation.WolfSpawnBiome;
import au.lyrael.stacywolves.client.render.IRenderableWolf;
import au.lyrael.stacywolves.inventory.InventoryWolfChest;
import au.lyrael.stacywolves.registry.ItemRegistry;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import static au.lyrael.stacywolves.entity.SpawnWeights.SPAWN_WEIGHT_RARE;
import static net.minecraftforge.common.BiomeDictionary.Type.MESA;
import static net.minecraftforge.common.BiomeDictionary.Type.SAVANNA;

@WolfMetadata(name = "EntityDonkeyWolf", primaryColour = 0x514439, secondaryColour = 0x514439,
		spawns = {
				@WolfSpawn(spawnBiomes = {
						@WolfSpawnBiome(requireBiomeTypes = {SAVANNA}),
						@WolfSpawnBiome(requireBiomeTypes = {MESA}),
				}, weight = SPAWN_WEIGHT_RARE, min = 1, max = 2),
		})
public class EntityDonkeyWolf extends EntityWolfBase implements IRenderableWolf {


	public EntityDonkeyWolf(World worldObj) {
		super(worldObj);
		addLikedItem(ItemRegistry.getWolfFood("apple_bone"));
		this.setWolfChest(new InventoryWolfChest(27));
	}

	@Override
	public EntityWolfBase createChild(EntityAgeable parent) {
		EntityWolfBase child = new EntityDonkeyWolf(this.worldObj);
		return createChild(parent, child);
	}

	@Override
	public String getTextureFolderName() {
		return "donkey";
	}
}
