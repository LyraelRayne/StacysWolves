package au.lyrael.stacywolves.event;

import au.lyrael.stacywolves.entity.wolf.EntityFireWolf;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.structure.MapGenNetherBridge;
import net.minecraftforge.event.terraingen.InitMapGenEvent;

public class MapGenEventHandler {

    @SubscribeEvent
    public void onMapGenEvent(InitMapGenEvent event) {
        if (event.type == InitMapGenEvent.EventType.NETHER_BRIDGE) {
            final MapGenNetherBridge fortressGen = (MapGenNetherBridge) event.newGen;
            fortressGen.getSpawnList().add(new BiomeGenBase.SpawnListEntry(EntityFireWolf.class, 7, 1, 4));
        }
    }
}
