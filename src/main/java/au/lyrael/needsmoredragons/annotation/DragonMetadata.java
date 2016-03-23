package au.lyrael.needsmoredragons.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DragonMetadata {
    String name();

    int primaryColour();

    int secondaryColour();

    DragonSpawn[] spawns();
}
