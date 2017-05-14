package au.lyrael.stacywolves.annotation;

import au.lyrael.stacywolves.entity.SpawnWeights;
import au.lyrael.stacywolves.registry.WolfType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface WolfMetadata {

    public static final int MAX_SPAWN_PROBABILITY = 1000;

    String name();

    int primaryColour();

    int secondaryColour();

    WolfSpawn[] spawns() default {};

    /**
     *
     * @return Chance out of @{link MAX_SPAWN_PROBABILITY} that the wolf will actually spawn.
     * For most wolves this is 100%
     * Decrease to make the wolf more rare.
     */
    int probability() default SpawnWeights.SPAWN_PROBABILITY_MOSTLY;

    /**
     * Type of creature this is considered as for spawn caps and such.
     */
    WolfType type() default WolfType.NORMAL;
}
