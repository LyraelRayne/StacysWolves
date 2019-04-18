package au.lyrael.stacywolves.utility;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class WorldHelper {

    public static boolean canSeeTheSky(World world, double x, double y, double z) {
        return world.canBlockSeeSky(new BlockPos(MathHelper.floor(x),
                MathHelper.floor(y),
                MathHelper.floor(z)));
    }

    public static int getFullBlockLightValue(World world, double x, double y, double z) {
        final BlockPos pos = new BlockPos(x, y, z);
        return world.getBlockState(pos).getLightValue(world, pos);
    }
}
