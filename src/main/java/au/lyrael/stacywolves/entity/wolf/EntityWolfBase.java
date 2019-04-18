package au.lyrael.stacywolves.entity.wolf;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.client.render.IRenderableWolf;
import au.lyrael.stacywolves.config.RuntimeConfiguration;
import au.lyrael.stacywolves.entity.ISpawnable;
import au.lyrael.stacywolves.entity.ai.EntityAIAvoidEntityIfEntityIsTamed;
import au.lyrael.stacywolves.entity.ai.EntityAIWolfTempt;
import au.lyrael.stacywolves.entity.ai.WolfAIBeg;
import au.lyrael.stacywolves.integration.PamsHarvestcraftHolder;
import au.lyrael.stacywolves.inventory.InventoryWolfChest;
import au.lyrael.stacywolves.item.ItemWolfFood;
import au.lyrael.stacywolves.item.WolfPeriodicItemDrop;
import au.lyrael.stacywolves.lighting.WolfLightSource;
import au.lyrael.stacywolves.registry.ItemRegistry;
import au.lyrael.stacywolves.registry.WolfType;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemNameTag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.village.Village;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import org.apache.commons.lang3.RandomUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.*;

import static au.lyrael.stacywolves.StacyWolves.MOD_ID;
import static au.lyrael.stacywolves.config.RuntimeConfiguration.allowedInPeaceful;
import static au.lyrael.stacywolves.config.RuntimeConfiguration.onlyOcelotWolvesScareCreepers;
import static au.lyrael.stacywolves.registry.ItemRegistry.isClicker;
import static au.lyrael.stacywolves.utility.WorldHelper.canSeeTheSky;
import static au.lyrael.stacywolves.utility.WorldHelper.getFullBlockLightValue;
import static net.minecraft.init.Blocks.*;


public abstract class EntityWolfBase extends EntityTameable implements IWolf, IRenderableWolf, ISpawnable
{

	private static final DataParameter<Float> DATA_HEALTH_ID = EntityDataManager.<Float>createKey(EntityWolf.class, DataSerializers.FLOAT);
	private static final DataParameter<Boolean> BEGGING = EntityDataManager.<Boolean>createKey(EntityWolf.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> COLLAR_COLOR = EntityDataManager.<Integer>createKey(EntityWolf.class, DataSerializers.VARINT);

	/**
	 * Float used to smooth the rotation of the wolf head
	 */
	private float headRotationCourse;

	private final WolfMetadata metadata;

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

	private static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	private static final Logger SPAWNLOGGER = LogManager.getLogger(MOD_ID + ".spawn");

	protected static final List<Block> NORMAL_FLOOR_BLOCKS = Arrays.asList(GRASS, DIRT, GRAVEL, SAND, SANDSTONE);
	protected static final List<Block> SUBTERRANEAN_FLOOR_BLOCKS = Arrays.asList(STONE, GRAVEL, COBBLESTONE, OBSIDIAN);
	protected static final List<Block> INDOOR_FLOOR_BLOCKS = Arrays.asList(STONE, COBBLESTONE, PLANKS, WOODEN_SLAB, STONE_SLAB, DOUBLE_STONE_SLAB, DOUBLE_WOODEN_SLAB);
	protected static final List<Block> ICE_FLOOR_BLOCKS = Arrays.asList(ICE, SNOW, PACKED_ICE, SNOW_LAYER);
	protected static final List<Block> SHROOM_FLOOR_BLOCKS = Arrays.asList((Block) MYCELIUM, RED_MUSHROOM_BLOCK,
			BROWN_MUSHROOM_BLOCK);
	protected static final List<Block> MESA_FLOOR_BLOCKS = Arrays.asList(SAND, HARDENED_CLAY, STAINED_HARDENED_CLAY);

	private WolfPeriodicItemDrop periodicDrop;
	private WolfLightSource lightSource;
	private InventoryWolfChest wolfChest;

	public EntityWolfBase(World world)
	{
		super(world);
		this.setSize(0.6F, 0.8F);
		this.getNavigator().getNodeProcessor().setCanSwim(!(this.normallyAvoidsWater() || this.alwaysAvoidsWater()));

		this.aiSit = new EntityAISit(this);
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, this.aiSit);
		this.setAiTempt(new EntityAIWolfTempt(this, 0.5D, false));
		this.tasks.addTask(3, this.getAiTempt());
//		this.tasks.addTask(3, new EntityWolf.AIAvoidEntity(this, EntityLlama.class, 24.0F, 1.5D, 1.5D));
		this.tasks.addTask(4, new EntityAILeapAtTarget(this, 0.4F));
		this.tasks.addTask(5, new EntityAIAttackMelee(this, 1.0D, true));
		this.tasks.addTask(6, new EntityAIFollowOwner(this, 1.0D, 10.0F, 2.0F));
		this.tasks.addTask(7, new EntityAIMate(this, 1.0D));
		this.tasks.addTask(8, new EntityAIWanderAvoidWater(this, 1.0D));
		this.tasks.addTask(9, new WolfAIBeg(this, 8.0F));
		this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(10, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
		this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
		this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true, new Class[0]));

