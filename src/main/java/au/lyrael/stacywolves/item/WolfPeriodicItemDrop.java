package au.lyrael.stacywolves.item;

import au.lyrael.stacywolves.config.RuntimeConfiguration;
import au.lyrael.stacywolves.entity.wolf.EntityWolfBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scala.actors.threadpool.Arrays;

import java.util.List;
import java.util.Random;

import static au.lyrael.stacywolves.StacyWolves.MOD_ID;

public class WolfPeriodicItemDrop {
	private static final Logger LOGGER = LogManager.getLogger(MOD_ID + ".itemdrop");

	private final long period;
	private final int chancePercent;
	private final List<ItemStack> dropList;

	public WolfPeriodicItemDrop(final int periodSeconds, final int chancePercent, final ItemStack... dropItem) {
		assert periodSeconds > 1;
		this.period = periodSeconds * 20;
		this.chancePercent = chancePercent;
		this.dropList = Arrays.asList(dropItem);
	}

	private long counter = 0;
	public boolean shouldItemDrop(EntityWolfBase wolf) {
		// This is the main disabler. Others are redundant for safety only.
		if(RuntimeConfiguration.randomDropsEnabled) {
			final long counter = this.counter++;
			if (counter > getPeriod()) {
				this.counter = 0;
				final boolean result = rollChance(wolf.getRNG());
				LOGGER.trace("Wolf [{}] rolled item drop [{}] with result [{}]", wolf, this, result);
				return result;
			}
		}

		return false;
	}

	private boolean rollChance(final Random rand) {
		final int roll = rand.nextInt(100);
		return roll < getChancePercent();
	}

	public void dropItem(EntityWolfBase wolf) {
		if(RuntimeConfiguration.randomDropsEnabled) {
			final ItemStack stack = selectRandomItem(wolf.getRNG()).copy();
			final World worldObj = wolf.getWorldObj();
			final Vec3 position = wolf.getPosition(1f);
			double posX = position.xCoord, posY = position.yCoord, posZ = position.zCoord;

			if (!worldObj.isRemote && stack != null) {
				LOGGER.debug("[{}] is dropping [{}] at [{}]", wolf, stack, position);
				EntityItem itemDrop = new EntityItem(worldObj, posX, posY, posZ, stack);
				itemDrop.delayBeforeCanPickup = 40;
				worldObj.spawnEntityInWorld(itemDrop);
			}
		} else {
			LOGGER.warn("[{}] attempted to drop [{}] but random drops are disabled", wolf, getDropList());
		}
	}

	private ItemStack selectRandomItem(Random random) {
		final List<ItemStack> dropList = getDropList();
		final int itemIndex = random.nextInt(getDropList().size());
		return dropList.get(itemIndex);
	}

	public long getPeriod() {
		return period;
	}

	public int getChancePercent() {
		return chancePercent;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
	}

	public List<ItemStack> getDropList() {
		return dropList;
	}
}
