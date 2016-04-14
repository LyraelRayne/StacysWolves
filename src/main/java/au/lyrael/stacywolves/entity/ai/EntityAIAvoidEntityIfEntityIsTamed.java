package au.lyrael.stacywolves.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.passive.EntityTameable;

import java.util.List;

public class EntityAIAvoidEntityIfEntityIsTamed extends EntityAIAvoidEntity {

    private EntityCreature self;
    private Class<? extends EntityTameable> creatureToAvoid;
    private float range;

    public EntityAIAvoidEntityIfEntityIsTamed(EntityCreature self, Class<? extends EntityTameable> creatureToAvoid, float range, double farSpeed, double nearSpeed) {
        super(self, creatureToAvoid, range, farSpeed, nearSpeed);
        this.self = self;
        this.creatureToAvoid = creatureToAvoid;
        this.range = range;
    }


    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {
        if (super.shouldExecute()) {
            List list = getSelf().worldObj.selectEntitiesWithinAABB(getCreatureToAvoid(), getSelf().boundingBox.expand((double) this.getRange(), 3.0D, (double) this.getRange()), this.field_98218_a);

            if (list.isEmpty()) {
                return false;
            }

            Entity closestLivingEntity = (Entity) list.get(0);

            if (creatureToAvoid.isAssignableFrom(closestLivingEntity.getClass())) {
                EntityTameable tameable = (EntityTameable) closestLivingEntity;
                return tameable.isTamed();
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    protected EntityCreature getSelf() {
        return self;
    }

    protected float getRange() {
        return range;
    }

    public Class<? extends Entity> getCreatureToAvoid() {
        return creatureToAvoid;
    }
}
