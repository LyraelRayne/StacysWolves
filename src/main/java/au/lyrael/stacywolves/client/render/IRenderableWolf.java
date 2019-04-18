package au.lyrael.stacywolves.client.render;

import au.lyrael.stacywolves.entity.wolf.IWolf;
import net.minecraft.item.EnumDyeColor;

/**
 * Created by Lyrael on 23/03/2016.
 */
public interface IRenderableWolf extends IWolf {

    String getTextureFolderName();

    float getTailRotation();

    float getWolfBrightness(float maybeTime);

    float getShadingWhileShaking(float maybeTime);

    EnumDyeColor getCollarColor();

}
