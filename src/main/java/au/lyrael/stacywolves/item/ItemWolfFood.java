package au.lyrael.stacywolves.item;

import au.lyrael.stacywolves.integration.EtFuturumHolder;
import cpw.mods.fml.common.registry.GameRegistry;
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
    private final Map<String, IIcon> icons = new HashMap<>();

    public ItemWolfFood() {
        super();
        this.setCreativeTab(CREATIVE_TAB);
        this.setUnlocalizedName(UNLOCALIZED_NAME);
        this.setMaxStackSize(64);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.buildItemsList();
        this.showTooltipsAlways(true);
        canRepair = false;
    }

    public void buildItemsList() {
        createSubItem("air_bone");
        createSubItem("birch_bone");
        createSubItem("cake_bone");
        createSubItem("desert_bone");
        createSubItem("diamond_bone");
        createSubItem("earth_bone");
        createSubItem("emerald_bone");
        createSubItem("end_bone");
        createSubItem("ender_bone");
        createSubItem("fire_bone");
        createSubItem("flower_bone");
        createSubItem("gold_bone");
        createSubItem("ice_bone");
        createSubItem("iron_bone");
        createSubItem("mesa_bone");
        createSubItem("mushroom_bone");
        createSubItem("nether_bone");
        createSubItem("prismarine_bone");
        createSubItem("redstone_bone");
        createSubItem("savannah_bone");
        createSubItem("skeleton_bone");
        createSubItem("water_bone");
        createSubItem("zombie_bone");
        createSubItem("meaty_bone");
    }

    public void registerRecipes() {
        registerRecipe("air_bone", new ItemStack(Items.glass_bottle));
        registerRecipe("birch_bone", new ItemStack(Blocks.sapling, 1, 2));
        registerRecipe("cake_bone", new ItemStack(Items.cake));
        registerRecipe("desert_bone", new ItemStack(Blocks.cactus));
        registerRecipe("diamond_bone", new ItemStack(Items.diamond));
        registerRecipe("earth_bone", new ItemStack(Items.wooden_shovel));
        registerRecipe("emerald_bone", new ItemStack(Items.emerald));
        registerRecipe("end_bone", new ItemStack(Items.ender_eye));
        registerRecipe("ender_bone", new ItemStack(Items.ender_pearl));
        registerRecipe("fire_bone", new ItemStack(Items.blaze_powder));
        registerRecipe("flower_bone", new ItemStack(Blocks.double_plant, 1, 5));
        registerRecipe("gold_bone", new ItemStack(Items.gold_ingot));
        registerRecipe("ice_bone", new ItemStack(Items.snowball));
        registerRecipe("iron_bone", new ItemStack(Items.iron_ingot));
        registerRecipe("mesa_bone", new ItemStack(Blocks.deadbush));
        registerRecipe("mushroom_bone", new ItemStack(Blocks.red_mushroom));
        registerRecipe("nether_bone", new ItemStack(Items.magma_cream));
        registerRecipe("prismarine_bone", EtFuturumHolder.getPrismarineCrystalItemStack());
        registerRecipe("redstone_bone", new ItemStack(Items.redstone));
        registerRecipe("savannah_bone", new ItemStack(Blocks.grass));
        registerRecipe("skeleton_bone", new ItemStack(Items.dye, 1, 15));
        registerRecipe("water_bone", new ItemStack(Items.water_bucket));
        registerRecipe("zombie_bone", new ItemStack(Items.rotten_flesh));
        registerRecipe("meaty_bone", new ItemStack(Items.beef));
    }

    private void registerRecipe(String id, ItemStack craftedWith) {
        if (craftedWith != null)
            GameRegistry.addShapelessRecipe(getFood(id), Items.bone, craftedWith);
    }

    @Override
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
    public void getSubItems(final Item item, final CreativeTabs tab, final List list) {
        for (Map.Entry<String, ItemStack> entry : foods.entrySet()) {
            list.add(entry.getValue());
        }
    }

    protected ItemStack createSubItem(String id) {
        final ItemStack itemStack = new ItemStack(this, 1);
        itemStack.setTagCompound(new NBTTagCompound());
        itemStack.getTagCompound().setString(ID_TAG, id);
        foods.put(id, itemStack);
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
        if (a == null || b == null || !(a.getItem() instanceof ItemWolfFood && b.getItem() instanceof ItemWolfFood))
            return false;

        return a.getTagCompound().getString(ID_TAG).equals(b.getTagCompound().getString(ID_TAG));

    }
}
