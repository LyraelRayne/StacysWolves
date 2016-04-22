package au.lyrael.stacywolves.utility;

import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class WorldHelper {

    public static boolean canSeeTheSky(World world, double x, double y, double z) {
        return world.canBlockSeeTheSky(MathHelper.floor_double(x),
                MathHelper.floor_double(y),
                MathHelper.floor_double(z));
    }
}
