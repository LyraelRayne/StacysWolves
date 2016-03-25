package au.lyrael.needsmoredragons.entity.dragon;

import au.lyrael.needsmoredragons.client.render.IRenderableDragon;
import au.lyrael.needsmoredragons.entity.ai.EntityAIAvoidEntityIfEntityIsTamed;
import au.lyrael.needsmoredragons.entity.ai.EntityAIDragonSit;
import au.lyrael.needsmoredragons.entity.ai.EntityAIDragonTempt;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public abstract class EntityDragonBase extends EntityTameable implements IDragon, IRenderableDragon {

    private float field_70926_e;
    private float field_70924_f;
    /**
     * true is the wolf is wet else false
     */
    private boolean isShaking;
    private boolean field_70928_h;
    /**
     * This time increases while wolf is shaking and emitting water particles.
     */
    private float timeWolfIsShaking;
    private float prevTimeWolfIsShaking;
    private final List<ItemStack> edibleItems = new ArrayList<>();
    private EntityAIDragonTempt aiTempt;


    public EntityDragonBase(World world) {
        super(world);
        this.setSize(0.6F, 0.8F);
        this.getNavigator().setAvoidsWater(true);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, this.aiSit);
        this.tasks.addTask(3, this.aiTempt = new EntityAIDragonTempt(this, 0.6D, true));
        this.tasks.addTask(4, new EntityAIAvoidEntity(this, EntityPlayer.class, 16.0F, 0.8D, 1.33D));
        this.tasks.addTask(5, new EntityAIFollowOwner(this, 1.0D, 10.0F, 5.0F));
        this.tasks.addTask(6, new EntityAIDragonSit(this, 1.33D));
        this.tasks.addTask(7, new EntityAILeapAtTarget(this, 0.3F));
        this.tasks.addTask(8, new EntityAIOcelotAttack(this));
        this.tasks.addTask(9, new EntityAIMate(this, 0.8D));
        this.tasks.addTask(10, new EntityAIWander(this, 0.8D));
        this.tasks.addTask(11, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0F));
        this.setTamed(false);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5D);

        if (this.isTamed()) {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(50.0D);
        } else {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(15.0D);
        }
    }

    /**
     * Returns true if the newer Entity AI code should be run
     */
    @Override
    public boolean isAIEnabled() {
        return true;
    }

    /**
     * Sets the active target the Task system uses for tracking
     */
    @Override
    public void setAttackTarget(EntityLivingBase target) {
        super.setAttackTarget(target);

        if (target == null) {
            this.setAngry(false);
        } else if (!this.isTamed()) {
            this.setAngry(true);
        }
    }

    /**
     * main AI tick function, replaces updateEntityActionState
     */
    @Override
    protected void updateAITick() {
        this.dataWatcher.updateObject(18, Float.valueOf(this.getHealth()));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(18, new Float(this.getHealth()));
        this.dataWatcher.addObject(19, new Byte((byte) 0));
        this.dataWatcher.addObject(20, new Byte((byte) BlockColored.func_150032_b(1)));
    }

    /**
     * Fired whenever the entity moves
     */
    @Override
    protected void func_145780_a(int x, int y, int z, Block surface) {
        this.playSound("mob.wolf.step", 0.15F, 1.0F);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setBoolean("Angry", this.isAngry());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        this.setAngry(nbt.getBoolean("Angry"));
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
    protected String getLivingSound() {
        return this.isAngry() ? "mob.wolf.growl" : (this.rand.nextInt(3) == 0 ? (this.isTamed() && this.dataWatcher.getWatchableObjectFloat(18) < 10.0F ? "mob.wolf.whine" : "mob.wolf.panting") : "mob.wolf.bark");
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
    protected String getHurtSound() {
        return "mob.wolf.hurt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
    protected String getDeathSound() {
        return "mob.wolf.death";
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    protected Item getDropItem() {
        return Item.getItemById(-1);
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        if (!this.worldObj.isRemote && this.isShaking && !this.field_70928_h && !this.hasPath() && this.onGround) {
            this.field_70928_h = true;
            this.timeWolfIsShaking = 0.0F;
            this.prevTimeWolfIsShaking = 0.0F;
            this.worldObj.setEntityState(this, (byte) 8);
        }

        if (scaresCreepers())
            scareCreepers();

    }

    protected void scareCreepers() {
        final float scareRange = 30F;

        final IEntitySelector targetSelector = new IEntitySelector() {
            /**
             * Return whether the specified entity is applicable to this filter.
             */
            public boolean isEntityApplicable(Entity subject) {
                return EntityCreeper.class.isAssignableFrom(subject.getClass()) && subject.isEntityAlive() && getEntitySenses().canSee(subject);
            }
        };

        List<EntityCreature> creepersInRange = getWorldObj().selectEntitiesWithinAABB(EntityCreeper.class,
                boundingBox.expand(scareRange, 3.0D, scareRange), targetSelector);

        for (EntityCreature entityCreature : creepersInRange) {
            if (!isAlreadyScared(entityCreature)) {
                entityCreature.tasks.addTask(0, new EntityAIAvoidEntityIfEntityIsTamed(entityCreature, this.getClass(), scareRange, 1.0D, 1.2D));
            }
        }
    }

    protected boolean isAlreadyScared(EntityCreature entityCreature) {
        final List<EntityAITasks.EntityAITaskEntry> taskEntries = entityCreature.tasks.taskEntries;
        for (EntityAITasks.EntityAITaskEntry taskEntry : taskEntries) {
            if (taskEntry.action instanceof EntityAIAvoidEntityIfEntityIsTamed) {
                return ((EntityAIAvoidEntityIfEntityIsTamed) taskEntry.action).getCreatureToAvoid().equals(this.getClass());
            }
        }
        return false;
    }

    protected boolean scaresCreepers() {
        return isTamed() && true;
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate() {
        super.onUpdate();
        this.field_70924_f = this.field_70926_e;

        if (this.isBegging()) {
            this.field_70926_e += (1.0F - this.field_70926_e) * 0.4F;
        } else {
            this.field_70926_e += (0.0F - this.field_70926_e) * 0.4F;
        }

        if (this.isBegging()) {
            this.numTicksToChaseTarget = 10;
        }

        if (this.isWet()) {
            this.isShaking = true;
            this.field_70928_h = false;
            this.timeWolfIsShaking = 0.0F;
            this.prevTimeWolfIsShaking = 0.0F;
        } else if ((this.isShaking || this.field_70928_h) && this.field_70928_h) {
            if (this.timeWolfIsShaking == 0.0F) {
                this.playSound("mob.wolf.shake", this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            }

            this.prevTimeWolfIsShaking = this.timeWolfIsShaking;
            this.timeWolfIsShaking += 0.05F;

            if (this.prevTimeWolfIsShaking >= 2.0F) {
                this.isShaking = false;
                this.field_70928_h = false;
                this.prevTimeWolfIsShaking = 0.0F;
                this.timeWolfIsShaking = 0.0F;
            }

            if (this.timeWolfIsShaking > 0.4F) {
                float f = (float) this.boundingBox.minY;
                int i = (int) (MathHelper.sin((this.timeWolfIsShaking - 0.4F) * (float) Math.PI) * 7.0F);

                for (int j = 0; j < i; ++j) {
                    float f1 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
                    float f2 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
                    this.worldObj.spawnParticle("splash", this.posX + (double) f1, (double) (f + 0.8F), this.posZ + (double) f2, this.motionX, this.motionY, this.motionZ);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public boolean isShaking() {
        return this.isShaking;
    }

    /**
     * Used when calculating the amount of shading to apply while the wolf is shaking.
     */
    @SideOnly(Side.CLIENT)
    public float getShadingWhileShaking(float p_70915_1_) {
        return 0.75F + (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * p_70915_1_) / 2.0F * 0.25F;
    }

    @SideOnly(Side.CLIENT)
    public float getShakeAngle(float p_70923_1_, float p_70923_2_) {
        float f2 = (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * p_70923_1_ + p_70923_2_) / 1.8F;

        if (f2 < 0.0F) {
            f2 = 0.0F;
        } else if (f2 > 1.0F) {
            f2 = 1.0F;
        }

        return MathHelper.sin(f2 * (float) Math.PI) * MathHelper.sin(f2 * (float) Math.PI * 11.0F) * 0.15F * (float) Math.PI;
    }

    @Override
    public float getEyeHeight() {
        return this.height * 0.8F;
    }

    @SideOnly(Side.CLIENT)
    public float getInterestedAngle(float maybeTime) {
        return (this.field_70924_f + (this.field_70926_e - this.field_70924_f) * maybeTime) * 0.15F * (float) Math.PI;
    }

    /**
     * The speed it takes to move the entityliving's rotationPitch through the faceEntity method. This is only currently
     * use in wolves.
     */
    @Override
    public int getVerticalFaceSpeed() {
        return this.isSitting() ? 20 : super.getVerticalFaceSpeed();
    }

    /**
     * Called when the entity is attacked.
     */
    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float amount) {
        if (this.isEntityInvulnerable()) {
            return false;
        } else {
            Entity entity = damageSource.getEntity();
            this.aiSit.setSitting(false);

            if (entity != null && !(entity instanceof EntityPlayer) && !(entity instanceof EntityArrow)) {
                amount = (amount + 1.0F) / 2.0F;
            }

            return super.attackEntityFrom(damageSource, amount);
        }
    }

    @Override
    public boolean attackEntityAsMob(Entity target) {
        int i = this.isTamed() ? 4 : 2;
        return target.attackEntityFrom(DamageSource.causeMobDamage(this), (float) i);
    }

    @Override
    public void setTamed(boolean tamed) {
        super.setTamed(tamed);

        if (tamed) {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0D);
        } else {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0D);
        }
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     *
     * @return
     */
    @Override
    public boolean interact(EntityPlayer player) {
        ItemStack itemstack = player.inventory.getCurrentItem();

        if (this.isTamed()) {
            if (itemstack != null) {
                if (canEat(itemstack)) {

                    if (this.dataWatcher.getWatchableObjectFloat(18) < 20.0F) {
                        consumeHeldItem(player, itemstack);
                        this.heal(getHealAmount(itemstack));
                        return true;
                    }
                }
            }

            if (this.func_152114_e(player) && !this.worldObj.isRemote && !this.isDragonBreedingItem(itemstack)) {
                toggleSitting();
            }

        } else if (this.aiTempt.isRunning() && itemstack != null && this.likes(itemstack) && !this.isAngry()) {
            consumeHeldItem(player, itemstack);
            becomeTamedBy(player);
            return true;
        }

        return super.interact(player);
    }

    protected void toggleSitting() {
        this.aiSit.setSitting(!this.isSitting());
        this.isJumping = false;
        this.setPathToEntity(null);
        this.setTarget(null);
        this.setAttackTarget(null);
    }

    protected void becomeTamedBy(EntityPlayer player) {
        if (!this.worldObj.isRemote) {
            if (this.rand.nextInt(3) == 0) {
                this.setTamed(true);
                this.setPathToEntity(null);
                this.setAttackTarget(null);
                this.aiSit.setSitting(true);
                this.setHealth(20.0F);
                this.func_152115_b(player.getUniqueID().toString());
                this.playTameEffect(true);
                this.worldObj.setEntityState(this, (byte) 7);
            } else {
                this.playTameEffect(false);
                this.worldObj.setEntityState(this, (byte) 6);
            }
        }
    }

    protected void consumeHeldItem(EntityPlayer player, ItemStack itemstack) {
        if (!player.capabilities.isCreativeMode) {
            --itemstack.stackSize;
        }

        if (itemstack.stackSize <= 0) {
            player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
        }
    }

    /**
     * Tests whether the dragon can eat a particular item.
     *
     * @param itemStack
     * @return
     */
    @Override
    public boolean canEat(ItemStack itemStack) {
        final Class<? extends Item> itemType = itemStack.getItem().getClass();
        for (ItemStack food : getEdibleItems()) {
            if (food.getItem().getClass().isAssignableFrom(itemType))
                return true;
        }
        return false;
    }

    protected List<ItemStack> getEdibleItems() {
        return new ArrayList<>(this.edibleItems);
    }

    protected void addEdibleItem(ItemStack edibleItem) {
        this.edibleItems.add(edibleItem);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleHealthUpdate(byte updateFlags) {
        if (updateFlags == 8) {
            this.field_70928_h = true;
            this.timeWolfIsShaking = 0.0F;
            this.prevTimeWolfIsShaking = 0.0F;
        } else {
            super.handleHealthUpdate(updateFlags);
        }
    }

    @SideOnly(Side.CLIENT)
    public float getTailRotation() {
        return this.isAngry() ? 1.5393804F : (this.isTamed() ? (0.55F - (20.0F - this.dataWatcher.getWatchableObjectFloat(18)) * 0.02F) * (float) Math.PI : ((float) Math.PI / 5F));
    }

    @Override
    public boolean isBreedingItem(ItemStack itemStack) {
        return isDragonBreedingItem(itemStack);
    }

    /**
     * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on
     * the animal type)
     */
    @Override
    public boolean isDragonBreedingItem(ItemStack itemStack) {
        return itemStack == null ? false : (!(itemStack.getItem() instanceof ItemFood) ? false : ((ItemFood) itemStack.getItem()).isWolfsFavoriteMeat());
    }


    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    @Override
    public int getMaxSpawnedInChunk() {
        return 8;
    }

    /**
     * Determines whether this wolf is angry or not.
     */
    public boolean isAngry() {
        return (this.dataWatcher.getWatchableObjectByte(16) & 2) != 0;
    }

    /**
     * Sets whether this wolf is angry or not.
     */
    public void setAngry(boolean angry) {
        byte b0 = this.dataWatcher.getWatchableObjectByte(16);

        if (angry) {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte) (b0 | 2)));
        } else {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte) (b0 & -3)));
        }
    }
//
//    /**
//     * Return this wolf's collar color.
//     */
//    public int getCollarColor() {
//        return this.dataWatcher.getWatchableObjectByte(20) & 15;
//    }
//
//    /**
//     * Set this wolf's collar color.
//     */
//    public void setCollarColor(int color) {
//        this.dataWatcher.updateObject(20, Byte.valueOf((byte) (color & 15)));
//    }

    @Override
    public abstract EntityDragonBase createChild(EntityAgeable parent);

    public void setBegging(boolean isBegging) {
        if (isBegging) {
            this.dataWatcher.updateObject(19, Byte.valueOf((byte) 1));
        } else {
            this.dataWatcher.updateObject(19, Byte.valueOf((byte) 0));
        }
    }

    public boolean isBegging() {
        return this.dataWatcher.getWatchableObjectByte(19) == 1;
    }

    /**
     * Another method that's here because I want a non-obfuscated name for my method.
     *
     * @param potentialMate
     * @return
     */
    public boolean canMateWith(EntityAnimal potentialMate) {
        return canDragonMateWith(potentialMate);
    }

    /**
     * Returns true if the mob is currently able to mate with the specified mob.
     */
    @Override
    public boolean canDragonMateWith(EntityAnimal potentialMate) {
        // Dragons cannot mate with themselves.
        return potentialMate != this &&
                // Can only breed tamed dragons
                this.isTamed() &&
                // Can only mate with eligible species.
                canMateWithSpecies(potentialMate) &&
                // Dragons cannot mate with a partner who is not tamed or who is sitting down.
                isNotSittingOrUntamed(potentialMate) &&
                // Gotta be feeling the love if I'm going to mate.
                this.isInLove() &&
                // My partner's gotta be feeling it too!
                potentialMate.isInLove();
    }

    /**
     * Test whether an animal is sitting or untamed. An animal is considered untamed if it is tameable but has not been tamed.
     * For the purposes of this check, an untameable animal is considered tamed.
     *
     * @param potentialMate Animal to test for sitting and untamed conditions.
     * @return True if the animal is not tameable or the tameable animal is tamed and not sitting.
     */
    private boolean isNotSittingOrUntamed(EntityAnimal potentialMate) {
        if (potentialMate instanceof EntityTameable) {
            EntityTameable tameableMate = (EntityTameable) potentialMate;
            if (!tameableMate.isTamed() || tameableMate.isSitting()) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    /**
     * Can I mate with animals {@code potentialMate}'s species?
     *
     * @param potentialMate The potential mate to test for suitability
     * @return True if {@code potentialMate}'s species is the same as mine.
     */
    protected boolean canMateWithSpecies(EntityAnimal potentialMate) {
        return potentialMate.getClass() == this.getClass();
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    @Override
    protected boolean canDespawn() {
        return !this.isTamed() && this.ticksExisted > 2400;
    }

    /**
     * Used by the owner hurt by target AI in determining whether the wolf should attack their owner's attacker.
     *
     * @param ownerAttacker The entity which attacked the owner.
     * @param attackedOwner The owner who was attacked
     * @return True if the tamable thing should attack?
     */
    @Override
    public boolean func_142018_a(EntityLivingBase ownerAttacker, EntityLivingBase attackedOwner) {
        if (!(ownerAttacker instanceof EntityCreeper) && !(ownerAttacker instanceof EntityGhast)) {
            if (ownerAttacker instanceof EntityDragonBase) {
                EntityDragonBase entitywolf = (EntityDragonBase) ownerAttacker;

                if (entitywolf.isTamed() && entitywolf.getOwner() == attackedOwner) {
                    return false;
                }
            }

            return ownerAttacker instanceof EntityPlayer && attackedOwner instanceof EntityPlayer &&
                    !((EntityPlayer) attackedOwner).canAttackPlayer((EntityPlayer) ownerAttacker) ?
                    false :
                    !(ownerAttacker instanceof EntityHorse) || !((EntityHorse) ownerAttacker).isTame();
        } else {
            return false;
        }
    }

    @Override
    public boolean likes(ItemStack itemStack) {
        return canEat(itemStack);
    }

    @Override
    public boolean isTemptedBy(ItemStack itemStack) {
        return likes(itemStack);
    }

    @Override
    public boolean normallyAvoidsWater() {
        return true;
    }

    @Override
    public boolean alwaysAvoidsWater() {
        return false;
    }

    @Override
    public EntityLiving asEntityLiving() {
        return this;
    }

    @Override
    public World getWorldObj() {
        return this.worldObj;
    }

    /**
     * NOTE: This method basically exists because I wanted a method on the dragon interface to be used by other stuff.
     * <p>
     * Don't replace it with isShaking because the obfuscator renames it and causes AbstractMethodExceptions.
     */
    @Override
    public boolean isDragonShaking() {
        return isShaking();
    }

    /**
     * NOTE: This method basically exists because I wanted a method on the dragon interface to be used by other stuff.
     * <p>
     * Don't replace it with isShaking because the obfuscator renames it and causes AbstractMethodExceptions.
     */
    @Override
    public boolean isDragonAngry() {
        return isAngry();
    }

    /**
     * NOTE: This method basically exists because I wanted a method on the dragon interface to be used by other stuff.
     * <p>
     * Don't replace it with isShaking because the obfuscator renames it and causes AbstractMethodExceptions.
     */
    @Override
    public boolean isDragonTamed() {
        return isTamed();
    }

}