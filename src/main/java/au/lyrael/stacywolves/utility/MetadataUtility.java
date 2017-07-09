package au.lyrael.stacywolves.utility;

import au.lyrael.stacywolves.annotation.WolfMetadata;

import java.text.MessageFormat;

public class MetadataUtility {
    public static String toString(WolfMetadata metadata) {
        return MessageFormat.format("Wolf Metadata [name = {0}, primaryColor = {1}, secondaryColor = {2}, number of spawn entries {3}]", metadata.name(), metadata.primaryColour(), metadata.secondaryColour(), metadata.spawns().length);
    }
}
