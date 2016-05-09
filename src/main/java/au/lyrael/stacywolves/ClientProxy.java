package au.lyrael.stacywolves;

import au.lyrael.stacywolves.client.entity.model.ModelWolf;
import au.lyrael.stacywolves.client.render.RenderWolf;
import au.lyrael.stacywolves.client.render.RenderWolfTransporter;
import au.lyrael.stacywolves.entity.EntityWolfTransporter;
import au.lyrael.stacywolves.entity.wolf.EntityWolfBase;
import au.lyrael.stacywolves.registry.ItemRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);

    }

    @Override
    public void init() {
        super.init();
        registerRenderers();
    }

    protected void registerRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(EntityWolfBase.class,
                new RenderWolf(new ModelWolf(), new ModelWolf(), 0.5F));
        RenderingRegistry.registerEntityRenderingHandler(EntityWolfTransporter.class, new RenderWolfTransporter(ItemRegistry.wolf_transporter));
    }
}