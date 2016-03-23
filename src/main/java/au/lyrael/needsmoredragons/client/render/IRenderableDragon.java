package au.lyrael.needsmoredragons.client.render;

import au.lyrael.needsmoredragons.entity.dragon.IDragon;

/**
 * Created by Lyrael on 23/03/2016.
 */
public interface IRenderableDragon extends IDragon {

    String getTextureFolderName();

    float getTailRotation();

    float getBrightness(float maybeTime);

    float getShadingWhileShaking(float maybeTime);

}
