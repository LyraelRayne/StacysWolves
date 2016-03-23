package au.lyrael.needsmoredragons.config;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

import static au.lyrael.needsmoredragons.NeedsMoreDragons.MOD_ID;
import static au.lyrael.needsmoredragons.NeedsMoreDragons.MOD_NAME;

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
