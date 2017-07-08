package au.lyrael.stacywolves.config;

import static au.lyrael.stacywolves.StacyWolves.MOD_ID;
import static au.lyrael.stacywolves.StacyWolves.MOD_NAME;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;


public class ConfigurationLoader {

    private static final Logger LOGGER = LogManager.getLogger(MOD_ID + ".Config");

    public void loadConfiguration(Configuration configuration) {
        try {
            LOGGER.info("Loading configuration....");
            loadGeneralSettings(configuration);

            LOGGER.info("Loaded!");
        } catch (Exception e) {
            LOGGER.error("Failed to load configuration.");
            throw new RuntimeException("Failed to load " + MOD_NAME + " configuration.", e);
        } finally {
            if (configuration.hasChanged()) {
                LOGGER.info("Configuration has changed. Saving....");
                configuration.save();
                LOGGER.debug("Saved!");
            }

            logLoadedConfiguration(configuration);
        }
    }

    private void loadGeneralSettings(Configuration configuration) {
        final String category = "general";
        LOGGER.debug("Loading configuration category [{}]", category);
        configuration.setCategoryComment(category, "General settings");
        configuration.setCategoryRequiresMcRestart(category, true);

        for (int dimension : configuration.get(category, "dimensionSpawnBlackList",
                new int[0], "List of dimensions to NEVER spawn Stacy's Wolves in.").getIntList()) {
            RuntimeConfiguration.dimensionSpawnBlackList.add(dimension);
        }
		RuntimeConfiguration.debugEnabled = configuration.getBoolean("debugEnabled", category, false,
				"Display debug information on the F3 screen.");
        RuntimeConfiguration.wolvesAttackAnimals = configuration.getBoolean("wolvesAttackAnimals", category, true,
				"Set to false if you don't want your piggies being devoured by wolves. " +
                        "Will not prevent wolves from defending themselves or their masters.");
        RuntimeConfiguration.lightingEnabled = configuration.getBoolean("lightingEnabled", category, true,
                "Should lighting be done for wolves which have a light level (e.g. torch wolf). " +
                 "Turn off for improved performance.");
        RuntimeConfiguration.randomDropsEnabled = configuration.getBoolean("randomDropsEnabled", category, true,
                "Turn this off to prevent wolves periodically dropping items (e.g. diamond wolves dropping diamonds)");
        RuntimeConfiguration.allowedInPeaceful = configuration.getBoolean("allowedInPeaceful", category, false,
                "Turn this on to prevent untamed wolves despawning in peaceful mode.");
    }

    private static void logLoadedConfiguration(final Configuration configuration) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Loaded configuration [{}]", configuration.toString());
            final Set<String> categoryNames = getTopLevelCategoryNames(configuration);
            for (String categoryName : categoryNames) {
                final ConfigCategory category = configuration.getCategory(categoryName);
                logCategory(category, 0);
            }
        }
    }

    private static Set<String> getTopLevelCategoryNames(Configuration configuration) {
        final Set<String> categoryNames = configuration.getCategoryNames();
        final Set<String> topLevelCategoryNames = new HashSet<>(categoryNames);
        for (String categoryName : categoryNames) {
            if (categoryName.contains("."))
                topLevelCategoryNames.remove(categoryName);
        }
        return topLevelCategoryNames;
    }

    private static void logCategory(final ConfigCategory category, final int level) {
        final String indent = getIndent(level);
        LOGGER.trace("{}Category: [{}] {", indent, category.getName());
        for (ConfigCategory subCategory : category.getChildren()) {
            logCategory(subCategory, level + 1);
        }
        for (Property property : category.getOrderedValues()) {
            LOGGER.trace("{}\t[{}} -> [{}]", indent, property.getName(), property.getString());
        }
        LOGGER.trace("{}}\n", indent);
    }

    private static String getIndent(final int level) {
        StringBuilder builder = new StringBuilder("\t");
        for (int index = 0; index < level; index++)
            builder.append("\t");

        return builder.toString();
    }
}
