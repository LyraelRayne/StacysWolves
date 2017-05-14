package au.lyrael.stacywolves.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface WolfSpawn {

    /**
     * Probabilistic weight of spawning in {@link #spawnBiomes()}
     * If a chunk can spawn 5 types of wolf
     * Earth - 50
     * Cake - 5
     * Mesa - 50
     *
     * Then cake wolves will spawn 5/105 times. Earth wolves 50/105, mesa wolves 50/105.
     *
     * This is not a probability of spawning! It's the probability of being selected out of a list.
     * If only one kind of wolf can spawn, that wolf WILL spawn.
     *
     * @see net.minecraft.util.WeightedRandom
     */
    int weight();

    /**
     * Minimum number of wolves to spawn in a pack.
     */
    int min();

    /**
     * Maximum number of wolves to spawn in a pack.
     */
    int max();

    WolfSpawnBiome[] spawnBiomes() default {};
}
