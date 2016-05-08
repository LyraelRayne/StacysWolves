package au.lyrael.stacywolves.registry;

import au.lyrael.stacywolves.tileentity.TileEntityWolfsbane;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WolfsbaneRegistry {

    private Map<World, Set<TileEntityWolfsbane>> loadedWolfsbanes = new HashMap<>();

    public void addWolfsbane(TileEntityWolfsbane toAdd) {
        Set<TileEntityWolfsbane> wolfsbanes = getWolfsbanesForWorld(toAdd.getWorldObj());
        wolfsbanes.add(toAdd);
    }

    public Set<TileEntityWolfsbane> getWolfsbanesForWorld(World world) {
        Set<TileEntityWolfsbane> wolfsbanes = loadedWolfsbanes.get(world);
        if(wolfsbanes == null) {
            wolfsbanes = new HashSet<>();
            loadedWolfsbanes.put(world, wolfsbanes);
        }
        return wolfsbanes;
    }

    public void removeWolfsbane(TileEntityWolfsbane toRemove) {
        Set<TileEntityWolfsbane> wolfsbanes = getWolfsbanesForWorld(toRemove.getWorldObj());
        wolfsbanes.remove(toRemove);
    }

    public void clear(World world) {
        loadedWolfsbanes.remove(world);
    }

    public void clear() {
        loadedWolfsbanes.clear();
    }
}
