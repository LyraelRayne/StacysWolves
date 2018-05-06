package au.lyrael.stacywolves.utility;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class WorldHelper {

    public static boolean canSeeTheSky(World world, double x, double y, double z) {
        return world.canBlockSeeTheSky(MathHelper.floor_double(x),
                MathHelper.floor_double(y),
                MathHelper.floor_double(z));
    }

    public static int getFullBlockLightValue(World world, double x, double y, double z) {
        return world.getFullBlockLightValue(MathHelper.floor_double(x), MathHelper.floor_double(y), MathHelper.floor_double(z));
    }

    public static void spawnParticle(World worldObj, String particleToSpawn, int numParticlesToSpawn, double spread, double originX, double originY, double originZ) {
        for (int particles = 0; particles < numParticlesToSpawn; particles++) {
            worldObj.spawnParticle(particleToSpawn, originX + worldObj.rand.nextDouble() * spread, originY + worldObj.rand.nextDouble() * spread, originZ + worldObj.rand.nextDouble() * spread, 0D, 0D, 0D);
        }
    }

    public static double getFloorY(final World world, final double maxY, final double x, final double z) {
        boolean found = false;
        double currentY = maxY;
        while (!found) {
            if (world.isSideSolid((int) x, (int) currentY, (int) z, ForgeDirection.UP)) {
                found = true;
            } else {
                currentY -= 1;
            }
        }
        return currentY + 1;
    }
}
