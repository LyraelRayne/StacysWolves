package au.lyrael.stacywolves.annotation;


import net.minecraftforge.common.BiomeDictionary;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static net.minecraftforge.common.BiomeDictionary.Type.END;
import static net.minecraftforge.common.BiomeDictionary.Type.NETHER;

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface WolfSpawnBiome {
    /**
     * Biome types to spawn wolves of this type in. A biome must have all types in order to qualify.
     * (For example HOT, DRY would mean that desert would qualify but Jungle would not.
     */
    BiomeDictionary.Type[] requireBiomeTypes() default {};

    /**
     * Biome types to exclude wolves of this type from. Useful if you want a meta biome type but want to exclude a particular type.
     */
    BiomeDictionary.Type[] excludeBiomeTypes() default {NETHER, END};

    /**
     * List of specific biome names to exclude spawns from.
     */
    String[] excludeBiomeNames() default {};

    /**
     * These biomes will be added to the list regardless of the other parameters.
     */
    String[] specificBiomes() default {};
}
