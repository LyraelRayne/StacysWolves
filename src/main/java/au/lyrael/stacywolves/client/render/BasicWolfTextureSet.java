package au.lyrael.stacywolves.client.render;

import net.minecraft.util.ResourceLocation;

import static au.lyrael.stacywolves.StacyWolves.MOD_ID;

public class BasicWolfTextureSet implements IWolfTextureSet {

    protected static String textureBasePath = "textures/entity/wolf";

    private ResourceLocation base;
    private ResourceLocation angry;
    private ResourceLocation tame;
    private ResourceLocation collar;

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

    @Override
    public ResourceLocation getCollar() {
        return collar;
    }

    public BasicWolfTextureSet(String folderName) {
        base = new ResourceLocation(MOD_ID, textureBasePath + "/" + folderName + "/" + folderName + "_wolf.png");
        angry = new ResourceLocation(MOD_ID, textureBasePath + "/" + folderName + "/" + folderName + "_wolf_angry.png");
        tame = new ResourceLocation(MOD_ID, textureBasePath + "/" + folderName + "/" + folderName + "_wolf_tame.png");
        collar = new ResourceLocation(MOD_ID, textureBasePath + "/wolf_collar.png");
    }
}
