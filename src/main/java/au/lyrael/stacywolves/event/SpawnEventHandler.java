package au.lyrael.stacywolves.event;

import au.lyrael.stacywolves.entity.ISpawnable;
import au.lyrael.stacywolves.entity.wolf.IWolf;
import au.lyrael.stacywolves.registry.WolfType;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.WorldEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static au.lyrael.stacywolves.StacyWolves.MOD_ID;
import static au.lyrael.stacywolves.StacyWolves.WOLF_REGISTRY;
import static au.lyrael.stacywolves.registry.WolfType.MOB;
import static au.lyrael.stacywolves.registry.WolfType.NORMAL;
import static au.lyrael.stacywolves.registry.WolfType.ORE;
import static cpw.mods.fml.common.eventhandler.Event.Result.DENY;

public class SpawnEventHandler {

    private static final Logger LOGGER = LogManager.getLogger(MOD_ID + ".spawn");


    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onCheckSpawn(LivingSpawnEvent.CheckSpawn event) {
        if (event.entity instanceof EntityLiving) {
            final EntityLiving entity = (EntityLiving) event.entity;

            if (entity instanceof ISpawnable) {
                if (!canSpawn(event, (ISpawnable) entity)) {
                    event.setResult(DENY);
                    if (entity instanceof IWolf)
                        LOGGER.trace("Can not spawn now: {}", entity);
                    return;
                } else {
                    if (entity instanceof IWolf) {
                        if (entity.getCanSpawnHere()) {
                            LOGGER.debug("Can spawn here and now: {}", entity);
                            traceSpawnCaps(event);
                        } else {
                            LOGGER.trace("Can spawn now but not here: {}", entity);
                        }
                    }
                }
            }
        }
    }

    protected void traceSpawnCaps(LivingSpawnEvent.CheckSpawn event) {
        traceSpawnCap(NORMAL.creatureType(), event.world);
        traceSpawnCap(ORE.creatureType(), event.world);
        traceSpawnCap(MOB.creatureType(), event.world);
    }

    protected void traceSpawnCap(EnumCreatureType creatureType, World world) {
        LOGGER.trace("Total spawned [{}] wolf count: {}", creatureType.name(), world.countEntities(creatureType, true));
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onGetSpawnList(WorldEvent.PotentialSpawns event) {
        final WolfType wolfType = WolfType.valueOf(event.type);
        if (wolfType != null) {
            if (WOLF_REGISTRY.getSpawnRegistry().containsKey(wolfType)) {
                final BiomeGenBase biome = event.world.getBiomeGenForCoords(event.x, event.z);
                List<BiomeGenBase.SpawnListEntry> wolfSpawns = WOLF_REGISTRY.getSpawnsFor(biome, wolfType);
                for (BiomeGenBase.SpawnListEntry wolfSpawn : wolfSpawns) {
                    if(!event.list.contains(wolfSpawn))
                        event.list.add(wolfSpawn);
                }
                if(wolfType != MOB)
                    LOGGER.trace("Added [{}] wolves to spawn list for biome [{}]: [{}]", event.type.name(), biome.biomeName, event.list);
            }
        }
    }

    protected boolean canSpawn(LivingSpawnEvent.CheckSpawn event, ISpawnable entity) {
        return entity.canSpawnNow(event.world, event.x, event.y, event.z);
    }
}
