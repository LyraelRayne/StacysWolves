package au.lyrael.needsmoredragons;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class CreativeTabNeedsMoreDragons extends CreativeTabs {
    public CreativeTabNeedsMoreDragons(int ID, String langName) {
        super(ID, langName);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getTabIconItem() {
        return (Item) Item.itemRegistry.getObject("dragon_egg");
    }

}
