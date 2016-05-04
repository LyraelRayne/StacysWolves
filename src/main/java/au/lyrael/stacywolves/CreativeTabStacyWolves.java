package au.lyrael.stacywolves;

import au.lyrael.stacywolves.registry.ItemRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class CreativeTabStacyWolves extends CreativeTabs {
    public CreativeTabStacyWolves(int ID, String langName) {
        super(ID, langName);
    }

    @Override
    public Item getTabIconItem() {
        return ItemRegistry.getWolfFood("diamond_bone").getItem();
    }

}
