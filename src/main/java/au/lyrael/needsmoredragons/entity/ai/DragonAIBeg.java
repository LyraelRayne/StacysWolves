package au.lyrael.needsmoredragons.entity.ai;


import au.lyrael.needsmoredragons.entity.dragon.EntityDragonBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class DragonAIBeg extends EntityAIBase
{
    private EntityDragonBase theDragon;
    private EntityPlayer thePlayer;
    private World worldObject;
    private float minPlayerDistance;
    private int durationRemaining;
    private static final String __OBFID = "CL_00001576";

    public DragonAIBeg(EntityDragonBase dragon, float range)
    {
        this.theDragon = dragon;
        this.worldObject = dragon.worldObj;
        this.minPlayerDistance = range;
        this.setMutexBits(2);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        this.thePlayer = this.worldObject.getClosestPlayerToEntity(this.theDragon, (double)this.minPlayerDistance);
        return this.thePlayer == null ? false : this.hasPlayerGotBoneInHand(this.thePlayer);
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return !this.thePlayer.isEntityAlive() ? false : (this.theDragon.getDistanceSqToEntity(this.thePlayer) > (double)(this.minPlayerDistance * this.minPlayerDistance) ? false : this.durationRemaining > 0 && this.hasPlayerGotBoneInHand(this.thePlayer));
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.theDragon.setBegging(true);
        this.durationRemaining = 40 + this.theDragon.getRNG().nextInt(40);
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        this.theDragon.setBegging(false);
        this.thePlayer = null;
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        this.theDragon.getLookHelper().setLookPosition(this.thePlayer.posX, this.thePlayer.posY + (double)this.thePlayer.getEyeHeight(), this.thePlayer.posZ, 10.0F, (float)this.theDragon.getVerticalFaceSpeed());
        --this.durationRemaining;
    }

    /**
     * Gets if the Player has the Bone in the hand.
     */
    private boolean hasPlayerGotBoneInHand(EntityPlayer player)
    {
        ItemStack itemstack = player.inventory.getCurrentItem();
        return itemstack == null ? false : (!this.theDragon.isTamed() && itemstack.getItem() == Items.bone ? true : this.theDragon.isDragonBreedingItem(itemstack));
    }
}