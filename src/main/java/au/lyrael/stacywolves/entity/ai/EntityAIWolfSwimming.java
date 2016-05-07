package au.lyrael.stacywolves.entity.ai;

import au.lyrael.stacywolves.entity.wolf.EntityWolfBase;
import net.minecraft.entity.ai.EntityAIBase;


public class EntityAIWolfSwimming extends EntityAIBase {
    private EntityWolfBase theEntity;
    private static final String __OBFID = "CL_00001584";

    public EntityAIWolfSwimming(EntityWolfBase p_i1624_1_) {
        this.theEntity = p_i1624_1_;
        this.setMutexBits(4);
        p_i1624_1_.getNavigator().setCanSwim(true);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        return this.theEntity.isInWater() || this.theEntity.handleLavaMovement();
    }

    /**
     * Updates the task
     */
    public void updateTask() {
        if (theEntity.shouldSwimToSurface() && this.theEntity.getRNG().nextFloat() < 0.8F) {
            this.theEntity.getJumpHelper().setJumping();
        }
    }
}
