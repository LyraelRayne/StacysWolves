package au.lyrael.stacywolves.blocks;

import au.lyrael.stacywolves.StacyWolves;
import au.lyrael.stacywolves.utility.LanguageHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;

import static au.lyrael.stacywolves.StacyWolves.MOD_ID;

public class BlockStacyWolves extends Block {

    public BlockStacyWolves(Material material) {
        super(material);
        this.setCreativeTab(StacyWolves.CREATIVE_TAB);
    }

    public String getUnlocalizedName(ItemStack itemStack) {
        return String.format("tile.%s:%s", MOD_ID, unwrapUnlocalizedName(super.getUnlocalizedName()));
    }

    protected String unwrapUnlocalizedName(String unlocalizedName) {
        return LanguageHelper.unwrapUnlocalizedName(unlocalizedName);
    }

}
