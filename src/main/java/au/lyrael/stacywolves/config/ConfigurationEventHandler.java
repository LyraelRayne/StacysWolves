package au.lyrael.stacywolves.config;

import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

import static au.lyrael.stacywolves.StacyWolves.MOD_ID;
import static au.lyrael.stacywolves.StacyWolves.configuration;

public class ConfigurationEventHandler {

    private ConfigurationLoader loader = new ConfigurationLoader();
    private File configurationDir;
    private File mainConfigurationFile;

    public void preInit(FMLPreInitializationEvent event) {
        configurationDir = event.getModConfigurationDirectory();
        mainConfigurationFile = new File(configurationDir, MOD_ID + "/main.cfg");
        configuration = new Configuration(mainConfigurationFile, true);
        getLoader().loadConfiguration(configuration);
    }

    @SubscribeEvent
    public void onConfigurationChangedEvent(final ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.modID.equalsIgnoreCase(MOD_ID)) {
            getLoader().loadConfiguration(configuration);
        }
    }

    public ConfigurationLoader getLoader() {
        return loader;
    }

    public void setLoader(ConfigurationLoader loader) {
        this.loader = loader;
    }
}
