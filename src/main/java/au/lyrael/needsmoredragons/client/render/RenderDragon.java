package au.lyrael.needsmoredragons.client.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;

import static au.lyrael.needsmoredragons.NeedsMoreDragons.MOD_ID;
import static au.lyrael.needsmoredragons.NeedsMoreDragons.dragonRegistry;

public class RenderDragon extends RenderLiving {

    private static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    private static Map<String, IDragonTextureSet> textureCache = new HashMap<>();

    public RenderDragon(ModelBase model, ModelBase renderPassModel, float shadowSize) {
        super(model, shadowSize);
        this.setRenderPassModel(renderPassModel);
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    protected int shouldRenderPass(IRenderableDragon dragon, int pass, float maybeTime) {
        if (pass == 0 && dragon.isShaking()) {
            float f1 = dragon.getBrightness(maybeTime) * dragon.getShadingWhileShaking(maybeTime);
            bindDragonTexture(dragon);
            GL11.glColor3f(f1, f1, f1);
            return 1;
        } else {
            return -1;
        }
    }

    private void bindDragonTexture(IRenderableDragon dragon) {
        final ResourceLocation entityTexture = this.getEntityTexture(dragon);
        bindTexture(entityTexture);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(IRenderableDragon entity) {
        final String entityName = dragonRegistry.getEntityNameFor(entity);

        if (!textureCache.containsKey(entityName))
            textureCache.put(entityName, createNewTextureSet(entity));

        IDragonTextureSet textureSet = textureCache.get(entityName);

        if (textureSet != null) {
            return entity.isTamed() ? textureSet.getTame() : (entity.isAngry() ? textureSet.getAngry() : textureSet.getBase());
        } else {
            LOGGER.warn("Can't load texture set for [{}]", entityName);
            return null;
        }
    }

    private IDragonTextureSet createNewTextureSet(IRenderableDragon entity) {
        return new BasicDragonTextureSet(entity.getTextureFolderName());
    }

    /**
     * Defines what float the third param in setRotationAngles of ModelBase is
     */
    protected float handleRotationFloat(IRenderableDragon entity) {
        return entity.getTailRotation();
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    @Override
    protected int shouldRenderPass(EntityLivingBase wolf, int pass, float maybeTime) {
        return this.shouldRenderPass((IRenderableDragon) wolf, pass, maybeTime);
    }

    /**
     * Defines what float the third param in setRotationAngles of ModelBase is
     */
    @Override
    protected float handleRotationFloat(EntityLivingBase wolf, float maybeTime) {
        return this.handleRotationFloat((IRenderableDragon) wolf);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
    protected ResourceLocation getEntityTexture(Entity dragon) {
        return this.getEntityTexture((IRenderableDragon) dragon);
    }

}