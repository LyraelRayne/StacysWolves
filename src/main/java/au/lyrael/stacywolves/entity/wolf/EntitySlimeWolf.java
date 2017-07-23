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
import net.minecraft.util.MathHelper;
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

	/**
	 * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
	 * use this to react to sunlight and start to burn.
	 */
	@Override
	public void onLivingUpdate() {
		if (this.rand.nextInt(100) == 9) {
			generateSlimeParticles();
		}
		super.onLivingUpdate();
	}

	protected void generateSlimeParticles() {

		if(this.worldObj.isRemote) {
			for (int j = 0; j < 5; ++j) {
				float f = this.rand.nextFloat() * (float) Math.PI * 2.0F;
				float randomOffset = this.rand.nextFloat() * 0.5F + 0.1F;
				float xOffset = MathHelper.sin(f) * randomOffset;
				float zOffset = MathHelper.cos(f) * randomOffset;
				this.worldObj.spawnParticle("slime", this.posX + (double) xOffset, this.boundingBox.minY + 0.4, this.posZ + (double) zOffset, 0.0D, 0.0D, 0.0D);
			}
		}

		this.playSound("mob.slime.small", this.getSoundVolume() * 0.5f, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) / 0.8F);
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
