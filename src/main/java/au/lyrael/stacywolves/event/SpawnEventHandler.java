package au.lyrael.stacywolves.event;

import au.lyrael.stacywolves.entity.ISpawnable;
import au.lyrael.stacywolves.entity.wolf.IWolf;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static au.lyrael.stacywolves.StacyWolves.MOD_ID;
import static au.lyrael.stacywolves.StacyWolves.WOLF_REGISTRY;
import static cpw.mods.fml.common.eventhandler.Event.Result.DENY;

public class SpawnEventHandler {

    private static final Logger LOGGER = LogManager.getLogger(MOD_ID + ".spawn");


    @SubscribeEvent
    public void onCheckSpawn(LivingSpawnEvent.CheckSpawn event) {
        final Entity entity = event.entity;

        if (entity instanceof ISpawnable) {
            if (!((ISpawnable) entity).canSpawnNow(event.world, event.x, event.y, event.z)) {
                event.setResult(DENY);
                if (entity instanceof IWolf)
                    LOGGER.trace("Denied Spawn for {}", WOLF_REGISTRY.getEntityNameFor((IWolf) entity));
                return;
            } else {
                if (entity instanceof IWolf)
                    LOGGER.debug("Spawned {}", entity);
            }
        }
    }
}
