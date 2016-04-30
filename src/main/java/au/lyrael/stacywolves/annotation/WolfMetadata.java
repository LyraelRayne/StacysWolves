package au.lyrael.stacywolves.annotation;

import au.lyrael.stacywolves.registry.WolfType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface WolfMetadata {
    String name();

    int primaryColour();

    int secondaryColour();

    WolfSpawn[] spawns() default {};

    /**
     * Type of creature this is considered as for spawn caps and such.
     */
    WolfType type() default WolfType.NORMAL;
}
