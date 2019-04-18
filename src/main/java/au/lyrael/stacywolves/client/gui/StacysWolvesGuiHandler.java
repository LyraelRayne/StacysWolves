package au.lyrael.stacywolves.client.gui;

import au.lyrael.stacywolves.inventory.ContainerWolfWorkbench;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import static au.lyrael.stacywolves.client.gui.StacysWolvesGuiHandler.GuiIds.*;

public class StacysWolvesGuiHandler implements IGuiHandler {

	public enum GuiIds {
		WORKBENCH
	}

	/**
	 * Returns a Server side Container to be displayed to the user.
	 *
	 * @param ID     The Gui ID Number
	 * @param player The player viewing the Gui
	 * @param world  The current world
	 * @param x      X Position
	 * @param y      Y Position
	 * @param z      Z Position
	 * @return A GuiScreen/Container to be displayed to the user, null if none.
	 */
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return ID == WORKBENCH.ordinal() ? new ContainerWolfWorkbench(player.inventory, world, x, y, z) : null;

	}

	/**
	 * Returns a Container to be displayed to the user. On the client side, this
	 * needs to return a instance of GuiScreen On the server side, this needs to
	 * return a instance of Container
	 *
	 * @param ID     The Gui ID Number
	 * @param player The player viewing the Gui
	 * @param world  The current world
	 * @param x      X Position
	 * @param y      Y Position
	 * @param z      Z Position
	 * @return A GuiScreen/Container to be displayed to the user, null if none.
	 */
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return ID == WORKBENCH.ordinal() ? new GuiCrafting(player.inventory, world, x, y, z) : null;
	}
}
