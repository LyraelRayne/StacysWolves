package au.lyrael.stacywolves.utility;

import au.lyrael.stacywolves.annotation.WolfMetadata;

import java.text.MessageFormat;

public class MetadataUtility {
    public static String toString(WolfMetadata metadata) {
        return MessageFormat.format("Dragon Metadata : [ [name] -> [{0}], [primaryColour] -> [{1}], [secondaryColour] -> [{2}], [numSpawns] -> [{3}] ]}", metadata.name(), metadata.primaryColour(), metadata.secondaryColour(), metadata.spawns().length);
    }
}
