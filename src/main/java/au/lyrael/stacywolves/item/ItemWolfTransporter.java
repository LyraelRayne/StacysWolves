package au.lyrael.stacywolves.item;

import au.lyrael.stacywolves.entity.EntityWolfTransporter;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import static au.lyrael.stacywolves.StacyWolves.CREATIVE_TAB;
import static au.lyrael.stacywolves.utility.WolfTransporterUtility.containsWolf;

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
        GameRegistry.addRecipe(new ItemStack(this), new Object[] {"PEP", "E E", "PEP", 'P', Items.ender_pearl , 'E', Items.ender_eye});
        GameRegistry.addRecipe(new ItemStack(this), new Object[] {"EPE", "P P", "EPE", 'P', Items.ender_pearl , 'E', Items.ender_eye});

    }
}
