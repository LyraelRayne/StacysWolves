package au.lyrael.stacywolves.client.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;

import static au.lyrael.stacywolves.StacyWolves.MOD_ID;
import static au.lyrael.stacywolves.StacyWolves.WOLF_REGISTRY;

public class RenderWolf extends RenderLiving {

    private static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    private static Map<String, IWolfTextureSet> textureCache = new HashMap<>();

    public RenderWolf(ModelBase model, ModelBase renderPassModel, float shadowSize) {
        super(model, shadowSize);
        this.setRenderPassModel(renderPassModel);
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    protected int shouldRenderPass(IRenderableWolf wolf, int pass, float maybeTime) {
        if (pass == 0 && wolf.isWolfShaking()) {
            float f1 = wolf.getBrightness(maybeTime) * wolf.getShadingWhileShaking(maybeTime);
            bindTexture(this.getEntityTexture(wolf));
            GL11.glColor3f(f1, f1, f1);
            return 1;
        } else if(pass == 1 && wolf.isWolfTamed()) {
            bindTexture(this.getCollarTexture(wolf));
            int color = wolf.getCollarColor();
            GL11.glColor3f(EntitySheep.fleeceColorTable[color][0], EntitySheep.fleeceColorTable[color][1], EntitySheep.fleeceColorTable[color][2]);
            return 1;
        }
        else{
            return -1;
        }
    }

    private ResourceLocation getCollarTexture(IRenderableWolf entity) {
        final String entityName = WOLF_REGISTRY.getEntityNameFor(entity);

        IWolfTextureSet textureSet = getTextureSet(entity, entityName);

        if (textureSet != null) {
            return textureSet.getCollar();
        } else {
            LOGGER.warn("Can't load texture set for [{}]", entityName);
            return null;
        }
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(IRenderableWolf entity) {
        final String entityName = WOLF_REGISTRY.getEntityNameFor(entity);

        IWolfTextureSet textureSet = getTextureSet(entity, entityName);

        if (textureSet != null) {
            return entity.isWolfTamed() ? textureSet.getTame() : (entity.isWolfAngry() ? textureSet.getAngry() : textureSet.getBase());
        } else {
            LOGGER.warn("Can't load texture set for [{}]", entityName);
            return null;
        }
    }

    private IWolfTextureSet getTextureSet(IRenderableWolf entity, String entityName) {
        if (!textureCache.containsKey(entityName))
            textureCache.put(entityName, createNewTextureSet(entity));

        return textureCache.get(entityName);
    }

    private IWolfTextureSet createNewTextureSet(IRenderableWolf entity) {
        return new BasicWolfTextureSet(entity.getTextureFolderName());
    }

    /**
     * Defines what float the third param in setRotationAngles of ModelBase is
     */
    protected float handleRotationFloat(IRenderableWolf entity) {
        return entity.getTailRotation();
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    @Override
    protected int shouldRenderPass(EntityLivingBase wolf, int pass, float maybeTime) {
        return this.shouldRenderPass((IRenderableWolf) wolf, pass, maybeTime);
    }

    /**
     * Defines what float the third param in setRotationAngles of ModelBase is
     */
    @Override
    protected float handleRotationFloat(EntityLivingBase wolf, float maybeTime) {
        return this.handleRotationFloat((IRenderableWolf) wolf);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
    protected ResourceLocation getEntityTexture(Entity wolf) {
        return this.getEntityTexture((IRenderableWolf) wolf);
    }

}