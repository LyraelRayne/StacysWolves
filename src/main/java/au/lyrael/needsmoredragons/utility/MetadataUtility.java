package au.lyrael.needsmoredragons.utility;

import au.lyrael.needsmoredragons.annotation.DragonMetadata;
import com.sun.org.apache.xerces.internal.util.MessageFormatter;

import java.text.MessageFormat;

public class MetadataUtility {
    public static String toString(DragonMetadata metadata) {
        return MessageFormat.format("Dragon Metadata : [ [name] -> [{0}], [primaryColour] -> [{1}], [secondaryColour] -> [{2}], [numSpawns] -> [{3}] ]}", metadata.name(), metadata.primaryColour(), metadata.secondaryColour(), metadata.spawns().length);
    }
}
