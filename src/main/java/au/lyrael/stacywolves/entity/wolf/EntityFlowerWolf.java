package au.lyrael.stacywolves.entity.wolf;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.annotation.WolfSpawn;
import au.lyrael.stacywolves.annotation.WolfSpawnBiome;
import au.lyrael.stacywolves.client.render.IRenderableWolf;
import au.lyrael.stacywolves.registry.ItemRegistry;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.world.World;

import static au.lyrael.stacywolves.entity.SpawnWeights.SPAWN_WEIGHT_RARE;

@WolfMetadata(name = "EntityFlowerWolf", primaryColour = 0x0E5C00, secondaryColour = 0xB95E9A,
        spawns = {
                @WolfSpawn(spawnBiomes = {
                        @WolfSpawnBiome(specificBiomes = {"Flower Forest", "Plains", "Sunflower Plains"}),
                }, weight = SPAWN_WEIGHT_RARE, min = 1, max = 4),
        })
public class EntityFlowerWolf extends EntityWolfBase implements IRenderableWolf {

    public EntityFlowerWolf(World worldObj) {
        super(worldObj);
        addLikedItem(ItemRegistry.getWolfFood("flower_bone"));
    }

    @Override
    public EntityWolfBase createChild(EntityAgeable parent) {
        EntityWolfBase child = new EntityFlowerWolf(this.worldObj);
        return createChild(parent, child);
    }

    @Override
    public String getTextureFolderName() {
        return "flower";
    }
}
