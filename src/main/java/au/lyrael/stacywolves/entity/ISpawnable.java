package au.lyrael.stacywolves.entity;

import net.minecraft.world.World;

public interface ISpawnable {
    /**
     * When the game tries to spawn a spawnable, this method will be used to determine whether it is allowed to continue spawning.
     *
     * @param world
     * @param x
     * @param y
     * @param z
     * @return false if the spawn is to be prevented. True if it is not.
     */
    boolean canSpawnHereAndNow(World world, float x, float y, float z);
}
