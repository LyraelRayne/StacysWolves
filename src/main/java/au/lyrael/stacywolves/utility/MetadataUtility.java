package au.lyrael.stacywolves.utility;

import au.lyrael.stacywolves.annotation.WolfMetadata;

import java.text.MessageFormat;

public class MetadataUtility {
    public static String toString(WolfMetadata metadata) {
        return MessageFormat.format("Wolf Metadata [name = {1}, primaryColor = {2}, secondaryColor = {3}, number of spawn entries {4}]", metadata.name(), metadata.primaryColour(), metadata.secondaryColour(), metadata.spawns().length);
    }
}
