package au.lyrael.stacywolves.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static au.lyrael.stacywolves.StacyWolves.MOD_ID;

public class EntityAIWolfTempt extends EntityAIBase {

    private static final Logger LOGGER = LogManager.getLogger(MOD_ID + ".ai");

    /**
     * The entity using this AI that is tempted by the player.
     */
    private ITemptable temptableEntity;
    private double entityMoveSpeed;
    /**
     * X position of player tempting this mob
     */
    private double temptingPlayerPreviousX;
    /**
     * Y position of player tempting this mob
     */
    private double temptingPlayerPreviousY;
    /**
     * Z position of player tempting this mob
     */
    private double temptingPlayerPreviousZ;
    private double temptingPlayerPreviousRotationPitch;
    private double temptingPlayerPreviousRotationYaw;
    private EntityPlayer temptingPlayer;
    /**
     * A counter that is decremented each time the shouldExecute method is called. The shouldExecute method will always
     * return false if delayTemptCounter is greater than 0.
     */
    private int delayTemptCounter;
    /**
     * True if this EntityAIWolfTempt task is running
     */
    private boolean isRunning;
    /**
     * Whether the entity using this AI will be scared by the tempter's sudden movement.
     */
    private boolean scaredBySuddenMovement;

    public EntityAIWolfTempt(ITemptable temptableEntity, double entityMoveSpeed, boolean scaredBySuddenMovement) {
        this.temptableEntity = temptableEntity;
        this.entityMoveSpeed = entityMoveSpeed;
        this.scaredBySuddenMovement = scaredBySuddenMovement;
        this.setMutexBits(3);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        if (this.delayTemptCounter > 0) {
            --this.delayTemptCounter;
            return false;
        } else {

        }
        this.setTemptingPlayer(this.temptableEntity.getWorldObj().getClosestPlayerToEntity(this.temptableEntity.asEntityLiving(), 10.0D));

        if (this.getTemptingPlayer() == null) {
            return false;
        } else {
            ItemStack itemstack = this.getTemptingPlayer().getCurrentEquippedItem();
            final boolean isTempted = this.temptableEntity.isTemptedBy(itemstack);
            if (isTempted && !isRunning())
                LOGGER.debug("Temptable creature [{}] is tempted by [{}]'s [{}]", temptableEntity, getTemptingPlayer().getDisplayName(), itemstack);
            return isTempted;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting() {
        if (this.scaredBySuddenMovement) {
            if (this.temptableEntity.asEntityLiving().getDistanceSqToEntity(this.getTemptingPlayer()) < 36.0D) {
                if (this.getTemptingPlayer().getDistanceSq(this.temptingPlayerPreviousX, this.temptingPlayerPreviousY, this.temptingPlayerPreviousZ) > 0.010000000000000002D) {
                    LOGGER.debug("[{}] was scared by [{}]'s sudden body movement.", temptableEntity, getTemptingPlayer().getDisplayName());
                    return false;
                }

                if (Math.abs((double) this.getTemptingPlayer().rotationPitch - this.temptingPlayerPreviousRotationPitch) > 5.0D
                        || Math.abs((double) this.getTemptingPlayer().rotationYaw - this.temptingPlayerPreviousRotationYaw) > 5.0D) {
                    LOGGER.debug("[{}] was scared by [{}]'s sudden head movement.", temptableEntity, getTemptingPlayer().getDisplayName());
                    return false;
                }
            } else {
                this.temptingPlayerPreviousX = this.getTemptingPlayer().posX;
                this.temptingPlayerPreviousY = this.getTemptingPlayer().posY;
                this.temptingPlayerPreviousZ = this.getTemptingPlayer().posZ;
            }

            this.temptingPlayerPreviousRotationPitch = (double) this.getTemptingPlayer().rotationPitch;
            this.temptingPlayerPreviousRotationYaw = (double) this.getTemptingPlayer().rotationYaw;
        }

        return this.shouldExecute();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.temptingPlayerPreviousX = this.getTemptingPlayer().posX;
        this.temptingPlayerPreviousY = this.getTemptingPlayer().posY;
        this.temptingPlayerPreviousZ = this.getTemptingPlayer().posZ;
        this.temptingPlayerPreviousRotationPitch = (double) this.getTemptingPlayer().rotationPitch;
        this.temptingPlayerPreviousRotationYaw = (double) this.getTemptingPlayer().rotationYaw;

        this.isRunning = true;
        final EntityLiving temptedEntityLiving = this.temptableEntity.asEntityLiving();
        if (!this.temptableEntity.alwaysAvoidsWater()) {
            temptedEntityLiving.getNavigator().setAvoidsWater(false);
        }
    }

    /**
     * Resets the task
     */
    public void resetTask() {
        final EntityLiving temptedEntityLiving = this.temptableEntity.asEntityLiving();

        this.setTemptingPlayer(null);
        temptedEntityLiving.getNavigator().clearPathEntity();
        this.delayTemptCounter = 100;
        this.isRunning = false;
        temptedEntityLiving.getNavigator().setAvoidsWater(this.temptableEntity.normallyAvoidsWater());
    }

    /**
     * Updates the task
     */
    public void updateTask() {
        final EntityLiving temptedEntityLiving = this.temptableEntity.asEntityLiving();

        temptedEntityLiving.getLookHelper().setLookPositionWithEntity(this.getTemptingPlayer(), 30.0F, (float) temptedEntityLiving.getVerticalFaceSpeed());

        if (temptedEntityLiving.getDistanceSqToEntity(this.getTemptingPlayer()) < 6.25D) {
            temptedEntityLiving.getNavigator().clearPathEntity();
        } else {
            temptedEntityLiving.getNavigator().tryMoveToEntityLiving(this.getTemptingPlayer(), this.entityMoveSpeed);
        }
    }

    /**
     * @see #isRunning
     */
    public boolean isRunning() {
        return this.isRunning;
    }

    /**
     * The player that is tempting the entity that is using this AI.
     */
    public EntityPlayer getTemptingPlayer() {
        return temptingPlayer;
    }

    protected void setTemptingPlayer(EntityPlayer temptingPlayer) {
        this.temptingPlayer = temptingPlayer;
    }
}