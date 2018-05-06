package au.lyrael.stacywolves.entity.wolf;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.annotation.WolfSpawn;
import au.lyrael.stacywolves.annotation.WolfSpawnBiome;
import au.lyrael.stacywolves.client.render.IRenderableWolf;
import au.lyrael.stacywolves.entity.ai.EntityAIWolfTempt;
import au.lyrael.stacywolves.item.WolfPeriodicItemDrop;
import au.lyrael.stacywolves.registry.ItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;

import static au.lyrael.stacywolves.entity.SpawnWeights.SPAWN_WEIGHT_MOB_RARE;
import static au.lyrael.stacywolves.registry.WolfType.MOB;
import static au.lyrael.stacywolves.utility.WorldHelper.canSeeTheSky;
import static net.minecraftforge.common.BiomeDictionary.Type.*;

@WolfMetadata(name = "EntityEnderWolf", primaryColour = 0x000000, secondaryColour = 0xCC00FA, type = MOB,
        spawns = {
                @WolfSpawn(spawnBiomes = {
                        @WolfSpawnBiome(requireBiomeTypes = {PLAINS}),
                        @WolfSpawnBiome(requireBiomeTypes = {MESA}),
                        @WolfSpawnBiome(requireBiomeTypes = {FOREST}),
                        @WolfSpawnBiome(requireBiomeTypes = {MOUNTAIN}),
                        @WolfSpawnBiome(requireBiomeTypes = {HILLS}),
                        @WolfSpawnBiome(requireBiomeTypes = {SWAMP}),
                        @WolfSpawnBiome(requireBiomeTypes = {SANDY}),
                        @WolfSpawnBiome(requireBiomeTypes = {SNOWY}),
                        @WolfSpawnBiome(requireBiomeTypes = {WASTELAND}),
                        @WolfSpawnBiome(requireBiomeTypes = {BEACH}),
                        @WolfSpawnBiome(requireBiomeTypes = {JUNGLE}),
                        @WolfSpawnBiome(requireBiomeTypes = {RIVER}),
                }, weight = SPAWN_WEIGHT_MOB_RARE, min = 1, max = 1)
        })

public class EntityEnderWolf extends EntityWolfBase implements IRenderableWolf {

    private static int TICKS_BETWEEN_RANDOM_TELEPORT_ATTEMPTS = 20 * 5;

    private int teleportDelay;
    private int randomTeleportDelay = TICKS_BETWEEN_RANDOM_TELEPORT_ATTEMPTS;


    public EntityEnderWolf(World worldObj) {
        super(worldObj);
        addLikedItem(ItemRegistry.getWolfFood("ender_bone"));
		setPeriodicDrop(new WolfPeriodicItemDrop(300, 5, new ItemStack(Items.ender_pearl)));
	}

    @Override
    public EntityWolfBase createChild(EntityAgeable parent) {
        EntityWolfBase child = new EntityEnderWolf(this.worldObj);
        return createChild(parent, child);
    }

    @Override
    public String getTextureFolderName() {
        return "ender";
    }

    @Override
    public void onLivingUpdate() {
        if (!this.worldObj.isRemote) {
            hurtIfWet();
            doTeleportStuff();
        } else {
            for (int loop = 0; loop < 2; ++loop) {
                this.worldObj.spawnParticle("portal",
                        this.posX + (this.rand.nextDouble() - 0.5D) * (double) this.width,
                        this.posY + this.rand.nextDouble() * (double) this.height - 0.25D,
                        this.posZ + (this.rand.nextDouble() - 0.5D) * (double) this.width,
                        (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D);
            }
        }

        super.onLivingUpdate();
    }

    protected void doTeleportStuff() {
        final EntityAIWolfTempt aiTempt = getAiTempt();
        final EntityPlayer temptingPlayer = aiTempt.getTemptingPlayer();

        if (isTamed() && isSitting()) {
            // do nothing
        } else if (!isTamed() && (isWet() || isBurning())) {
            teleportRandomly();
        } else if (aiTempt.isRunning() && temptingPlayer != null) {
            doTeleportToEntity(temptingPlayer);
        } else if (getAttackTarget() != null) {
            doTeleportToEntity(getAttackTarget());
        } else if (!isTamed() && shouldRandomTeleport()) {
            teleportRandomly();
        }
    }

    protected void doTeleportToEntity(EntityLivingBase target) {
        if (this.isEntityAlive()) {
            if (target != null) {
                if (target.getDistanceSqToEntity(this) > 32.0D && this.teleportDelay-- <= 0 && this.teleportToEntity(target)) {
                    this.getNavigator().clearPathEntity();
                    this.teleportDelay = 30;
                }
            } else {
                this.teleportDelay = 30;
            }
        }
    }

    private boolean shouldRandomTeleport() {
        if (getAttackTarget() == null && canSeeTheSky(getWorldObj(), posX, posY, posZ)) {
            randomTeleportDelay = randomTeleportDelay > 0 ? randomTeleportDelay - 1 : 0;
            if (this.randomTeleportDelay <= 0) {
                this.randomTeleportDelay = TICKS_BETWEEN_RANDOM_TELEPORT_ATTEMPTS;
                return this.getRNG().nextInt(100) < 20;
            }
        }
        return false;
    }


    /**
     * Teleport the enderman to a random nearby position
     */
    protected boolean teleportRandomly() {
        double d0 = this.posX + (this.rand.nextDouble() - 0.5D) * 64.0D;
        double d1 = this.posY + (double) (this.rand.nextInt(64) - 32);
        double d2 = this.posZ + (this.rand.nextDouble() - 0.5D) * 64.0D;
        return this.teleportTo(d0, d1, d2);
    }


    /**
     * Teleport the wolf
     *
     * @param p_70825_1_
     * @param p_70825_3_
     * @param p_70825_5_
     */
    @Override
    protected boolean teleportTo(double p_70825_1_, double p_70825_3_, double p_70825_5_) {
        final boolean teleported = super.teleportTo(p_70825_1_, p_70825_3_, p_70825_5_);
        if(!teleported) {
            this.teleportDelay = 90 * 20;
        }

        return teleported;
    }

    @Override
    public boolean canSpawnNow(World world, float x, float y, float z) {
        return !world.isDaytime();
    }

    @Override
    protected boolean isStandingOnSuitableFloor() {
        return true;
    }

	@Override
	public boolean normallyAvoidsWater() {
		return !isTamed();
	}

	@Override
	public boolean alwaysAvoidsWater() {
		return !isTamed();
	}
}
