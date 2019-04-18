package au.lyrael.stacywolves.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerHorseChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class InventoryWolfChest {

	private ContainerHorseChest horseChest;
	private final int size;

	public InventoryWolfChest(int size) {
		this.size = size;
		this.initChest();
	}

	public void openGUI(World worldObj, EntityPlayer player, String name) {
		if (!worldObj.isRemote) {
			this.horseChest.setCustomName(name); // Set custom inventory name.
			player.displayGUIChest(this.horseChest);
		}
	}

	/**
	 * Init horse horseChest
	 */
	private void initChest() {
		ContainerHorseChest ContainerHorseChest = this.horseChest;
		this.horseChest = new ContainerHorseChest("HorseChest", this.getSize());
		this.horseChest.setCustomName("HorseChest"); // Set name

		if (ContainerHorseChest != null) {
			//ContainerHorseChest.func_110132_b(this); // Remove change listener from animal chest
			int i = Math.min(ContainerHorseChest.getSizeInventory(), this.horseChest.getSizeInventory());

			for (int j = 0; j < i; ++j) {
				ItemStack itemstack = ContainerHorseChest.getStackInSlot(j);

				if (itemstack != null) {
					this.horseChest.setInventorySlotContents(j, itemstack.copy());
				}
			}
		}
		// this.horseChest.func_110134_a(this); // Add change listener to animal chest
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound tag) {
		NBTTagList nbttaglist = tag.getTagList("Items", 10);
		this.initChest();

		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			int j = nbttagcompound1.getByte("Slot") & 255;

			if (j >= 0 && j < this.horseChest.getSizeInventory()) {
				this.horseChest.setInventorySlotContents(j, new ItemStack(nbttagcompound1));
			}
		}
	}

	public void writeEntityToNBT(NBTTagCompound tag) {
		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < this.horseChest.getSizeInventory(); ++i) {
			ItemStack itemstack = this.horseChest.getStackInSlot(i);

			if (itemstack != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				itemstack.writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		tag.setTag("Items", nbttaglist);
	}

	public List<ItemStack> getContents() {
		List<ItemStack> result = new ArrayList<>();
		final ContainerHorseChest chest = this.horseChest;
		for (int i = 0; i < chest.getSizeInventory(); ++i)
		{
			ItemStack itemstack = chest.getStackInSlot(i);

			if (itemstack != null)
			{
				result.add(itemstack);
				//this.entityDropItem(itemstack, 0.0F);
			}
		}
		return result;
	}

	/**
	 * getInventorySize()
	 *
	 * @return 27. Because chest.
	 */
	private int getSize() {
		return this.size;
	}
}
