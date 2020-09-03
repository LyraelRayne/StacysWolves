package au.lyrael.stacywolves.entity.wolf;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.client.render.IRenderableWolf;
import au.lyrael.stacywolves.config.RuntimeConfiguration;
import au.lyrael.stacywolves.entity.ISpawnable;
import au.lyrael.stacywolves.entity.ai.*;
import au.lyrael.stacywolves.integration.PamsHarvestcraftHolder;
import au.lyrael.stacywolves.inventory.InventoryWolfChest;
import au.lyrael.stacywolves.item.ItemWolfFood;
import au.lyrael.stacywolves.item.WolfPeriodicItemDrop;
import au.lyrael.stacywolves.lighting.WolfLightSource;
import au.lyrael.stacywolves.registry.ItemRegistry;
import au.lyrael.stacywolves.registry.WolfType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemNameTag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.village.Village;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static au.lyrael.stacywolves.StacyWolves.MOD_ID;
import static au.lyrael.stacywolves.config.RuntimeConfiguration.allowedInPeaceful;
import static au.lyrael.stacywolves.config.RuntimeConfiguration.onlyOcelotWolvesScareCreepers;
import static au.lyrael.stacywolves.registry.ItemRegistry.isClicker;
import static au.lyrael.stacywolves.registry.ItemRegistry.isRemover;
import static au.lyrael.stacywolves.utility.WorldHelper.canSeeTheSky;
import static au.lyrael.stacywolves.utility.WorldHelper.getFullBlockLightValue;
import static net.minecraft.init.Blocks.*;


public abstract class EntityWolfBase extends EntityTameable implements IWolf, IRenderableWolf, ISpawnable
{
	private static final Random RANDOM = new Random();

	private WolfMetadata metadata;

	private float field_70926_e;
	private float field_70924_f;

	private boolean isShaking = false;
	private boolean wolfIsReadyToShake = false;
	private boolean isShakingToDrySelf = false;
	private boolean isShakingToDropItem = false;
	private boolean isDroppingItem = false;
	private boolean hasDroppedItem = false;

	private boolean shouldFollowOwner = true;

	private boolean bypassThrottleAndProbability = false;

	/**
	 * This time increases while wolf is shaking and emitting water particles.
	 */
	private float timeWolfIsShaking;
	private float prevTimeWolfIsShaking;
	private final List<ItemStack> edibleItems = new ArrayList<>();
	private final List<ItemStack> likedItems = new ArrayList<>();
	private EntityAIWolfTempt aiTempt;
	private final EntityAIWolfFollowOwner aiWolfFollowOwner;

	private static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	private static final Logger SPAWNLOGGER = LogManager.getLogger(MOD_ID + ".spawn");

	protected static final List<Block> NORMAL_FLOOR_BLOCKS = Arrays.asList(grass, dirt, gravel, sand, sandstone);
	protected static final List<Block> SUBTERRANEAN_FLOOR_BLOCKS = Arrays.asList(stone, gravel, cobblestone, obsidian);
	protected static final List<Block> INDOOR_FLOOR_BLOCKS = Arrays.asList(stone, cobblestone, planks, wooden_slab, stone_slab, double_stone_slab, double_wooden_slab);
	protected static final List<Block> ICE_FLOOR_BLOCKS = Arrays.asList(ice, snow, packed_ice, snow_layer);
	protected static final List<Block> SHROOM_FLOOR_BLOCKS = Arrays.asList((Block) mycelium, red_mushroom_block,
			brown_mushroom_block);
	protected static final List<Block> MESA_FLOOR_BLOCKS = Arrays.asList(sand, hardened_clay, stained_hardened_clay);

	private WolfPeriodicItemDrop periodicDrop;
	private WolfLightSource lightSource;
	private InventoryWolfChest wolfChest;

