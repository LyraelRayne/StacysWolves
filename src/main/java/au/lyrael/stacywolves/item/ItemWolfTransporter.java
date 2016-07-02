package au.lyrael.stacywolves.item;

import au.lyrael.stacywolves.entity.EntityWolfTransporter;
import au.lyrael.stacywolves.utility.LanguageHelper;
import com.google.common.collect.ImmutableMap;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static au.lyrael.stacywolves.StacyWolves.CREATIVE_TAB;
import static au.lyrael.stacywolves.StacyWolves.MOD_ID;
import static au.lyrael.stacywolves.utility.WolfTransporterUtility.containsWolf;
import static au.lyrael.stacywolves.utility.WolfTransporterUtility.getWolfDetails;

public class ItemWolfTransporter extends ItemStacyWolves implements IRegisterMyOwnRecipes {

    public static final String WOLF_TRANSPORTER_NAME = "wolf_transporter";

    private IIcon emptyIcon;
    private IIcon fullIcon;

    public ItemWolfTransporter() {
        super();
        this.setCreativeTab(CREATIVE_TAB);
        this.setUnlocalizedName(WOLF_TRANSPORTER_NAME);
        this.setMaxStackSize(1);
        this.setMaxDamage(0);
        this.showTooltipsAlways(true);
        canRepair = false;
    }

    @Override
    public boolean hasEffect(ItemStack p_77636_1_) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.rare;
    }

    @Override
    public void registerIcons(IIconRegister iconRegister) {
        emptyIcon = iconRegister.registerIcon(String.format("%s/empty", unwrapUnlocalizedName(this.getUnlocalizedName())));
        fullIcon = iconRegister.registerIcon(String.format("%s/full", unwrapUnlocalizedName(this.getUnlocalizedName())));
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
            return containsWolf(stack) ? fullIcon : emptyIcon;
        } else {
            // Fallback to default, because what else are we going to do?
            return emptyIcon;
        }
    }

    /**
     * Just a call to formatTooltip(). If you are overriding this function, call
     * formatTooltip() directly and DO NOT call super.addInformation().
     */
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean whatDoesThisEvenDo) {
        Map<String, String> params = new HashMap<>();
        final NBTTagCompound wolfDetails = getWolfDetails(stack);
        if (wolfDetails != null) {
            final String id = wolfDetails.getString("id");
            final String typeName = LanguageHelper.getLocalization(String.format("entity.%s.%s.name", MOD_ID, unwrapUnlocalizedName(id)));
            params.put("type", typeName);
            params.put("name", wolfDetails.getString("CustomName"));
        }
        this.formatTooltip(ImmutableMap.copyOf(params), stack, list);
    }

    /**
     * Used to format tooltips. Grabs tooltip from language registry with the
     * entry 'item.unlocalizedName.tooltip'. Has support for Handlebars-style
     * templating, and line breaking using '\n'.
     *
     * @param params An ImmutableMap that has all the regex keys and values. Regex
     *               strings are handled on the tooltip by including '{{regexKey}}'
     *               with your regex key, of course.
     * @param stack  The ItemStack passed from addInformation.
     * @param list   List of description lines passed from addInformation.
     */
    public void formatTooltip(ImmutableMap<String, String> params, ItemStack stack, List list) {
        if (showTooltipsAlways() || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
            LanguageHelper.formatTooltip(this.getUnlocalizedNameInefficiently(stack) + ".tooltip", params, stack, list);

            if (!StringUtils.isNullOrEmpty(params.get("type"))) {
                list.add("");
                final String name = params.get("name");
                final String contentsKey = StringUtils.isNullOrEmpty(name) ? "unnamedcontents" : "namedcontents";
                LanguageHelper.formatTooltip(String.format("%s.%s.tooltip", this.getUnlocalizedNameInefficiently(stack), contentsKey), params, stack, list);
            }
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        if (par2World.isRemote)
            return par1ItemStack;

        par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
        par2World.spawnEntityInWorld(new EntityWolfTransporter(par2World, par3EntityPlayer, par1ItemStack.copy()));

        if (!par3EntityPlayer.capabilities.isCreativeMode) {
            --par1ItemStack.stackSize;
        }

        return par1ItemStack;
    }

    @Override
    public void registerRecipes() {
        GameRegistry.addRecipe(new ItemStack(this), new Object[]{" E ", "E E", " E ", 'E', Items.ender_eye});

    }
}
