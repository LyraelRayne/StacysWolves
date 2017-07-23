package au.lyrael.stacywolves.event;

import au.lyrael.stacywolves.entity.ISpawnable;
import au.lyrael.stacywolves.entity.wolf.EntityWolfBase;
import au.lyrael.stacywolves.entity.wolf.IWolf;
import au.lyrael.stacywolves.registry.WolfType;
import au.lyrael.stacywolves.tileentity.TileEntityWolfsbane;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.WorldEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static au.lyrael.stacywolves.StacyWolves.*;
import static au.lyrael.stacywolves.registry.WolfType.*;
import static cpw.mods.fml.common.eventhandler.Event.Result.*;

public class SpawnEventHandler {

    private static final Logger LOGGER = LogManager.getLogger(MOD_ID + ".spawn");

    private Map<WolfType, Long> lastSpawnOfType = new HashMap<>();

    protected boolean hasThrottlePeriodExpired(WolfType type, World world) {
        final long worldTime = world.getWorldInfo().getWorldTotalTime();
        final long throttlePeriod = type.getThrottlePeriod();
        final Long lastSpawnTime = lastSpawnOfType.get(type);
        if(lastSpawnTime == null && worldTime % throttlePeriod == 0L)
        {
            lastSpawnOfType.put(type, worldTime);
            return true;
        } else if(lastSpawnTime != null){
            final long timeSinceLastSpawn = worldTime - lastSpawnTime;
            if(timeSinceLastSpawn >= throttlePeriod) {
                lastSpawnOfType.put(type, worldTime);
                return true;
            } else if(timeSinceLastSpawn == 0) {
                return true;
            }
        }
        return false;
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onCheckSpawn(LivingSpawnEvent.CheckSpawn event) {
        if (event.entity instanceof EntityLiving) {
            final EntityLiving entity = (EntityLiving) event.entity;
            event.setResult(DEFAULT);

            if (entity instanceof ISpawnable) {

                final ISpawnable spawnable = (ISpawnable) entity;
                if (!spawnable.testSpawnProbability()) {
                    LOGGER.trace("Spawn canceled due to probability: {}", entity);
                    event.setResult(DENY);
                    return;
                } else if (!canSpawnNow(event, spawnable)) {
                    event.setResult(DENY);
                    if (entity instanceof IWolf)
                        LOGGER.trace("Can not spawn now: {}", entity);
                    return;
                } else if (entity instanceof IWolf) {
                    final IWolf wolf = (IWolf) entity;
                    if (hasThrottlePeriodExpired(wolf.getWolfType(), event.world) && entity.getCanSpawnHere()) {
                        if (!isNearWolfsBane(event)) {
                            LOGGER.debug("Can spawn here and now: {}", entity);
                            traceSpawnCaps(event);
                            event.setResult(ALLOW);
                        } else {
                            event.setResult(DENY);
                            LOGGER.debug("Spawn prevented by wolfsbane. Aborted entity: [{}]", entity);
                            return;
                        }
                    } else {
                        event.setResult(DENY);
                        LOGGER.trace("Can spawn now but not here: {}", entity);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onEnderTeleport(EnderTeleportEvent event) {
        if (event.entityLiving instanceof EntityWolfBase) {
            EntityWolfBase wolf = (EntityWolfBase) event.entityLiving;
            if (!wolf.isTamed() && isNearWolfsBane(event)) {
                LOGGER.debug("Denied teleport of [{}] event [{}] due to Wolfsbane.", wolf, event);
                event.setResult(Event.Result.DENY);
                event.setCanceled(true);
                return;
            }
        }
    }

    private boolean isNearWolfsBane(EnderTeleportEvent event) {
        boolean result = false;
        final Set<TileEntityWolfsbane> wolfsBanes = WOLFSBANE_REGISTRY.getWolfsbanesForWorld(event.entity.worldObj);
        for (TileEntityWolfsbane wolfsBane : wolfsBanes) {
            if (wolfsBane.isWithinRange(event.targetX, event.targetY, event.targetZ)) {
                result = true;
                LOGGER.debug("entity [{}] attempted to teleport near wolfsbane [{}]", event.entity, wolfsBane);
                break;
            }
        }
        return result;
    }

    private boolean isNearWolfsBane(LivingSpawnEvent.CheckSpawn event) {
        boolean result = false;
        final Set<TileEntityWolfsbane> wolfsBanes = WOLFSBANE_REGISTRY.getWolfsbanesForWorld(event.world);
        for (TileEntityWolfsbane wolfsBane : wolfsBanes) {
            if (wolfsBane.isWithinRange(event.x, event.y, event.z)) {
                result = true;
                LOGGER.debug("entity [{}] attempted to spawn near wolfsbane [{}]", event.entity, wolfsBane);
                break;
            }
        }
        return result;
    }

    protected void traceSpawnCaps(LivingSpawnEvent.CheckSpawn event) {
        traceSpawnCap(NORMAL.creatureType(), event.world);
        traceSpawnCap(SUBTERRANEAN.creatureType(), event.world);
        traceSpawnCap(MOB.creatureType(), event.world);
    }

    protected void traceSpawnCap(EnumCreatureType creatureType, World world) {
        if(LOGGER.isTraceEnabled())
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
                    if (!event.list.contains(wolfSpawn))
                        event.list.add(wolfSpawn);
                }
                if (wolfType != MOB && event.list != null && !event.list.isEmpty())
                    LOGGER.trace("Added [{}] wolves to spawn list at [{},{}] with biome [{}]: [{}]", event.type.name(), event.x, event.y, biome.biomeName, event.list);
            }
        }
    }

    protected boolean canSpawnNow(LivingSpawnEvent.CheckSpawn event, ISpawnable entity) {
        return entity.canSpawnNow(event.world, event.x, event.y, event.z);
    }
}