	public EntityWolfBase(World world)
	{
		super(world);
		this.setSize(0.6F, 0.8F);
		this.getNavigator().setAvoidsWater(this.normallyAvoidsWater() || this.alwaysAvoidsWater());
		this.tasks.addTask(1, new EntityAIWolfSwimming(this));
		this.tasks.addTask(2, this.aiSit);
		this.setAiTempt(new EntityAIWolfTempt(this, 0.5D, false));
		this.tasks.addTask(3, this.getAiTempt());
		this.tasks.addTask(4, new EntityAIAttackOnCollide(this, 1.0D, true));
		aiWolfFollowOwner = new EntityAIWolfFollowOwner(this, 1.0D, 10.0F, 5.0F);
		this.tasks.addTask(5, aiWolfFollowOwner);
		this.tasks.addTask(7, new EntityAILeapAtTarget(this, 0.4F));
		this.tasks.addTask(8, new WolfAIBeg(this, 8.0F));
		this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(9, new EntityAIWolfMate(this, 1.0D));
		this.tasks.addTask(9, new EntityAILookIdle(this));
		this.tasks.addTask(10, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(11, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0F));
		this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
		this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
		this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true));
		if (RuntimeConfiguration.wolvesAttackAnimals)
		{
			this.targetTasks.addTask(4, new EntityAITargetNonTamed(this, EntitySheep.class, 400, false));
			this.targetTasks.addTask(4, new EntityAITargetNonTamed(this, EntityCow.class, 400, false));
			this.targetTasks.addTask(4, new EntityAITargetNonTamed(this, EntityChicken.class, 400, false));
		}

		this.setTamed(false);

		this.metadata = this.getClass().getAnnotation(WolfMetadata.class);

		this.setupEdibleItems();

		this.renderDistanceWeight = 10.0D;
	}

	protected WolfMetadata getMetadata()
	{
		if (this.metadata == null) {
			this.metadata = this.getClass().getAnnotation(WolfMetadata.class);
			if (this.metadata == null) {
				throw new RuntimeException("Wolf metadata is not able to be retrieved. This should be impossible.");
			}

		}
		return this.metadata;
	}

	private void setupEdibleItems()
	{
		this.addEdibleItem(new ItemStack(Items.beef));
		this.addEdibleItem(new ItemStack(Items.chicken));
		final ItemStack blackberryJellySandwichItemStack = PamsHarvestcraftHolder.getBlackberryJellySandwichItemStack();
		if (blackberryJellySandwichItemStack != null)
		{
			this.addEdibleItem(blackberryJellySandwichItemStack);
		}
	}

	protected Village scanForVillage(int villageScanRadius) {
		return getWorldObj().villageCollectionObj.findNearestVillage(
				MathHelper.floor_double(this.posX),
				MathHelper.floor_double(this.posY),
				MathHelper.floor_double(this.posZ), villageScanRadius);
	}

	protected List<Block> getFloorBlocks()
	{
		return NORMAL_FLOOR_BLOCKS;
	}


	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3D);

		if (this.isTamed())
		{
			this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(30.0D);
		}
		else
		{
			this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
		}
	}

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	@Override
	public boolean isAIEnabled()
	{
		return true;
	}

	/**
	 * Sets the active target the Task system uses for tracking
	 */
	@Override
	public void setAttackTarget(EntityLivingBase target)
	{
		super.setAttackTarget(target);

		if (target == null)
		{
			this.setAngry(false);
		}
		else if (!this.isTamed())
		{
			this.setAngry(true);
		}
	}

	public boolean shouldFollowOwner()
	{
		return this.shouldFollowOwner;
	}

	/**
	 * main AI tick function, replaces updateEntityActionState
	 */
	@Override
	protected void updateAITick()
	{
		this.dataWatcher.updateObject(18, Float.valueOf(this.getHealth()));
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataWatcher.addObject(18, new Float(this.getHealth()));
		this.dataWatcher.addObject(19, new Byte((byte) 0));
		this.dataWatcher.addObject(20, new Byte((byte) BlockColored.func_150032_b(1)));
	}

	/**
	 * Fired whenever the entity moves
	 */
	@Override
	protected void func_145780_a(int x, int y, int z, Block surface)
	{
		this.playSound("mob.wolf.step", 0.15F, 1.0F);
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		nbt.setBoolean("Angry", this.isAngry());
		nbt.setByte("CollarColor", (byte) this.getCollarColor());
		nbt.setBoolean("FollowingOwner", this.shouldFollowOwner);
		final InventoryWolfChest wolfChest = this.getWolfChest();
		if (wolfChest != null)
			wolfChest.writeEntityToNBT(nbt);
	}

	@Override
	public float getWolfBrightness(float maybeTime)
	{
		return isOffsetPositionInLiquid(posX, posY, posZ) ? 1F : getBrightness(maybeTime);
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);
		this.setAngry(nbt.getBoolean("Angry"));
		if (nbt.hasKey("CollarColor", 99))
		{
			this.setCollarColor(nbt.getByte("CollarColor"));
		}
		this.shouldFollowOwner = nbt.getBoolean("FollowingOwner");
		final InventoryWolfChest wolfChest = this.getWolfChest();
		if (wolfChest != null)
			wolfChest.readEntityFromNBT(nbt);
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	@Override
	protected String getLivingSound()
	{
		return this.isAngry() ? "mob.wolf.growl"
				: (this.rand.nextInt(3) == 0 ? (this.isTamed() && getOtherWatchableHealthThing() < 10.0F
						? "mob.wolf.whine" : "mob.wolf.panting") : "mob.wolf.bark");
	}

	/**
	 * There appear to be two watchables for health. 18 is used by a lot of tamed wolf stuff, maybe this is tamed health?
	 *
	 * @return Datawatcher item #18
	 */
	private float getOtherWatchableHealthThing() {
		return this.dataWatcher.getWatchableObjectFloat(18);
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected String getHurtSound()
	{
		return "mob.wolf.hurt";
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	@Override
	protected String getDeathSound()
	{
		return "mob.wolf.death";
	}

	/**
	 * Returns the volume for the sounds this mob makes.
	 */
	@Override
	protected float getSoundVolume()
	{
		return 0.4F;
	}

	@Override
	protected Item getDropItem()
	{
		return Item.getItemById(-1);
	}

	/**
	 * Return this wolf's collar color.
	 */
	public int getCollarColor()
	{
		return this.dataWatcher.getWatchableObjectByte(20) & 15;
	}

	/**
	 * Set this wolf's collar color.
	 */
	public void setCollarColor(int p_82185_1_)
	{
		this.dataWatcher.updateObject(20, Byte.valueOf((byte) (p_82185_1_ & 15)));
	}

	/**
	 * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
	 * use this to react to sunlight and start to burn.
	 */
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		doLightingUpdate();

		if (!this.worldObj.isRemote && this.isShaking && !this.wolfIsReadyToShake && !this.hasPath() && this.onGround)
		{
			this.wolfIsReadyToShake = true;
			this.timeWolfIsShaking = 0.0F;
			this.prevTimeWolfIsShaking = 0.0F;
			this.worldObj.setEntityState(this, (byte) 8);
		}

		if (scaresCreepers())
			scareCreepers();

	}

	protected void doLightingUpdate() {
		final WolfLightSource lightSource = getLightSource();
		if (lightSource != null) {
			if (!this.isDead)
				lightSource.addLight();
			else
				lightSource.clearLighting();
		}
	}

	protected void scareCreepers()
	{
		final float scareRange = 30F;

		final IEntitySelector targetSelector = new IEntitySelector()
		{
			/**
			 * Return whether the specified entity is applicable to this filter.
			 */
			public boolean isEntityApplicable(Entity subject)
			{
				return EntityCreeper.class.isAssignableFrom(subject.getClass()) && subject.isEntityAlive()
						&& getEntitySenses().canSee(subject);
			}
		};

		List<EntityCreature> creepersInRange = getWorldObj().selectEntitiesWithinAABB(EntityCreeper.class,
				boundingBox.expand(scareRange, 3.0D, scareRange), targetSelector);

		for (EntityCreature entityCreature : creepersInRange)
		{
			if (!isAlreadyScared(entityCreature))
			{
				entityCreature.tasks.addTask(0,
						new EntityAIAvoidEntityIfEntityIsTamed(entityCreature, this.getClass(), scareRange, 1.0D, 1.2D));
			}
		}
	}

	protected boolean isAlreadyScared(EntityCreature entityCreature)
	{
		final List<EntityAITasks.EntityAITaskEntry> taskEntries = entityCreature.tasks.taskEntries;
		for (EntityAITasks.EntityAITaskEntry taskEntry : taskEntries)
		{
			if (taskEntry.action instanceof EntityAIAvoidEntityIfEntityIsTamed)
			{
				return ((EntityAIAvoidEntityIfEntityIsTamed) taskEntry.action).getCreatureToAvoid().equals(this.getClass());
			}
		}
		return false;
	}

	protected boolean scaresCreepers()
	{
		return !onlyOcelotWolvesScareCreepers && isTamed();
	}

	@Override
	protected void despawnEntity() {
		super.despawnEntity();
		if (this.isDead) LOGGER.trace("[{}] despawned", this);

	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate()
	{
		super.onUpdate();
		if (doPeacefulDespawn())
			return;

		this.field_70924_f = this.field_70926_e;

		doBeggingChase();

		if (isTamed() && !isDroppingItem && getPeriodicDrop() != null) {
			if (getPeriodicDrop().shouldItemDrop(this))
				this.isDroppingItem = true;
		}

		doWolfShaking();

		if(isBypassThrottleAndProbability() && this.ticksExisted > 60)
			this.setDead();
	}

	protected void doBeggingChase()
	{
		if (this.isBegging())
		{
			this.field_70926_e += (1.0F - this.field_70926_e) * 0.4F;
			this.numTicksToChaseTarget = 10;
		}
		else
		{
			this.field_70926_e += (0.0F - this.field_70926_e) * 0.4F;
		}
	}

	protected boolean doPeacefulDespawn()
	{
		if (!allowedInPeaceful && !this.worldObj.isRemote && !isWolfTamed() && this.worldObj.difficultySetting == EnumDifficulty.PEACEFUL)
		{
			this.setDead();
			return true;
		}
		return false;
	}

	/**
	 * Will get destroyed next tick.
	 */
	@Override
	public void setDead() {
		if (this.isTamed() && RuntimeConfiguration.tamedWolvesNeverDie) {
			LOGGER.warn("Warning: A wolf died.", new Exception("This is not really an exception. Just printing stacktrace"));
			LOGGER.warn("Unwated death happened to [{}]", this);
		}
		super.setDead();
		doLightingUpdate();
	}

	protected void doWolfShaking()
	{
		// NOTE: isWet means "is in water" rather than "has been in water"
		if (this.isWet())
		{
			if (!isShakingToDropItem && this.wolfIsReadyToShake) {
				this.prevTimeWolfIsShaking = 0.0F;
				this.timeWolfIsShaking = 0.0F;
			}
			this.isShaking = true;
			this.isShakingToDrySelf = true;
			this.wolfIsReadyToShake = false;

		} else if (this.isDroppingItem && !this.isShakingToDropItem) {
			this.isShaking = true;
			this.isShakingToDropItem = true;
			this.hasDroppedItem = false;
		} else if ((this.isShaking || this.wolfIsReadyToShake) && this.wolfIsReadyToShake)
		{
			if (this.timeWolfIsShaking == 0.0F)
			{
				this.playSound("mob.wolf.shake", this.getSoundVolume(),
						(this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			}

			this.prevTimeWolfIsShaking = this.timeWolfIsShaking;
			this.timeWolfIsShaking += 0.05F;

			if (this.prevTimeWolfIsShaking >= 2.0F)
			{
				this.isShaking = false;
				this.wolfIsReadyToShake = false;
				this.prevTimeWolfIsShaking = 0.0F;
				this.timeWolfIsShaking = 0.0F;
				this.isShakingToDropItem = false;
				this.isShakingToDrySelf = false;
				this.isDroppingItem = false;
			}

			if (this.timeWolfIsShaking > 0.4F)
			{
				if (isShakingToDrySelf) {
					shedWetParticles();
				}
				if (isTamed() && isShakingToDropItem && !hasDroppedItem) {
					getPeriodicDrop().dropItem(this);
					this.hasDroppedItem = true;
				}
			}
		}
	}

	private void shedWetParticles() {
		float f = (float) this.boundingBox.minY;
		int i = (int) (MathHelper.sin((this.timeWolfIsShaking - 0.4F) * (float) Math.PI) * 7.0F);

		for (int j = 0; j < i; ++j) {
			float f1 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
			float f2 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
			this.worldObj.spawnParticle("splash", this.posX + (double) f1, (double) (f + 0.8F), this.posZ + (double) f2,
					this.motionX, this.motionY, this.motionZ);
		}
	}

	public boolean isShaking()
	{
		return this.isShaking;
	}

	/**
	 * Used when calculating the amount of shading to apply while the wolf is shaking.
	 */
	public float getShadingWhileShaking(float p_70915_1_)
	{
		return 0.75F
				+ (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * p_70915_1_) / 2.0F * 0.25F;
	}

	public float getShakeAngle(float p_70923_1_, float p_70923_2_)
	{
		float f2 = (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * p_70923_1_ + p_70923_2_)
				/ 1.8F;

		if (f2 < 0.0F)
		{
			f2 = 0.0F;
		}
		else if (f2 > 1.0F)
		{
			f2 = 1.0F;
		}

		return MathHelper.sin(f2 * (float) Math.PI) * MathHelper.sin(f2 * (float) Math.PI * 11.0F) * 0.15F * (float) Math.PI;
	}

	@Override
	public float getEyeHeight()
	{
		return this.height * 0.8F;
	}

	public float getInterestedAngle(float maybeTime)
	{
		return (this.field_70924_f + (this.field_70926_e - this.field_70924_f) * maybeTime) * 0.15F * (float) Math.PI;
	}

	/**
	 * The speed it takes to move the entityliving's rotationPitch through the faceEntity method. This is only currently
	 * use in wolves.
	 */
	@Override
	public int getVerticalFaceSpeed()
	{
		return this.isSitting() ? 20 : super.getVerticalFaceSpeed();
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource damageSource, float amount)
	{
		if (this.isEntityInvulnerable())
		{
			return false;
		}
		else
		{
			Entity entity = damageSource.getEntity();
			this.aiSit.setSitting(false);

			if (entity != null && !(entity instanceof EntityPlayer) && !(entity instanceof EntityArrow))
			{
				amount = (amount + 1.0F) / 2.0F;
			}

			return super.attackEntityFrom(damageSource, amount);
		}
	}

	@Override
	public boolean attackEntityAsMob(Entity target)
	{
		int i = this.isTamed() ? 4 : 2;
		return target.attackEntityFrom(DamageSource.causeMobDamage(this), (float) i);
	}

	@Override
	public void setTamed(boolean tamed)
	{
		super.setTamed(tamed);

		if (tamed)
		{
			this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0D);
			this.getNavigator().setAvoidsWater(this.normallyAvoidsWater() || this.alwaysAvoidsWater());
		}
		else
		{
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
		boolean interacted = false;

		if (this.isTamed() && isOwnedBy(player)) {
			if (!this.isWolfBreedingItem(itemstack) && !isANameTag(itemstack)) { // Always use super if it's breeding item because something else handles that.
				interacted = interactFood(player, itemstack) ||
						interactDye(player, itemstack) ||
						interactClicker(player, itemstack) ||
						interactRemover(player, itemstack) ||
						interactWolfChest(player) ||
						interactToggleSit(player);
			}
		} else {
			interacted = interactTamingItem(player, itemstack);
		}

		return interacted || super.interact(player);
	}

	protected boolean isANameTag(ItemStack itemstack) {
		return itemstack != null && itemstack.getItem() != null && ItemNameTag.class.isAssignableFrom(itemstack.getItem().getClass());
	}

	protected boolean interactTamingItem(EntityPlayer player, ItemStack itemstack) {
		if (this.likes(itemstack) && !this.isAngry()) {
			consumeHeldItem(player, itemstack);
			attemptBecomeTamedBy(player);
			return true;
		} else {
			return false;
		}
	}

	protected boolean interactToggleSit(EntityPlayer player) {
		if (!this.worldObj.isRemote && !player.isSneaking()) {
			toggleSitting();
			return true;
		}
		return false;
	}

	protected boolean interactWolfChest(EntityPlayer player) {
		if (player.isSneaking() && this.getWolfChest() != null) {
			this.getWolfChest().openGUI(this.worldObj, player, this.getCommandSenderName());
			return true;
		} else {
			return false;
		}
	}

	protected boolean interactClicker(EntityPlayer player, ItemStack itemstack) {
		if (isClicker(itemstack) && !this.worldObj.isRemote) {
			toggleShouldFollowOwner();
			announceFollowChange(player);
			return true;
		} else {
			return false;
		}
	}

	protected boolean interactRemover(EntityPlayer player, ItemStack itemstack) {
		if (isRemover(itemstack) && !this.worldObj.isRemote) {
			this.setDead();
			return true;
		} else {
			return false;
		}
	}

	private void announceFollowChange(EntityPlayer player) {
		final String nameTag = this.getCustomNameTag();
		final String name = !StringUtils.isNullOrEmpty(nameTag) ? nameTag : "wolf";
		player.addChatMessage(new ChatComponentText(String.format("%s will %s follow %s", name, shouldFollowOwner() ? "now" : "no longer", player.getDisplayName())));
	}

	protected boolean interactDye(EntityPlayer player, ItemStack itemstack) {
		if (isDye(itemstack)) {
			int color = BlockColored.func_150032_b(itemstack.getItemDamage());

			if (color != this.getCollarColor()) {
				this.setCollarColor(color);

				if (!player.capabilities.isCreativeMode && --itemstack.stackSize <= 0) {
					player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
				}

				return true;
			}
		}
		return false;
	}

	protected boolean interactFood(EntityPlayer player, ItemStack itemstack) {
		if (canEat(itemstack) && getOtherWatchableHealthThing() < 20.0F) {
			consumeHeldItem(player, itemstack);
			this.heal(getHealAmount(itemstack));
			return true;
		}
		return false;
	}

	protected boolean isOwnedBy(EntityPlayer player) {
		return this.func_152114_e(player);
	}

	protected static boolean isDye(ItemStack itemstack) {
		return itemstack != null && itemstack.getItem() == Items.dye;
	}

	protected void setSittingStateAndReset(boolean state) {
		this.aiSit.setSitting(state);
		this.isJumping = false;
		this.setPathToEntity(null);
		this.setTarget(null);
		this.setAttackTarget(null);
	}

	protected void toggleSitting()
	{
		this.setSittingStateAndReset(!this.isSitting());
	}

	public void respondToWhistle(EntityPlayer player) {
		if (this.isOwnedBy(player)) {
			LOGGER.trace("Wolf [{}] is responding to whistle from player [{}]", this, player);
			this.setSittingStateAndReset(false);
			if (!this.shouldFollowOwner()) {
				this.toggleShouldFollowOwner();
				this.announceFollowChange(player);
			}
			this.teleportToEntity(player, false);
		} else {
			LOGGER.trace("Wolf [{}] is heard whistle from player [{}] but is owned by [{}]", this, player, this.getOwner());
		}
	}

	protected boolean teleportToEntity(Entity p_70816_1_) {
		return this.teleportToEntity(p_70816_1_, true);
	}

	protected boolean teleportTo(double xTarget, double yTarget, double zTarget) {
		return this.teleportTo(xTarget, yTarget, zTarget, true);
	}

		/**
		 * Teleport the wolf to another entity
		 */
	protected boolean teleportToEntity(Entity p_70816_1_, final boolean playFx) {
		final double myYHeight = this.boundingBox.minY + (double) (this.height / 2.0F);
		final double targetYHeight = p_70816_1_.posY + (double) p_70816_1_.getEyeHeight();
		Vec3 targetVector = Vec3.createVectorHelper(p_70816_1_.posX - this.posX, targetYHeight - myYHeight, p_70816_1_.posZ - this.posZ);
		targetVector = targetVector.normalize();

		double d0 = this.getDistanceToEntity(p_70816_1_) - 1;

		double targetX = this.posX + targetVector.xCoord * d0;
		double targetY = this.posY + targetVector.yCoord * d0;
		double targetZ = this.posZ + targetVector.zCoord * d0;
		return this.teleportTo(targetX, targetY, targetZ, playFx);
	}

	/**
	 * Teleport the wolf
	 */
	protected boolean teleportTo(double xTarget, double yTarget, double zTarget, final boolean playFx) {
		EnderTeleportEvent event = new EnderTeleportEvent(this, xTarget, yTarget, zTarget, 0);
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

			if (playFx) {
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
			}

			return true;
		}
	}

	protected void toggleShouldFollowOwner()
	{
		this.shouldFollowOwner = !this.shouldFollowOwner;
		this.setPathToEntity(null);
		this.setTarget(null);
		this.setAttackTarget(null);
	}

	protected void attemptBecomeTamedBy(EntityPlayer player)
	{
		if (!this.worldObj.isRemote)
		{
			if (this.rand.nextInt(3) == 0)
			{
				this.setTamed(true);
				this.setPathToEntity(null);
				this.setAttackTarget(null);
				this.aiSit.setSitting(true);
				this.setHealth(20.0F);
				this.func_152115_b(player.getUniqueID().toString());
				this.playTameEffect(true);
				this.worldObj.setEntityState(this, (byte) 7);
			}
			else
			{
				this.playTameEffect(false);
				this.worldObj.setEntityState(this, (byte) 6);
			}
		}
	}

	@Override
	public void setHealth(float newHealth) {
		if (this.isTamed() && RuntimeConfiguration.tamedWolvesNeverDie) {
			newHealth = newHealth < 1.0f ? 1.0f : newHealth;
		}
		super.setHealth(newHealth);
	}

	protected void consumeHeldItem(EntityPlayer player, ItemStack itemstack)
	{
		if (!player.capabilities.isCreativeMode)
		{
			--itemstack.stackSize;
		}

		if (itemstack.stackSize <= 0)
		{
			player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
		}
	}

	/**
	 * Tests whether the wolf can eat a particular item.
	 *
	 * @param itemStack
	 * @return
	 */
	@Override
	public boolean canEat(ItemStack itemStack)
	{
		if (itemStack != null)
		{
			for (ItemStack food : getEdibleItems())
			{
				if (food.getItem() == itemStack.getItem() && food.getItemDamage() == itemStack.getItemDamage())
					return true;
			}
		}
		return false;
	}

	@Override
	public float getHealAmount(ItemStack itemstack)
	{
		if (canEat(itemstack))
		{
			if (itemstack.getItem().equals(PamsHarvestcraftHolder.blackberryjellysandwichItem))
				return 16F;
			else
				return 8F;
		}
		else
			return 0F;
	}

	protected List<ItemStack> getLikedItems()
	{
		return new ArrayList<>(this.likedItems);
	}

	protected void addLikedItem(ItemStack likedItem)
	{
		this.likedItems.add(likedItem);
	}

	protected List<ItemStack> getEdibleItems()
	{
		return new ArrayList<>(this.edibleItems);
	}

	protected void addEdibleItem(ItemStack edibleItem)
	{
		this.edibleItems.add(edibleItem);
	}

	@Override
	public void handleHealthUpdate(byte updateFlags)
	{
		if (updateFlags == 8)
		{
			this.wolfIsReadyToShake = true;
			this.timeWolfIsShaking = 0.0F;
			this.prevTimeWolfIsShaking = 0.0F;
		}
		else
		{
			super.handleHealthUpdate(updateFlags);
		}
	}

	public float getTailRotation()
	{
		return this.isAngry() ? 1.5393804F
				: (this.isTamed() ? (0.55F - (20.0F - getOtherWatchableHealthThing()) * 0.02F) * (float) Math.PI
						: ((float) Math.PI / 5F));
	}

	@Override
	public boolean isBreedingItem(ItemStack itemStack)
	{
		return isWolfBreedingItem(itemStack);
	}

	/**
	 * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on
	 * the animal type)
	 */
	@Override
	public boolean isWolfBreedingItem(ItemStack itemStack)
	{
		// Return false if wolf isn't tamed as that's the only way to prevent untamed wolves from falling in love.
		return itemStack != null && isTamed() && ItemWolfFood.foodsMatch(ItemRegistry.getWolfFood("meaty_bone"), itemStack);
	}


	/**
	 * Will return how many at most can spawn in a chunk at once.
	 */
	@Override
	public int getMaxSpawnedInChunk()
	{
		return 4;
	}

	/**
	 * Determines whether this wolf is angry or not.
	 */
	public boolean isAngry()
	{
		return (this.dataWatcher.getWatchableObjectByte(16) & 2) != 0;
	}

	/**
	 * Sets whether this wolf is angry or not.
	 */
	public void setAngry(boolean angry)
	{
		byte b0 = this.dataWatcher.getWatchableObjectByte(16);

		if (angry)
		{
			this.dataWatcher.updateObject(16, Byte.valueOf((byte) (b0 | 2)));
		}
		else
		{
			this.dataWatcher.updateObject(16, Byte.valueOf((byte) (b0 & -3)));
		}
	}

	@Override
	public abstract EntityWolfBase createChild(EntityAgeable parent);

	protected EntityWolfBase createChild(EntityAgeable parent, EntityWolfBase child)
	{
		String owner = this.func_152113_b();

		parent.getRNG();

		if (owner != null && owner.trim().length() > 0)
		{
			child.func_152115_b(owner);
			child.setTamed(true);
		}

		return child;
	}

	public void setBegging(boolean isBegging)
	{
		if (isBegging)
		{
			this.dataWatcher.updateObject(19, Byte.valueOf((byte) 1));
		}
		else
		{
			this.dataWatcher.updateObject(19, Byte.valueOf((byte) 0));
		}
	}

	public boolean isBegging()
	{
		return this.dataWatcher.getWatchableObjectByte(19) == 1;
	}

	/**
	 * Another method that's here because I want a non-obfuscated name for my method.
	 *
	 * @param potentialMate
	 * @return
	 */
	public boolean canMateWith(EntityAnimal potentialMate)
	{
		return canWolfMateWith(potentialMate);
	}

	/**
	 * Returns true if the mob is currently able to mate with the specified mob.
	 */
	@Override
	public boolean canWolfMateWith(EntityAnimal potentialMate)
	{
		// Wolves cannot mate with themselves.
		return potentialMate != this &&
		// Can only breed tamed wolves
				this.isTamed() &&
				// Can only mate with eligible species.
				canMateWithSpecies(potentialMate) &&
				// Wolves cannot mate with a partner who is not tamed or who is sitting down.
				isNotSittingOrUntamed(potentialMate) &&
				// Gotta be feeling the love if I'm going to mate.
				this.isInLove() &&
				// My partner's gotta be feeling it too!
				potentialMate.isInLove();
	}

	/**
	 * Test whether an animal is sitting or untamed. An animal is considered untamed if it is tameable but has not been
	 * tamed. For the purposes of this check, an untameable animal is considered tamed.
	 *
	 * @param potentialMate
	 *           Animal to test for sitting and untamed conditions.
	 * @return True if the animal is not tameable or the tameable animal is tamed and not sitting.
	 */
	private boolean isNotSittingOrUntamed(EntityAnimal potentialMate)
	{
		if (potentialMate instanceof EntityTameable)
		{
			EntityTameable tameableMate = (EntityTameable) potentialMate;
			if (!tameableMate.isTamed() || tameableMate.isSitting())
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		else
		{
			return true;
		}
	}

	/**
	 * Can I mate with animals {@code potentialMate}'s species?
	 *
	 * @param potentialMate
	 *           The potential mate to test for suitability
	 * @return True if {@code potentialMate}'s species is the same as mine.
	 */
	protected boolean canMateWithSpecies(EntityAnimal potentialMate)
	{
		return EntityWolfBase.class.isAssignableFrom(potentialMate.getClass());
	}

	protected void burnIfInSunlight()
	{
		if (!this.isWolfTamed() && this.worldObj.isDaytime() && !this.isChild())
		{
			float f = this.getBrightness(1.0F);

			if (f > 0.5F && this.getRNG().nextFloat() * 30.0F < (f - 0.4F) * 2.0F && canSeeTheSky(getWorldObj(), posX, posY, posZ))
			{
				this.setFire(8);
			}
		}
	}

	protected void hurtIfWet()
	{
		if (!this.isWolfTamed() && this.isWet())
		{
			this.attackEntityFrom(DamageSource.drown, 1.0F);
		}
	}

	/**
	 * Determines if an entity can be despawned, used on idle far away entities
	 */
	@Override
	protected boolean canDespawn()
	{
		if (getMetadata().type().creatureType() == EnumCreatureType.monster) {
			return !this.isTamed();
		} else {
			return !this.isTamed() && this.ticksExisted > 2400;
		}
	}

	protected boolean isStandingOn(Block... blockTypes)
	{
		int x = MathHelper.floor_double(this.posX);
		int y = MathHelper.floor_double(this.boundingBox.minY);
		int z = MathHelper.floor_double(this.posZ);

		return Arrays.asList(blockTypes).contains(this.worldObj.getBlock(x, y - 1, z)) || Arrays.asList(blockTypes).contains(this.worldObj.getBlock(x, y, z));
	}

	@Override
	public boolean getCanSpawnHere()
	{
		return getCanSpawnHere(false);
	}

	/**
	 * Standard spawn conditions
	 * @param noSky If true, wolf shouldn't be able to see the sky, otherwise they should.
	 */
	protected boolean getCanSpawnHere(boolean noSky) {
		return isSuitableDimension() &&
				creatureCanSpawnHere() &&
				isStandingOnSuitableFloor() &&
				(canSeeTheSky(getWorldObj(), posX, posY, posZ) ^ noSky);
	}

	protected boolean isSuitableDimension()
	{
		int dimensionId = getWorldObj().provider.dimensionId;
		if (RuntimeConfiguration.dimensionSpawnBlackList != null
				&& RuntimeConfiguration.dimensionSpawnBlackList.contains(dimensionId))
		{
			SPAWNLOGGER.trace("Spawn in dimension [{}] prevented due to dimension blacklist.", dimensionId);
			return false;
		}
		else
		{
			return true;
		}
	}

	/**
	 * Called when the mob's health reaches 0.
	 */
	@Override
	public void onDeath(DamageSource p_70645_1_) {
		super.onDeath(p_70645_1_);

		if (!this.worldObj.isRemote) {
			this.dropItemsInChest();
			if (this.isTamed()  && RuntimeConfiguration.tamedWolvesNeverDie) {
				LOGGER.warn("Warning: A wolf died.", new Exception("This is not really an exception. Just printing stacktrace"));
				LOGGER.warn("Unwated death happened to [{}]", this);
			}
		}
	}

	private void dropItemsInChest() {
		final InventoryWolfChest wolfChest = getWolfChest();
		if (wolfChest != null) {
			for (ItemStack itemStack : wolfChest.getContents()) {
				if (itemStack != null) {
					this.entityDropItem(itemStack, 0.0F);
				}
			}
		}
	}

	protected boolean isStandingOnSuitableFloor()
	{
		return isStandingOn(getFloorBlocks().toArray(new Block[0]));
	}

	@SuppressWarnings("unused")
	protected boolean animalCanSpawnHere()
	{
		return isStandingOn(grass) && getFullBlockLightValue(getWorldObj(), posX, posY - 1, posZ) > 8 && creatureCanSpawnHere();
	}

	protected boolean creatureCanSpawnHere()
	{
		int i = MathHelper.floor_double(this.posX);
		int j = MathHelper.floor_double(this.boundingBox.minY);
		int k = MathHelper.floor_double(this.posZ);
		return livingCanSpawnHere() && this.getBlockPathWeight(i, j, k) >= 0.0F;
	}

	protected boolean livingCanSpawnHere()
	{
		return this.worldObj.checkNoEntityCollision(this.boundingBox)
				&& this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).isEmpty()
				&& !this.worldObj.isAnyLiquid(this.boundingBox);
	}

	@Override
	public float getBlockPathWeight(int p_70783_1_, int p_70783_2_, int p_70783_3_)
	{
		return this.worldObj.getBlock(p_70783_1_, p_70783_2_ - 1, p_70783_3_) != air ? 10.0F
				: this.worldObj.getLightBrightness(p_70783_1_, p_70783_2_, p_70783_3_) - 0.5F;
	}

	@Override
	public boolean isCreatureType(EnumCreatureType type, boolean forSpawnCount)
	{
		final EnumCreatureType myType = getMetadata().type().creatureType();

		if (myType == null) {
			LOGGER.warn("Stacy's wolf with no type! [{}]", this);
			return !isTamed() && super.isCreatureType(type, forSpawnCount);
		} else {
			if (forSpawnCount)
				return !isTamed() && type == myType;
			else
				return type == myType;
		}
	}

	@Override
	public boolean canSpawnNow(World world, float x, float y, float z)
	{
		return world.isDaytime();
	}

	@Override
	public boolean testSpawnProbability() {
		return isBypassThrottleAndProbability() || RANDOM.nextInt(WolfMetadata.MAX_SPAWN_PROBABILITY) <= this.getMetadata().probability();
	}

	@Override
	public long getSpawnThrottlePeriod()
	{
		return isBypassThrottleAndProbability() ? 0 : this.getMetadata().type().getThrottlePeriod();
	}

	/**
	 * Used by the owner hurt by target AI in determining whether the wolf should attack their owner's attacker.
	 *
	 * @param ownerAttacker
	 *           The entity which attacked the owner.
	 * @param attackedOwner
	 *           The owner who was attacked
	 * @return True if the tamable thing should attack?
	 */
	@Override
	public boolean func_142018_a(EntityLivingBase ownerAttacker, EntityLivingBase attackedOwner)
	{
		if (!(ownerAttacker instanceof EntityCreeper) && !(ownerAttacker instanceof EntityGhast))
		{
			if (ownerAttacker instanceof EntityWolfBase)
			{
				EntityWolfBase entitywolf = (EntityWolfBase) ownerAttacker;

				if (entitywolf.isTamed() && entitywolf.getOwner() == attackedOwner)
				{
					return false;
				}
			}

			return ownerAttacker instanceof EntityPlayer && attackedOwner instanceof EntityPlayer
					&& !((EntityPlayer) attackedOwner).canAttackPlayer((EntityPlayer) ownerAttacker) ? false
							: !(ownerAttacker instanceof EntityHorse) || !((EntityHorse) ownerAttacker).isTame();
		}
		else
		{
			return false;
		}
	}

	@Override
	public boolean likes(ItemStack itemStack)
	{
		if (itemStack != null)
		{

			if (itemStack.getItem() instanceof ItemWolfFood)
			{
				for (ItemStack food : getLikedItems())
				{
					if ((ItemWolfFood.foodsMatch(itemStack, food)))
						return true;
				}
			}
			else
			{
				final Class<? extends Item> itemType = itemStack.getItem().getClass();
				for (ItemStack food : getLikedItems())
				{
					// Same item type and either has no subtypes or the metadata value matches
					if (food.getItem().getClass().isAssignableFrom(itemType)
							&& (itemStack.getHasSubtypes() == false || itemStack.getItemDamage() == food.getItemDamage()))
						return true;
				}
			}
		}
		return false;
	}



	@Override
	public boolean isTemptedBy(ItemStack itemStack)
	{
		return likes(itemStack);
	}

	@Override
	public boolean normallyAvoidsWater()
	{
		return true;
	}

	@Override
	public boolean alwaysAvoidsWater()
	{
		return false;
	}

	@Override
	public EntityLiving asEntityLiving()
	{
		return this;
	}

	@Override
	public World getWorldObj()
	{
		return this.worldObj;
	}

	public void setIsInWater(boolean value)
	{
		this.inWater = value;
	}

	/**
	 * NOTE: This method basically exists because I wanted a method on the wolf interface to be used by other stuff.
	 * <p>
	 * Don't replace it with isShaking because the obfuscator renames it and causes AbstractMethodExceptions.
	 */
	@Override
	public boolean isWolfShaking()
	{
		return isShaking();
	}

	/**
	 * NOTE: This method basically exists because I wanted a method on the wolf interface to be used by other stuff.
	 * <p>
	 * Don't replace it with isShaking because the obfuscator renames it and causes AbstractMethodExceptions.
	 */
	@Override
	public boolean isWolfAngry()
	{
		return isAngry();
	}

	/**
	 * NOTE: This method basically exists because I wanted a method on the wolf interface to be used by other stuff.
	 * <p>
	 * Don't replace it with isShaking because the obfuscator renames it and causes AbstractMethodExceptions.
	 */
	@Override
	public boolean isWolfTamed()
	{
		return isTamed();
	}

	public boolean shouldSwimToSurface()
	{
		return true;
	}

	protected EntityAIWolfTempt getAiTempt()
	{
		return aiTempt;
	}

	protected void setAiTempt(EntityAIWolfTempt aiTempt)
	{
		this.aiTempt = aiTempt;
	}

	public WolfType getWolfType()
	{
		return getMetadata().type();
	}

	public WolfLightSource getLightSource() {
		return lightSource;
	}

	public void setLightSource(WolfLightSource lightSource) {
		this.lightSource = lightSource;
	}

	public WolfPeriodicItemDrop getPeriodicDrop() {
		return periodicDrop;
	}

	public void setPeriodicDrop(WolfPeriodicItemDrop periodicDrop) {
		this.periodicDrop = periodicDrop;
	}
	
	public void setBypassThrottleAndProbability(boolean bypassThrottleAndProbability) {
		this.bypassThrottleAndProbability = bypassThrottleAndProbability;
	}

	public boolean isBypassThrottleAndProbability() {
		return bypassThrottleAndProbability;
	}

	public InventoryWolfChest getWolfChest() {
		return wolfChest;
	}

	public void setWolfChest(InventoryWolfChest wolfChest) {
		this.wolfChest = wolfChest;
	}
}