		if (RuntimeConfiguration.wolvesAttackAnimals) {
			this.targetTasks.addTask(4, new EntityAITargetNonTamed(this, EntityAnimal.class, false, new Predicate<Entity>() {
				public boolean apply(@Nullable Entity p_apply_1_) {
					return p_apply_1_ instanceof EntitySheep || p_apply_1_ instanceof EntityRabbit;
				}
			}));
		}

		this.targetTasks.addTask(5, new EntityAINearestAttackableTarget(this, AbstractSkeleton.class, false));

		this.setTamed(false);

		this.metadata = this.getClass().getAnnotation(WolfMetadata.class);

		this.setupEdibleItems();
	}

	private void setupEdibleItems() {
		this.addEdibleItem(new ItemStack(Items.BEEF));
		this.addEdibleItem(new ItemStack(Items.CHICKEN));
		final ItemStack blackberryJellySandwichItemStack = PamsHarvestcraftHolder.getBlackberryJellySandwichItemStack();
		if (blackberryJellySandwichItemStack != null)
		{
			this.addEdibleItem(blackberryJellySandwichItemStack);
		}
	}

	protected Village scanForVillage(int villageScanRadius) {
		return getWorldObj().getVillageCollection().getNearestVillage(this.getPosition(), villageScanRadius);
	}

	protected List<Block> getFloorBlocks()
	{
		return NORMAL_FLOOR_BLOCKS;
	}


	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);

		if (this.isTamed()) {
			this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
		} else {
			this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
		}
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
	protected void updateAITasks() {
		this.getDataManager().set(DATA_HEALTH_ID, Float.valueOf(this.getHealth()));
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataManager.register(DATA_HEALTH_ID, Float.valueOf(this.getHealth()));
		this.dataManager.register(BEGGING, Boolean.valueOf(false));
		this.dataManager.register(COLLAR_COLOR, Integer.valueOf(EnumDyeColor.RED.getDyeDamage()));
	}

	/**
	 * Fired whenever the entity moves
	 */
	protected void playStepSound(BlockPos pos, Block blockIn) {
		this.playSound(SoundEvents.ENTITY_WOLF_STEP, 0.15F, 1.0F);
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		nbt.setBoolean("Angry", this.isAngry());
		nbt.setByte("CollarColor", (byte) this.getCollarColor().getDyeDamage());
		nbt.setBoolean("FollowingOwner", this.shouldFollowOwner);
		final InventoryWolfChest wolfChest = this.getWolfChest();
		if (wolfChest != null)
			wolfChest.writeEntityToNBT(nbt);
	}

	@Override
	public float getWolfBrightness(float maybeTime) {
		return isOffsetPositionInLiquid(posX, posY, posZ) ? 1F : getBrightness();
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);
		this.setAngry(nbt.getBoolean("Angry"));
		if (nbt.hasKey("CollarColor", 99)) {
			this.setCollarColor(EnumDyeColor.byDyeDamage(nbt.getByte("CollarColor")));
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
	protected SoundEvent getAmbientSound() {
		if (this.isAngry()) {
			return SoundEvents.ENTITY_WOLF_GROWL;
		} else if (this.rand.nextInt(3) == 0) {
			return this.isTamed() && ((Float) this.dataManager.get(DATA_HEALTH_ID)).floatValue() < 10.0F ? SoundEvents.ENTITY_WOLF_WHINE : SoundEvents.ENTITY_WOLF_PANT;
		} else {
			return SoundEvents.ENTITY_WOLF_AMBIENT;
		}
	}

	/**
	 * There appear to be two watchables for health. 18 is used by a lot of tamed wolf stuff, maybe this is tamed health?
	 *
	 * @return Datawatcher item #18
	 */
	private float getOtherWatchableHealthThing() {
		return this.getDataManager().get(DATA_HEALTH_ID);
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_WOLF_HURT;
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_WOLF_DEATH;
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
	public EnumDyeColor getCollarColor() {
		return EnumDyeColor.byDyeDamage(((Integer) this.dataManager.get(COLLAR_COLOR)).intValue() & 15);
	}

	/**
	 * Set this wolf's collar color.
	 */
	public void setCollarColor(EnumDyeColor collarcolor) {
		this.dataManager.set(COLLAR_COLOR, Integer.valueOf(collarcolor.getDyeDamage()));
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

		if (!this.getWorldObj().isRemote && this.isShaking && !this.wolfIsReadyToShake && !this.hasPath() && this.onGround)
		{
			this.wolfIsReadyToShake = true;
			this.timeWolfIsShaking = 0.0F;
			this.prevTimeWolfIsShaking = 0.0F;
			this.getWorldObj().setEntityState(this, (byte) 8);
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

		List<EntityCreature> creepersInRange = getWorldObj().getEntitiesWithinAABB(EntityCreeper.class,
				getEntityBoundingBox().expand(scareRange, 3.0D, scareRange), Predicates.and(
						EntitySelectors.CAN_AI_TARGET,
						(Entity subject) -> EntityCreeper.class.isAssignableFrom(subject.getClass()) && subject.isEntityAlive()
								&& getEntitySenses().canSee(subject))
		);

		for (EntityCreature entityCreature : creepersInRange)
		{
			if (!isAlreadyScared(entityCreature))
			{
				entityCreature.tasks.addTask(0,
						new EntityAIAvoidEntityIfEntityIsTamed(entityCreature, this.getClass(), scareRange, 1.0D, 1.2D));
			}
		}
	}

	protected boolean isAlreadyScared(EntityCreature entityCreature) {
		final Set<EntityAITasks.EntityAITaskEntry> taskEntries = entityCreature.tasks.taskEntries;
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

	protected void doBeggingChase() {
		if (this.isBegging()) {
			this.headRotationCourse += (1.0F - this.headRotationCourse) * 0.4F;
		} else {
			this.headRotationCourse += (0.0F - this.headRotationCourse) * 0.4F;
		}
	}

	protected boolean doPeacefulDespawn() {
		if (!allowedInPeaceful && !this.getWorldObj().isRemote && !isWolfTamed() && this.getWorldObj().getDifficulty() == EnumDifficulty.PEACEFUL)
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
		} else if ((this.isShaking || this.wolfIsReadyToShake) && this.wolfIsReadyToShake) {
			if (this.timeWolfIsShaking == 0.0F) {
				this.playSound(SoundEvents.ENTITY_WOLF_SHAKE, this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
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
		float f = (float) this.getEntityBoundingBox().minY;
		int i = (int) (MathHelper.sin((this.timeWolfIsShaking - 0.4F) * (float) Math.PI) * 7.0F);

		for (int j = 0; j < i; ++j)
		{
			float f1 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
			float f2 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
			this.world.spawnParticle(EnumParticleTypes.WATER_SPLASH, this.posX + (double) f1, (double) (f + 0.8F), this.posZ + (double) f2, this.motionX, this.motionY, this.motionZ);
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
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (this.isEntityInvulnerable(source))
		{
			return false;
		} else {
			Entity entity = source.getTrueSource();

			if (this.aiSit != null) {
				this.aiSit.setSitting(false);
			}

			if (entity != null && !(entity instanceof EntityPlayer) && !(entity instanceof EntityArrow))
			{
				amount = (amount + 1.0F) / 2.0F;
			}

			return super.attackEntityFrom(source, amount);
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

		if (tamed) {
			this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
			this.getNavigator().getNodeProcessor().setCanSwim(!(this.normallyAvoidsWater() || this.alwaysAvoidsWater()));
		} else {
			this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0D);
		}
	}

	/**
	 * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
	 *
	 * @return
	 */
	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand) {
		ItemStack itemstack = player.inventory.getCurrentItem();
		boolean interacted = false;

		if (this.isTamed() && isOwnedBy(player)) {
			if (!this.isWolfBreedingItem(itemstack) && !isANameTag(itemstack)) { // Always use super if it's breeding item because something else handles that.
				interacted = interactFood(player, itemstack) ||
						interactDye(player, itemstack) ||
						interactClicker(player, itemstack) ||
						interactWolfChest(player) ||
						interactToggleSit(player);
			}
		} else {
			interacted = interactTamingItem(player, itemstack);
		}

		return interacted || super.processInteract(player, hand);
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
		if (!this.getWorldObj().isRemote && !player.isSneaking()) {
			toggleSitting();
			return true;
		}
		return false;
	}

	protected boolean interactWolfChest(EntityPlayer player) {
		if (player.isSneaking() && this.getWolfChest() != null) {
			this.getWolfChest().openGUI(this.getWorldObj(), player, this.getCommandSenderEntity().getName());
			return true;
		} else {
			return false;
		}
	}

	protected boolean interactClicker(EntityPlayer player, ItemStack itemstack) {
		if (isClicker(itemstack) && !this.getWorldObj().isRemote) {
			itemstack.getItem();
			toggleShouldFollowOwner();
			announceFollowChange(player);
			return true;
		} else {
			return false;
		}
	}

	private void announceFollowChange(EntityPlayer player) {
		final String nameTag = this.getCustomNameTag();
		final String name = !StringUtils.isNullOrEmpty(nameTag) ? nameTag : "wolf";
		player.sendStatusMessage(new TextComponentString(String.format("%s will %s follow %s", name, shouldFollowOwner() ? "now" : "no longer", player.getDisplayName())), false);
	}

	protected boolean interactDye(EntityPlayer player, ItemStack itemstack) {
		if (isDye(itemstack)) {
			EnumDyeColor enumdyecolor = EnumDyeColor.byDyeDamage(itemstack.getMetadata());

			if (enumdyecolor != this.getCollarColor()) {
				this.setCollarColor(enumdyecolor);
				if (!player.capabilities.isCreativeMode) {
					itemstack.shrink(1);
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
		return this.isOwner(player);
	}

	protected static boolean isDye(ItemStack itemstack) {
		return itemstack != null && itemstack.getItem() == Items.DYE;
	}

	protected void toggleSitting()
	{
		this.aiSit.setSitting(!this.isSitting());
		this.isJumping = false;
		this.navigator.clearPath();
		this.setAttackTarget(null);
	}

	protected void toggleShouldFollowOwner()
	{
		this.shouldFollowOwner = !this.shouldFollowOwner;
		this.navigator.clearPath();
		this.setAttackTarget(null);
	}

	protected void attemptBecomeTamedBy(EntityPlayer player) {
		if (!this.world.isRemote) {
			if (this.rand.nextInt(3) == 0 && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, player)) {
				this.setTamedBy(player);
				this.navigator.clearPath();
				this.setAttackTarget(null);
				this.aiSit.setSitting(true);
				this.setHealth(20.0F);
				this.playTameEffect(true);
				this.world.setEntityState(this, (byte)7);
			}
			else
			{
				this.playTameEffect(false);
				this.world.setEntityState(this, (byte)6);
			}
		}
	}

	protected void consumeHeldItem(EntityPlayer player, ItemStack itemstack)
	{
		if (!player.capabilities.isCreativeMode) {
			itemstack.shrink(1);
		}
//		if (itemstack.isEmpty())
//		{
//			player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
//		}
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
	public void handleStatusUpdate(byte updateFlags)
	{
		if (updateFlags == 8)
		{
			this.wolfIsReadyToShake = true;
			this.timeWolfIsShaking = 0.0F;
			this.prevTimeWolfIsShaking = 0.0F;
		}
		else {
			super.handleStatusUpdate(updateFlags);
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
	public boolean isAngry() {
		return (this.dataManager.get(TAMED).byteValue() & 2) != 0;
	}

	/**
	 * Sets whether this wolf is angry or not.
	 */
	public void setAngry(boolean angry) {
		byte b0 = ((Byte) this.dataManager.get(TAMED)).byteValue();

		if (angry) {
			this.dataManager.set(TAMED, Byte.valueOf((byte) (b0 | 2)));
		}
		else {
			this.dataManager.set(TAMED, Byte.valueOf((byte) (b0 & -3)));
		}
	}

	@Override
	public abstract EntityWolfBase createChild(EntityAgeable parent);

	protected EntityWolfBase createChild(EntityAgeable parent, EntityWolfBase child) {
		UUID uuid = this.getOwnerId();

		parent.getRNG();

		if (uuid != null) {
			child.setOwnerId(uuid);
			child.setTamed(true);
		}

		return child;
	}

	public void setBegging(boolean beg) {
		this.dataManager.set(BEGGING, Boolean.valueOf(beg));
	}

	public boolean isBegging() {
		return this.dataManager.get(BEGGING).booleanValue();
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

	protected void burnIfInSunlight() {
		if (!this.isWolfTamed() && this.getWorldObj().isDaytime() && !this.isChild()) {
			float f = this.getBrightness();

			if (f > 0.5F && this.getRNG().nextFloat() * 30.0F < (f - 0.4F) * 2.0F && canSeeTheSky(getWorldObj(), posX, posY, posZ))
			{
				this.setFire(8);
			}
		}
	}

	protected void hurtIfWet()
	{
		if (!this.isWolfTamed() && this.isWet()) {
			this.attackEntityFrom(DamageSource.DROWN, 1.0F);
		}
	}

	/**
	 * Determines if an entity can be despawned, used on idle far away entities
	 */
	@Override
	protected boolean canDespawn() {
		if (metadata.type().creatureType() == EnumCreatureType.MONSTER) {
			return !this.isTamed();
		} else {
			return !this.isTamed() && this.ticksExisted > 2400;
		}
	}

	protected boolean isStandingOn(Block... blockTypes) {
		int x = MathHelper.floor(this.posX);
		int y = MathHelper.floor(this.getEntityBoundingBox().minY);
		int z = MathHelper.floor(this.posZ);

		return Arrays.asList(blockTypes).contains(this.getWorldObj().getBlockState(new BlockPos(x, y - 1, z))) || Arrays.asList(blockTypes).contains(this.getWorldObj().getBlockState(new BlockPos(x, y, z)));
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

	protected boolean isSuitableDimension() {
		int dimensionId = getWorldObj().provider.getDimension();
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

		if (!this.getWorldObj().isRemote) {
			this.dropItemsInChest();
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
	protected boolean animalCanSpawnHere() {
		return isStandingOn(GRASS) && getFullBlockLightValue(getWorldObj(), posX, posY - 1, posZ) > 8 && creatureCanSpawnHere();
	}

	protected boolean creatureCanSpawnHere() {
		return livingCanSpawnHere() && this.getBlockPathWeight(new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ)) >= 0.0F;
	}

	protected boolean livingCanSpawnHere() {
		IBlockState iblockstate = this.world.getBlockState((new BlockPos(this)).down());
		return iblockstate.canEntitySpawn(this);
	}

	public float getBlockPathWeight(BlockPos pos) {
		return this.world.getBlockState(pos.down()).getBlock() == this.spawnableBlock ? 10.0F : this.world.getLightBrightness(pos) - 0.5F;
	}

	@Override
	public boolean isCreatureType(EnumCreatureType type, boolean forSpawnCount)
	{
		final EnumCreatureType myType = metadata.type().creatureType();

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
		return isBypassThrottleAndProbability() || RandomUtils.nextInt(0, WolfMetadata.MAX_SPAWN_PROBABILITY) <= this.metadata.probability();
	}

	@Override
	public long getSpawnThrottlePeriod()
	{
		return isBypassThrottleAndProbability() ? 0 : this.metadata.type().getThrottlePeriod();
	}

	/**
	 * Used by the owner hurt by target AI in determining whether the wolf should attack their owner's attacker.
	 *
	 * @param target
	 *           The entity which attacked the owner.
	 * @param owner
	 *           The owner who was attacked
	 * @return True if the tamable thing should attack?
	 */
	@Override
	public boolean shouldAttackEntity(EntityLivingBase target, EntityLivingBase owner) {
		if (!(target instanceof EntityCreeper) && !(target instanceof EntityGhast)) {
			if (target instanceof EntityWolf) {
				EntityWolf entitywolf = (EntityWolf) target;

				if (entitywolf.isTamed() && entitywolf.getOwner() == owner)
				{
					return false;
				}
			}

			if (target instanceof EntityPlayer && owner instanceof EntityPlayer && !((EntityPlayer) owner).canAttackPlayer((EntityPlayer) target)) {
				return false;
			} else {
				return !(target instanceof AbstractHorse) || !((AbstractHorse) target).isTame();
			}
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
	public World getWorldObj() {
		return this.world;
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
		return metadata.type();
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
