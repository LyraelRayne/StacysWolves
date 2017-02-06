package au.lyrael.stacywolves.entity.ai;

import au.lyrael.stacywolves.entity.wolf.EntityWolfBase;
import net.minecraft.entity.ai.EntityAIFollowOwner;


public class EntityAIWolfFollowOwner extends EntityAIFollowOwner
{
	private EntityWolfBase wolf;

	public EntityAIWolfFollowOwner(EntityWolfBase wolf, double p_i1625_2_, float p_i1625_4_, float p_i1625_5_)
	{
        super(wolf, p_i1625_2_, p_i1625_4_, p_i1625_5_);
        this.wolf = wolf;
	}

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean continueExecuting() {
        return getWolf().shouldFollowOwner() && super.continueExecuting();
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {
        return getWolf().shouldFollowOwner() && super.shouldExecute();
    }

    protected EntityWolfBase getWolf() {
        return wolf;
    }
}
