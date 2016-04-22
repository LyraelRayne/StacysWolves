package au.lyrael.stacywolves.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static au.lyrael.stacywolves.StacyWolves.CREATIVE_TAB;
import static au.lyrael.stacywolves.StacyWolves.MOD_ID;

public class ItemWolfFood extends ItemStacyWolves {

    public static final String UNLOCALIZED_NAME = "wolf_food";

    public static final String ID_TAG = "wolf_food_id";

    private final Map<String, ItemStack> foods = new HashMap<>();

    @SideOnly(Side.CLIENT)
    private final Map<String, IIcon> icons = new HashMap<>();

    public ItemWolfFood() {
        super();
        this.setCreativeTab(CREATIVE_TAB);
        this.setUnlocalizedName(UNLOCALIZED_NAME);
        this.setMaxStackSize(64);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.buildItemsList();
        canRepair = false;
    }

    private void buildItemsList() {
        createSubItem(this, "air_bone");
        createSubItem(this, "birch_bone");
        createSubItem(this, "cake_bone");
        createSubItem(this, "desert_bone");
        createSubItem(this, "diamond_bone");
        createSubItem(this, "earth_bone");
        createSubItem(this, "emerald_bone");
        createSubItem(this, "end_bone");
        createSubItem(this, "ender_bone");
        createSubItem(this, "fire_bone");
        createSubItem(this, "flower_bone");
        createSubItem(this, "gold_bone");
        createSubItem(this, "ice_bone");
        createSubItem(this, "iron_bone");
        createSubItem(this, "mesa_bone");
        createSubItem(this, "mushroom_bone");
        createSubItem(this, "nether_bone");
        createSubItem(this, "prismarine_bone");
        createSubItem(this, "redstone_bone");
        createSubItem(this, "savannah_bone");
        createSubItem(this, "skeleton_bone");
        createSubItem(this, "water_bone");
        createSubItem(this, "zombie_bone");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        for (Map.Entry<String, ItemStack> entry : foods.entrySet()) {
            IIcon itemIcon = iconRegister.registerIcon(String.format("%s/%s", unwrapUnlocalizedName(getUnlocalizedName()), entry.getKey()));
            icons.put(entry.getKey(), itemIcon);
        }
    }

    @Override
    public IIcon getIconIndex(ItemStack stack) {
        return getIconForStack(stack);
    }


    @Override
    public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
        return getIconForStack(stack);
    }

    protected IIcon getIconForStack(ItemStack stack) {
        if (stack.hasTagCompound()) {
            String id = stack.getTagCompound().getString(ID_TAG);

            // Load the first item if the id no longer exists.
            if (!foods.containsKey(id))
                id = foods.keySet().iterator().next();

            return icons.get(id);
        } else {
            // Fallback to default, because what else are we going to do?
            return icons.values().iterator().next();
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(final Item item, final CreativeTabs tab, final List list) {
        for (Map.Entry<String, ItemStack> entry : foods.entrySet()) {
            list.add(entry.getValue());
        }
    }

    protected ItemStack createSubItem(Item item, String id) {
        final ItemStack itemStack = new ItemStack(item, 1);
        itemStack.setTagCompound(new NBTTagCompound());
        itemStack.getTagCompound().setString(ID_TAG, id);
        foods.put(id, itemStack);
        return itemStack;
    }

    public ItemStack getFood(final String id) {
        final ItemStack sourceStack = foods.get(id);
        final ItemStack resultStack = new ItemStack(sourceStack.getItem(), sourceStack.stackSize, sourceStack.getItemDamage());

        if (sourceStack.stackTagCompound != null) {
            resultStack.stackTagCompound = (NBTTagCompound) sourceStack.stackTagCompound.copy();
        }

        return resultStack;
    }

    @Override
    public String getUnlocalizedName(final ItemStack stack) {
        if (stack.hasTagCompound()) {
            final String id = stack.getTagCompound().getString(ID_TAG);
            return this.getUnlocalizedName() + "_" + String.valueOf(id);
        } else {
            return super.getUnlocalizedName(stack);
        }
    }

}
