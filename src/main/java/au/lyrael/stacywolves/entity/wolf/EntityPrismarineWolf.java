package au.lyrael.stacywolves.entity.wolf;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.annotation.WolfSpawn;
import au.lyrael.stacywolves.annotation.WolfSpawnBiome;
import au.lyrael.stacywolves.client.render.IRenderableWolf;
import au.lyrael.stacywolves.registry.ItemRegistry;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.world.World;

import static au.lyrael.stacywolves.entity.SpawnWeights.SPAWN_WEIGHT_RARE;
import static au.lyrael.stacywolves.registry.WolfType.WATER;

@WolfMetadata(name = "EntityPrismarineWolf", primaryColour = 0x42689B, secondaryColour = 0x68516F, type = WATER,
        spawns = {
                @WolfSpawn(spawnBiomes = {
                        @WolfSpawnBiome(specificBiomes = "Deep Ocean"),
                }, weight = SPAWN_WEIGHT_RARE, min = 1, max = 6),
        })
public class EntityPrismarineWolf extends EntityPrismarineWolfBase implements IRenderableWolf {

    public EntityPrismarineWolf(World worldObj) {
        super(worldObj);
        addLikedItem(ItemRegistry.getWolfFood("prismarine_bone"));
    }

    @Override
    public EntityWolfBase createChild(EntityAgeable parent) {
        EntityWolfBase child = new EntityPrismarineWolf(this.worldObj);
        return createChild(parent, child);
    }

    @Override
    public String getTextureFolderName() {
        return "prismarine";
    }
}
