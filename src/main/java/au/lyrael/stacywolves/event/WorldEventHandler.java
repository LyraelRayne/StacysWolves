package au.lyrael.stacywolves.event;

import au.lyrael.stacywolves.StacyWolves;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.world.WorldEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static au.lyrael.stacywolves.StacyWolves.MOD_ID;

public class WorldEventHandler {

    private static final Logger LOGGER = LogManager.getLogger(MOD_ID + ".world");

    @SubscribeEvent
    public void onWorldUnloaded(WorldEvent.Unload event) {
        if (!event.world.isRemote) {
            StacyWolves.WOLFSBANE_REGISTRY.clear(event.world);
        }
    }
}
