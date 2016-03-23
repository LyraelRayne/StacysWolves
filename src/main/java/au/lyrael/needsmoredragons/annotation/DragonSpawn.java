package au.lyrael.needsmoredragons.annotation;

import net.minecraftforge.common.BiomeDictionary;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DragonSpawn {

    /**
     * Probability of spawning in {@link #biomeType()}
     */
    int probability();

    /**
     * Minimum number of dragons to spawn in a pack.
     */
    int min();

    /**
     * Maximum number of dragons to spawn in a pack.
     */
    int max();

    /**
     * Biome types to spawn dragons of this type in. A biome must have all types in order to qualify.
     * (For example HOT, DRY would mean that desert would qualify but Jungle would not.
     */
    BiomeDictionary.Type[] biomeType();
}
