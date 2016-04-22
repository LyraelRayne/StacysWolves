package au.lyrael.stacywolves.item;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static au.lyrael.stacywolves.StacyWolves.CREATIVE_TAB;

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
        createSubItem(this, "air_bone", new ItemStack(Items.glass_bottle));
        createSubItem(this, "birch_bone", new ItemStack(Blocks.sapling, 1, 2));
        createSubItem(this, "cake_bone", new ItemStack(Items.cake));
        createSubItem(this, "desert_bone", new ItemStack(Blocks.cactus));
        createSubItem(this, "diamond_bone", new ItemStack(Items.diamond));
        createSubItem(this, "earth_bone", new ItemStack(Items.wooden_shovel));
        createSubItem(this, "emerald_bone", new ItemStack(Items.emerald));
        createSubItem(this, "end_bone", new ItemStack(Items.ender_eye));
        createSubItem(this, "ender_bone", new ItemStack(Items.ender_pearl));
        createSubItem(this, "fire_bone", new ItemStack(Items.blaze_powder));
        createSubItem(this, "flower_bone", new ItemStack(Blocks.double_plant, 1, 5));
        createSubItem(this, "gold_bone", new ItemStack(Items.gold_ingot));
        createSubItem(this, "ice_bone", new ItemStack(Items.snowball));
        createSubItem(this, "iron_bone", new ItemStack(Items.iron_ingot));
        createSubItem(this, "mesa_bone", new ItemStack(Blocks.deadbush));
        createSubItem(this, "mushroom_bone", new ItemStack(Blocks.red_mushroom));
        createSubItem(this, "nether_bone", new ItemStack(Items.magma_cream));
        createSubItem(this, "prismarine_bone", null);
        createSubItem(this, "redstone_bone", new ItemStack(Items.redstone));
        createSubItem(this, "savannah_bone", new ItemStack(Blocks.grass));
        createSubItem(this, "skeleton_bone", new ItemStack(Items.dye, 1, 15));
        createSubItem(this, "water_bone", new ItemStack(Items.water_bucket));
        createSubItem(this, "zombie_bone", new ItemStack(Items.rotten_flesh));
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

    protected ItemStack createSubItem(Item item, String id, ItemStack craftedWith) {
        final ItemStack itemStack = new ItemStack(item, 1);
        itemStack.setTagCompound(new NBTTagCompound());
        itemStack.getTagCompound().setString(ID_TAG, id);
        foods.put(id, itemStack);
        if (craftedWith != null)
            GameRegistry.addShapelessRecipe(itemStack, Items.bone, craftedWith);
        return itemStack;
    }

    public ItemStack getFood(final String id) {
        return foods.get(id).copy();
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

    public static boolean foodsMatch(ItemStack a, ItemStack b) {
        if (!(a.getItem() instanceof ItemWolfFood && b.getItem() instanceof ItemWolfFood))
            return false;

        return a.getTagCompound().getString(ID_TAG).equals(b.getTagCompound().getString(ID_TAG));

    }

}
