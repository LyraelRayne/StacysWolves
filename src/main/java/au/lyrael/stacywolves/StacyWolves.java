package au.lyrael.stacywolves;

import au.lyrael.stacywolves.config.ConfigurationEventHandler;
import au.lyrael.stacywolves.event.MapGenEventHandler;
import au.lyrael.stacywolves.event.RenderGameOverlayEventHandler;
import au.lyrael.stacywolves.event.SpawnEventHandler;
import au.lyrael.stacywolves.event.WorldEventHandler;
import au.lyrael.stacywolves.registry.WolfRegistry;
import au.lyrael.stacywolves.registry.WolfsbaneRegistry;
import au.lyrael.stacywolves.utility.MetadataHelper;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static au.lyrael.stacywolves.StacyWolves.*;

@Mod(modid = MOD_ID, name = MOD_NAME, version = VERSION, acceptedMinecraftVersions = ACCEPTED_MC_VERSIONS)
public class StacyWolves {

    public static final String MOD_ID = "stacywolves";
    public static final String MOD_NAME = "Stacy's Wolves Mod";
    public static final String MC_VERSION = "1.7.10";
    public static final String VERSION = "1.1.2a-" + MC_VERSION;
    public static final String ACCEPTED_MC_VERSIONS = "[" + MC_VERSION + "]";
    public static final String DESC = "Cool Story Bro..... but it Needs Moar Wolves!";
    public static final String URL = "http://mods.curse.com/mc-mods/minecraft/244308-stacys-wolves";
    public static final String CREDITS = "Concept by StacyPlays. Code by LyraelRayne and AKTheKnight. Artwork by Jaspanda and Nathan_Oneday of Walschaerts Build Team.";
    public static final String LOGO_PATH = ""; // PUT LOGO PATH HERE!
    public static final String CLIENT_PROXY_CLASS = "au.lyrael.stacywolves.ClientProxy";
    public static final String COMMON_PROXY_CLASS = "au.lyrael.stacywolves.CommonProxy";

    /**
     * Make sure these are loaded in the same order every time so that clients and servers match!
     **/
    public static int LAST_MOD_ENTITY_ID = 0;

    @Mod.Instance(MOD_ID)
    public static StacyWolves INSTANCE;

    @Mod.Metadata(MOD_ID)
    public static ModMetadata MOD_METADATA;

    @SidedProxy(clientSide = CLIENT_PROXY_CLASS, serverSide = COMMON_PROXY_CLASS)
    public static CommonProxy proxy;

    public static CreativeTabs CREATIVE_TAB = new CreativeTabStacyWolves(CreativeTabs.getNextID(), MOD_ID);
    private static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static Configuration configuration;

    public static final WolfRegistry WOLF_REGISTRY = new WolfRegistry();
    public static final WolfsbaneRegistry WOLFSBANE_REGISTRY = new WolfsbaneRegistry();
    public static final RenderGameOverlayEventHandler DEBUG_HANDLER = new RenderGameOverlayEventHandler();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER.log(Level.INFO, "Pre Initialization: Starting...");

        final ConfigurationEventHandler configurationEventHandler = new ConfigurationEventHandler();
        configurationEventHandler.preInit(event);
        FMLCommonHandler.instance().bus().register(configurationEventHandler);


        MOD_METADATA = MetadataHelper.transformMetadata(MOD_METADATA);

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
        MinecraftForge.EVENT_BUS.register(new SpawnEventHandler());
        MinecraftForge.EVENT_BUS.register(new WorldEventHandler());
        MinecraftForge.EVENT_BUS.register(DEBUG_HANDLER);
        MinecraftForge.TERRAIN_GEN_BUS.register(new MapGenEventHandler());

        LOGGER.log(Level.INFO, "Post Initialization: Complete");
    }
}
