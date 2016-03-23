package au.lyrael.needsmoredragons;

import au.lyrael.needsmoredragons.config.ConfigurationEventHandler;
import au.lyrael.needsmoredragons.registry.DragonRegistry;
import au.lyrael.needsmoredragons.utility.MetadataHelper;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static au.lyrael.needsmoredragons.NeedsMoreDragons.*;

@Mod(modid = MOD_ID, name = MOD_NAME, version = VERSION, acceptedMinecraftVersions = ACCEPTED_MC_VERSIONS)
public class NeedsMoreDragons {

    public static final String MOD_ID = "needsmoredragons";
    public static final String MOD_NAME = "Needs Moar Dragons";
    public static final String MC_VERSION = "1.7.10";
    public static final String VERSION  = "1.0-" + MC_VERSION;
    public static final String ACCEPTED_MC_VERSIONS = "[" + MC_VERSION + "]";
    public static final String DESC = "Cool Story Bro..... but it Needs Moar Dragons!";
    public static final String URL = ""; // PUT CURSEFORGE PROJECT URL HERE!
    public static final String CREDITS = "Credits to the MinecraftForge team for making this mod possible!";
    public static final String LOGO_PATH = ""; // PUT LOGO PATH HERE!
    public static final String CLIENT_PROXY_CLASS = "au.lyrael.needsmoredragons.ClientProxy";
    public static final String COMMON_PROXY_CLASS = "au.lyrael.needsmoredragons.ClientProxy";
    public static int modEntityID = 0;

    @Mod.Instance(MOD_ID)
    public static NeedsMoreDragons instance;

    @Mod.Metadata(MOD_ID)
    public static ModMetadata metadata;

    @SidedProxy(clientSide = CLIENT_PROXY_CLASS, serverSide = COMMON_PROXY_CLASS)
    public static CommonProxy proxy;

    public static CreativeTabs CREATIVE_TAB = new CreativeTabNeedsMoreDragons(CreativeTabs.getNextID(), MOD_ID);
    private static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static Configuration configuration;

    public static final DragonRegistry dragonRegistry = new DragonRegistry();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER.log(Level.INFO, "Pre Initialization: Starting...");

        final ConfigurationEventHandler configurationEventHandler = new ConfigurationEventHandler();
        configurationEventHandler.preInit(event);
        FMLCommonHandler.instance().bus().register(configurationEventHandler);
        
        metadata = MetadataHelper.transformMetadata(metadata);

        proxy.preInit(event);

        LOGGER.log(Level.INFO, "Pre Initialization: Complete");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        LOGGER.log(Level.INFO, "Initialization: Starting...");
        proxy.init();
        LOGGER.log(Level.INFO, "Initialization: Complete");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        LOGGER.log(Level.INFO, "Post Initialization: Starting...");

        proxy.postInit(event);

        LOGGER.log(Level.INFO, "Post Initialization: Complete");
    }


}
