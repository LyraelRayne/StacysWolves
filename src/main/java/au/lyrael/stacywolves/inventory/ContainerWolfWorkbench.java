package au.lyrael.stacywolves.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.world.World;

public class ContainerWolfWorkbench extends ContainerWorkbench {
	public ContainerWolfWorkbench(InventoryPlayer playerInventory, World world, int x, int y, int z) {
		super(playerInventory, world, x, y, z);
	}
	// Thanks @Blue0500! http://www.minecraftforge.net/forum/topic/28414-solved18-open-crafting-table-gui-on-entity-right-click/
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}
}
