package au.lyrael.stacywolves.utility;

import au.lyrael.stacywolves.annotation.WolfMetadata;

import java.text.MessageFormat;

public class MetadataUtility {
    public static String toString(WolfMetadata metadata) {
        return MessageFormat.format("Wolf Metadata", metadata.name(), metadata.primaryColour(), metadata.secondaryColour(), metadata.spawns().length);
    }
}
