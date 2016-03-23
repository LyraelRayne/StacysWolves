package au.lyrael.needsmoredragons.client.render;

import net.minecraft.util.ResourceLocation;

import static au.lyrael.needsmoredragons.NeedsMoreDragons.MOD_ID;

public class BasicDragonTextureSet implements IDragonTextureSet {

    protected static String textureBasePath = "textures/entity/dragon";

    private ResourceLocation base;
    private ResourceLocation angry;
    private ResourceLocation tame;

    @Override
    public ResourceLocation getBase() {
        return base;
    }

    @Override
    public ResourceLocation getAngry() {
        return angry;
    }

    @Override
    public ResourceLocation getTame() {
        return tame;
    }

    //TODO Make textures different for different states if needed. Otherwise remove different textures.
    public BasicDragonTextureSet(String folderName) {
        base = new ResourceLocation(MOD_ID, textureBasePath + "/" + folderName + "/base.png");
        angry = base;
        tame = base;
//        angry = new ResourceLocation(MOD_ID, textureBasePath + "/" + folderName + "/angry.png");
//        tame = new ResourceLocation(MOD_ID, textureBasePath + "/" + folderName + "/tame.png");
    }
}
