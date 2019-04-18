package au.lyrael.stacywolves.item;

import au.lyrael.stacywolves.integration.EtFuturumHolder;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static au.lyrael.stacywolves.StacyWolves.CREATIVE_TAB;
import static au.lyrael.stacywolves.StacyWolves.MOD_ID;

public class ItemWolfFood extends ItemStacyWolves implements IRegisterMyOwnRecipes {

    private static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static final String WOLF_FOOD_NAME = "wolf_food";

    public static final String ID_TAG = "wolf_food_id";

    private final Map<String, ItemStack> foods = new HashMap<>();

    public ItemWolfFood() {
        super();
        this.setCreativeTab(CREATIVE_TAB);
        this.setUnlocalizedName(WOLF_FOOD_NAME);
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

        createSubItem("inky_bone");
        createSubItem("solid_prismarine_bone");
        createSubItem("glow_bone");
        createSubItem("book_bone");
        createSubItem("sunflower_bone");
        createSubItem("cookie_bone");
        createSubItem("melon_bone");
        createSubItem("lapis_bone");
        createSubItem("coal_bone");
        createSubItem("red_sand_bone");
        createSubItem("fish_bone");
        createSubItem("apple_bone");
        createSubItem("crafty_bone");
        createSubItem("wheat_bone");
        createSubItem("torch_bone");
        createSubItem("slime_bone");
    }

    // TODO Figure out how to JSON this
    @Override
    public void registerRecipes() {
        registerRecipe("air_bone", new ItemStack(Items.GLASS_BOTTLE));
        registerRecipe("birch_bone", new ItemStack(Blocks.SAPLING, 1, 2));
        registerRecipe("cake_bone", new ItemStack(Items.CAKE));
        registerRecipe("desert_bone", new ItemStack(Blocks.CACTUS));
        registerRecipe("diamond_bone", new ItemStack(Items.DIAMOND));
        registerRecipe("earth_bone", new ItemStack(Items.WOODEN_SHOVEL));
        registerRecipe("emerald_bone", new ItemStack(Items.EMERALD));
        registerRecipe("end_bone", new ItemStack(Items.ENDER_EYE));
        registerRecipe("ender_bone", new ItemStack(Items.ENDER_PEARL));
        registerRecipe("fire_bone", new ItemStack(Items.BLAZE_POWDER));
        registerRecipe("flower_bone", new ItemStack(Blocks.DOUBLE_PLANT, 1, 5));
        registerRecipe("gold_bone", new ItemStack(Items.GOLD_INGOT));
        registerRecipe("ice_bone", new ItemStack(Items.SNOWBALL));
        registerRecipe("iron_bone", new ItemStack(Items.IRON_INGOT));
        registerRecipe("mesa_bone", new ItemStack(Blocks.DEADBUSH));
        registerRecipe("mushroom_bone", new ItemStack(Blocks.RED_MUSHROOM));
        registerRecipe("nether_bone", new ItemStack(Items.MAGMA_CREAM));
        registerRecipe("prismarine_bone", EtFuturumHolder.getPrismarineCrystalItemStack());
        registerRecipe("redstone_bone", new ItemStack(Items.REDSTONE));
        registerRecipe("savannah_bone", new ItemStack(Blocks.TALLGRASS, 1, 1));
        registerRecipe("skeleton_bone", new ItemStack(Items.DYE, 1, 15));
        registerRecipe("water_bone", new ItemStack(Items.WATER_BUCKET));
        registerRecipe("zombie_bone", new ItemStack(Items.ROTTEN_FLESH));
        registerRecipe("meaty_bone", new ItemStack(Items.BEEF));

        registerRecipe("inky_bone", new ItemStack(Items.DYE));
        registerRecipe("solid_prismarine_bone", EtFuturumHolder.getPrismarineShardItemStack());
        registerRecipe("glow_bone", new ItemStack(Items.GLOWSTONE_DUST));
        registerRecipe("book_bone", new ItemStack(Items.BOOK));
        registerRecipe("sunflower_bone", new ItemStack(Blocks.DOUBLE_PLANT,1,0));
        registerRecipe("cookie_bone", new ItemStack(Items.COOKIE));
        registerRecipe("melon_bone", new ItemStack(Items.MELON));
        registerRecipe("lapis_bone", new ItemStack(Items.DYE,1,4));
        registerRecipe("coal_bone", new ItemStack(Items.COAL));
        registerRecipe("red_sand_bone", new ItemStack(Blocks.SAND, 1, 1));
        registerRecipe("fish_bone", new ItemStack(Items.FISH));
        registerRecipe("apple_bone", new ItemStack(Items.APPLE));
        registerRecipe("crafty_bone", new ItemStack(Blocks.CRAFTING_TABLE));
        registerRecipe("wheat_bone", new ItemStack(Items.WHEAT));
        registerRecipe("torch_bone", new ItemStack(Blocks.TORCH));
        registerRecipe("slime_bone", new ItemStack(Items.SLIME_BALL));
    }

    private void registerRecipe(String id, ItemStack craftedWith) {
        final ItemStack food = getFood(id);

//        if (craftedWith != null && food != null) {
//            GameRegistry.addShapelessRecipe(food, Items.BONE, craftedWith);
//            if (id.endsWith("_bone"))
//                GameRegistry.addShapelessRecipe(new ItemStack(Items.DYE, 5, 15), food);
//        }
        // TODO JSONIFY
    }
//    @Override
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

//    public void registerOreDict() {
//        registerOreDict("air_bone", "bone");
//        registerOreDict("birch_bone", "bone");
//        registerOreDict("cake_bone", "bone");
//        registerOreDict("desert_bone", "bone");
//        registerOreDict("diamond_bone", "bone");
//        registerOreDict("earth_bone", "bone");
//        registerOreDict("emerald_bone", "bone");
//        registerOreDict("end_bone", "bone");
//        registerOreDict("ender_bone", "bone");
//        registerOreDict("fire_bone", "bone");
//        registerOreDict("flower_bone", "bone");
//        registerOreDict("gold_bone", "bone");
//        registerOreDict("ice_bone", "bone");
//        registerOreDict("iron_bone", "bone");
//        registerOreDict("mesa_bone", "bone");
//        registerOreDict("mushroom_bone", "bone");
//        registerOreDict("nether_bone", "bone");
//        registerOreDict("prismarine_bone", "bone");
//        registerOreDict("redstone_bone", "bone");
//        registerOreDict("savannah_bone", "bone");
//        registerOreDict("skeleton_bone", "bone");
//        registerOreDict("water_bone", "bone");
//        registerOreDict("zombie_bone", "bone");
//        registerOreDict("meaty_bone", "bone");
//    }
//
//    private void registerOreDict(String id, String oreDictName) {
//        final ItemStack food = getFood(id);
//
//        if (oreDictName != null && food != null) {
//            OreDictionary.registerOre(oreDictName, food);
//        } else {
//            LOGGER.warn("Tried to register [{}] as [{}] but one was null.", id, oreDictName);
//        }
//    }
}
