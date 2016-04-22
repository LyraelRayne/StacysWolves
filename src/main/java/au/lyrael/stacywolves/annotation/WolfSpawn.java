package au.lyrael.stacywolves.annotation;

import net.minecraftforge.common.BiomeDictionary;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface WolfSpawn {

    /**
     * Probability of spawning in {@link #biomeTypes()}
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

    /**
     * Biome types to spawn wolves of this type in. A biome must have all types in order to qualify.
     * (For example HOT, DRY would mean that desert would qualify but Jungle would not.
     */
    BiomeDictionary.Type[] biomeType();
}
