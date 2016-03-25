package au.lyrael.needsmoredragons;

import au.lyrael.needsmoredragons.client.entity.model.ModelDragon;
import au.lyrael.needsmoredragons.client.render.RenderDragon;
import au.lyrael.needsmoredragons.entity.dragon.EntityDragonBase;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        RenderingRegistry.registerEntityRenderingHandler(EntityDragonBase.class,
                new RenderDragon(new ModelDragon(), new ModelDragon(), 0.5F));
    }


    @Override
    public void init() {
        super.init();
    }


    protected void registerRenderers() {
    }
}