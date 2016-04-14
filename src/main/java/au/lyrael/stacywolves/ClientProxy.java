package au.lyrael.stacywolves;

import au.lyrael.stacywolves.client.entity.model.ModelWolf;
import au.lyrael.stacywolves.client.render.RenderWolf;
import au.lyrael.stacywolves.entity.wolf.EntityWolfBase;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        RenderingRegistry.registerEntityRenderingHandler(EntityWolfBase.class,
                new RenderWolf(new ModelWolf(), new ModelWolf(), 0.5F));
    }


    @Override
    public void init() {
        super.init();
    }


    protected void registerRenderers() {
    }
}