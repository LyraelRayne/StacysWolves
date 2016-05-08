package au.lyrael.stacywolves.entity.wolf;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.annotation.WolfSpawn;
import au.lyrael.stacywolves.annotation.WolfSpawnBiome;
import au.lyrael.stacywolves.client.render.IRenderableWolf;
import au.lyrael.stacywolves.registry.ItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;

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
                }, probability = 10, min = 1, max = 1)
        })

public class EntityEnderWolf extends EntityWolfBase implements IRenderableWolf {

    private int teleportDelay;

    public EntityEnderWolf(World worldObj) {
        super(worldObj);
        addLikedItem(ItemRegistry.getWolfFood("ender_bone"));
        this.addEdibleItem(new ItemStack(Items.beef));
        this.addEdibleItem(new ItemStack(Items.chicken));
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

            if (!isTamed() && (isWet() || isBurning() || shouldRandomTeleport()))
                teleportRandomly();

            if (getAttackTarget() != null) {
                doAttackTeleport();
            }
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

    protected void doAttackTeleport() {
        if (this.isEntityAlive()) {
            if (this.getAttackTarget() != null) {
                if (this.getAttackTarget().getDistanceSqToEntity(this) > 32.0D && this.teleportDelay++ >= 30 && this.teleportToEntity(this.getAttackTarget())) {
                    this.getNavigator().clearPathEntity();
                    this.teleportDelay = 0;
                }
            } else {
                this.teleportDelay = 0;
            }
        }
    }

    private boolean shouldRandomTeleport() {
        return getAttackTarget() == null && canSeeTheSky(getWorldObj(), posX, posY, posZ) && this.getRNG().nextInt(100) < 2;
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
     * Teleport the enderman to another entity
     */
    protected boolean teleportToEntity(Entity p_70816_1_) {
        final double myYHeight = this.boundingBox.minY + (double) (this.height / 2.0F);
        final double targetYHeight = p_70816_1_.posY + (double) p_70816_1_.getEyeHeight();
        Vec3 targetVector = Vec3.createVectorHelper(p_70816_1_.posX - this.posX, targetYHeight - myYHeight, p_70816_1_.posZ - this.posZ);
        targetVector = targetVector.normalize();

        double d0 = this.getDistanceToEntity(p_70816_1_) - 1;

        double targetX = this.posX + targetVector.xCoord * d0;
        double targetY = this.posY + targetVector.yCoord * d0;
        double targetZ = this.posZ + targetVector.zCoord * d0;
        return this.teleportTo(targetX, targetY, targetZ);
    }

    /**
     * Teleport the enderman
     */
    protected boolean teleportTo(double p_70825_1_, double p_70825_3_, double p_70825_5_) {
        EnderTeleportEvent event = new EnderTeleportEvent(this, p_70825_1_, p_70825_3_, p_70825_5_, 0);
        if (MinecraftForge.EVENT_BUS.post(event)) {
            return false;
        }
        double origPosX = this.posX;
        double origPosY = this.posY;
        double origPosZ = this.posZ;
        this.posX = event.targetX;
        this.posY = event.targetY;
        this.posZ = event.targetZ;
        boolean success = false;
        int i = MathHelper.floor_double(this.posX);
        int j = MathHelper.floor_double(this.posY);
        int k = MathHelper.floor_double(this.posZ);

        if (this.worldObj.blockExists(i, j, k)) {
            boolean flag1 = false;

            while (!flag1 && j > 0) {
                Block block = this.worldObj.getBlock(i, j - 1, k);

                if (block.getMaterial().blocksMovement()) {
                    flag1 = true;
                } else {
                    --this.posY;
                    --j;
                }
            }

            if (flag1) {
                this.setPosition(this.posX, this.posY, this.posZ);

                if (this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).isEmpty() && !this.worldObj.isAnyLiquid(this.boundingBox)) {
                    success = true;
                }
            }
        }

        if (!success) {
            this.setPosition(origPosX, origPosY, origPosZ);
            return false;
        } else {
            short short1 = 128;

            for (int l = 0; l < short1; ++l) {
                double d6 = (double) l / ((double) short1 - 1.0D);
                float f = (this.rand.nextFloat() - 0.5F) * 0.2F;
                float f1 = (this.rand.nextFloat() - 0.5F) * 0.2F;
                float f2 = (this.rand.nextFloat() - 0.5F) * 0.2F;
                double d7 = origPosX + (this.posX - origPosX) * d6 + (this.rand.nextDouble() - 0.5D) * (double) this.width * 2.0D;
                double d8 = origPosY + (this.posY - origPosY) * d6 + this.rand.nextDouble() * (double) this.height;
                double d9 = origPosZ + (this.posZ - origPosZ) * d6 + (this.rand.nextDouble() - 0.5D) * (double) this.width * 2.0D;
                this.worldObj.spawnParticle("portal", d7, d8, d9, (double) f, (double) f1, (double) f2);
            }

            this.worldObj.playSoundEffect(origPosX, origPosY, origPosZ, "mob.endermen.portal", 1.0F, 1.0F);
            this.playSound("mob.endermen.portal", 1.0F, 1.0F);

            return true;
        }
    }


    @Override
    public boolean canSpawnNow(World world, float x, float y, float z) {
        return !world.isDaytime();
    }

    @Override
    protected boolean isStandingOnSuitableFloor() {
        return true;
    }
}
