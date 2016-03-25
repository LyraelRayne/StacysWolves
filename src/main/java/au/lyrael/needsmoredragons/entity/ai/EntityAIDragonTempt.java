package au.lyrael.needsmoredragons.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static au.lyrael.needsmoredragons.NeedsMoreDragons.MOD_ID;

public class EntityAIDragonTempt extends EntityAIBase {

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
    /**
     * The player that is tempting the entity that is using this AI.
     */
    private EntityPlayer temptingPlayer;
    /**
     * A counter that is decremented each time the shouldExecute method is called. The shouldExecute method will always
     * return false if delayTemptCounter is greater than 0.
     */
    private int delayTemptCounter;
    /**
     * True if this EntityAIDragonTempt task is running
     */
    private boolean isRunning;
    /**
     * Whether the entity using this AI will be scared by the tempter's sudden movement.
     */
    private boolean scaredBySuddenMovement;

    public EntityAIDragonTempt(ITemptable temptableEntity, double entityMoveSpeed, boolean scaredBySuddenMovement) {
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
        this.temptingPlayer = this.temptableEntity.getWorldObj().getClosestPlayerToEntity(this.temptableEntity.asEntityLiving(), 10.0D);

        if (this.temptingPlayer == null) {
            return false;
        } else {
            ItemStack itemstack = this.temptingPlayer.getCurrentEquippedItem();
            final boolean isTempted = this.temptableEntity.isTemptedBy(itemstack);
            if (isTempted && !isRunning())
                LOGGER.debug("Temptable creature [{}] is tempted by [{}]'s [{}]", temptableEntity, temptingPlayer.getDisplayName(), itemstack);
            return isTempted;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting() {
        if (this.scaredBySuddenMovement) {
            if (this.temptableEntity.asEntityLiving().getDistanceSqToEntity(this.temptingPlayer) < 36.0D) {
                if (this.temptingPlayer.getDistanceSq(this.temptingPlayerPreviousX, this.temptingPlayerPreviousY, this.temptingPlayerPreviousZ) > 0.010000000000000002D) {
                    LOGGER.debug("[{}] was scared by [{}]'s sudden body movement.", temptableEntity, temptingPlayer.getDisplayName());
                    return false;
                }

                if (Math.abs((double) this.temptingPlayer.rotationPitch - this.temptingPlayerPreviousRotationPitch) > 5.0D
                        || Math.abs((double) this.temptingPlayer.rotationYaw - this.temptingPlayerPreviousRotationYaw) > 5.0D) {
                    LOGGER.debug("[{}] was scared by [{}]'s sudden head movement.", temptableEntity, temptingPlayer.getDisplayName());
                    return false;
                }
            } else {
                this.temptingPlayerPreviousX = this.temptingPlayer.posX;
                this.temptingPlayerPreviousY = this.temptingPlayer.posY;
                this.temptingPlayerPreviousZ = this.temptingPlayer.posZ;
            }

            this.temptingPlayerPreviousRotationPitch = (double) this.temptingPlayer.rotationPitch;
            this.temptingPlayerPreviousRotationYaw = (double) this.temptingPlayer.rotationYaw;
        }

        return this.shouldExecute();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.temptingPlayerPreviousX = this.temptingPlayer.posX;
        this.temptingPlayerPreviousY = this.temptingPlayer.posY;
        this.temptingPlayerPreviousZ = this.temptingPlayer.posZ;
        this.temptingPlayerPreviousRotationPitch = (double) this.temptingPlayer.rotationPitch;
        this.temptingPlayerPreviousRotationYaw = (double) this.temptingPlayer.rotationYaw;

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

        this.temptingPlayer = null;
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

        temptedEntityLiving.getLookHelper().setLookPositionWithEntity(this.temptingPlayer, 30.0F, (float) temptedEntityLiving.getVerticalFaceSpeed());

        if (temptedEntityLiving.getDistanceSqToEntity(this.temptingPlayer) < 6.25D) {
            temptedEntityLiving.getNavigator().clearPathEntity();
        } else {
            temptedEntityLiving.getNavigator().tryMoveToEntityLiving(this.temptingPlayer, this.entityMoveSpeed);
        }
    }

    /**
     * @see #isRunning
     */
    public boolean isRunning() {
        return this.isRunning;
    }
}