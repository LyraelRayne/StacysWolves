package au.lyrael.stacywolves.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;

/**
 * Copy of ocelot sit AI.
 */
public class EntityAIWolfSit extends EntityAIBase {
    private final EntityTameable entity;
    private final double wolfMoveSpeed;
    private int executionTime;
    private int distanceMoved;
    private int field_151490_e;
    private int wolfXPos;
    private int wolfYPos;
    private int wolfZPos;

    public EntityAIWolfSit(EntityTameable entity, double moveSpeed) {
        this.entity = entity;
        this.wolfMoveSpeed = moveSpeed;
        this.setMutexBits(5);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        return this.entity.isTamed() && !this.entity.isSitting() && this.entity.getRNG().nextDouble() <= 0.006500000134110451D && this.func_151485_f();
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting() {
        return this.executionTime <= this.field_151490_e && this.distanceMoved <= 60 && this.shouldSitOn(this.entity.worldObj, this.wolfXPos, this.wolfYPos, this.wolfZPos);
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.entity.getNavigator().tryMoveToXYZ((double) ((float) this.wolfXPos) + 0.5D, (double) (this.wolfYPos + 1), (double) ((float) this.wolfZPos) + 0.5D, this.wolfMoveSpeed);
        this.executionTime = 0;
        this.distanceMoved = 0;
        this.field_151490_e = this.entity.getRNG().nextInt(this.entity.getRNG().nextInt(1200) + 1200) + 1200;
        getRegularEntitySitAI(this.entity).setSitting(false);
    }

    /**
     * Resets the task
     */
    public void resetTask() {
        this.entity.setSitting(false);
    }

    /**
     * Updates the task
     */
    public void updateTask() {
        ++this.executionTime;

        getRegularEntitySitAI(this.entity).setSitting(false);

        if (this.entity.getDistanceSq((double) this.wolfXPos, (double) (this.wolfYPos + 1), (double) this.wolfZPos) > 1.0D) {
            this.entity.setSitting(false);
            this.entity.getNavigator().tryMoveToXYZ((double) ((float) this.wolfXPos) + 0.5D, (double) (this.wolfYPos + 1), (double) ((float) this.wolfZPos) + 0.5D, this.wolfMoveSpeed);
            ++this.distanceMoved;
        } else if (!this.entity.isSitting()) {
            this.entity.setSitting(true);
        } else {
            --this.distanceMoved;
        }
    }

    private EntityAISit getRegularEntitySitAI(EntityTameable entity) {
        return entity.func_70907_r();
    }

    private boolean func_151485_f() {
        int i = (int) this.entity.posY;
        double d0 = 2.147483647E9D;

        for (int j = (int) this.entity.posX - 8; (double) j < this.entity.posX + 8.0D; ++j) {
            for (int k = (int) this.entity.posZ - 8; (double) k < this.entity.posZ + 8.0D; ++k) {
                if (this.shouldSitOn(this.entity.worldObj, j, i, k) && this.entity.worldObj.isAirBlock(j, i + 1, k)) {
                    double d1 = this.entity.getDistanceSq((double) j, (double) i, (double) k);

                    if (d1 < d0) {
                        this.wolfXPos = j;
                        this.wolfYPos = i;
                        this.wolfZPos = k;
                        d0 = d1;
                    }
                }
            }
        }

        return d0 < 2.147483647E9D;
    }

    /**
     * I think this is to determine whether to sit on a thing.
     *
     * @param world world the object is in
     * @param x     X co-ordinate of the thing
     * @param y     Y co-ordinate of the thing
     * @param z     Z co-ordinate of the thing
     * @return True if the wolf shouldsit on it.
     */
    private boolean shouldSitOn(World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        int metadata = world.getBlockMetadata(x, y, z);

        if (block == Blocks.chest) {
            TileEntityChest tileentitychest = (TileEntityChest) world.getTileEntity(x, y, z);

            if (tileentitychest.numPlayersUsing < 1) {
                return true;
            }
        } else {
            if (block == Blocks.lit_furnace) {
                return true;
            }

            if (block == Blocks.bed && !BlockBed.isBlockHeadOfBed(metadata)) {
                return true;
            }
        }

        return false;
    }
}