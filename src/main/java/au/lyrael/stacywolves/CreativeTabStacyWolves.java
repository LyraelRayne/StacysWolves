package au.lyrael.stacywolves;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class CreativeTabStacyWolves extends CreativeTabs {
    public CreativeTabStacyWolves(int ID, String langName) {
        super(ID, langName);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getTabIconItem() {
        return (Item) Item.itemRegistry.getObject("dragon_egg");
    }

}