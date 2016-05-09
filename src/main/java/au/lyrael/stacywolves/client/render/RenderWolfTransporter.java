package au.lyrael.stacywolves.client.render;

import au.lyrael.stacywolves.entity.EntityWolfTransporter;
import au.lyrael.stacywolves.item.ItemStacyWolves;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import static au.lyrael.stacywolves.StacyWolves.MOD_ID;

@SideOnly(Side.CLIENT)
public class RenderWolfTransporter extends Render
{
    private static final Logger LOGGER = LogManager.getLogger(MOD_ID + ".render");

    private ItemStacyWolves item;

    public RenderWolfTransporter(ItemStacyWolves item)
    {
        this.setItem(item);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void func_76986_a(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */

    @Override
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
        this.bindEntityTexture(par1Entity);
        GL11.glPushMatrix();
        GL11.glTranslatef((float) par2, (float) par4, (float) par6);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        Tessellator var10 = Tessellator.instance;

        int itemIconIndex = 0;

        if(par1Entity instanceof EntityWolfTransporter)
        {
            itemIconIndex = ((EntityWolfTransporter)par1Entity).getTextureIndex();
        }

        float var3 = (itemIconIndex % 16 * 16 + 0) / 256.0F;
        float var4 = (itemIconIndex % 16 * 16 + 16) / 256.0F;
        float var5 = (itemIconIndex / 16 * 16 + 0) / 256.0F;
        float var6 = (itemIconIndex / 16 * 16 + 16) / 256.0F;
        float var7 = 1.0F;
        float var8 = 0.5F;
        float var9 = 0.25F;
        GL11.glRotatef(180.0F - renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        var10.startDrawingQuads();
        var10.setNormal(0.0F, 1.0F, 0.0F);
        var10.addVertexWithUV(0.0F - var8, 0.0F - var9, 0.0D, var3, var6);
        var10.addVertexWithUV(var7 - var8, 0.0F - var9, 0.0D, var4, var6);
        var10.addVertexWithUV(var7 - var8, var7 - var9, 0.0D, var4, var5);
        var10.addVertexWithUV(0.0F - var8, var7 - var9, 0.0D, var3, var5);
        var10.draw();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return new ResourceLocation(MOD_ID, "textures/entity/thrownItemsSheet.png");
    }

    public ItemStacyWolves getItem() {
        return item;
    }

    public void setItem(ItemStacyWolves item) {
        this.item = item;
    }
}