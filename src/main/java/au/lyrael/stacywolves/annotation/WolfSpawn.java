package au.lyrael.stacywolves.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface WolfSpawn {

    /**
     * Probability of spawning in {@link #spawnBiomes()}
     */
    int probability();

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
