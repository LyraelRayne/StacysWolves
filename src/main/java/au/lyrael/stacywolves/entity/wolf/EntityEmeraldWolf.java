package au.lyrael.stacywolves.entity.wolf;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.annotation.WolfSpawn;
import au.lyrael.stacywolves.annotation.WolfSpawnBiome;
import au.lyrael.stacywolves.client.render.IRenderableWolf;
import au.lyrael.stacywolves.registry.ItemRegistry;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.world.World;

import static au.lyrael.stacywolves.entity.SpawnWeights.SPAWN_PROBABILITY_SUPER_RARE;
import static au.lyrael.stacywolves.entity.SpawnWeights.SPAWN_WEIGHT_SUPER_RARE;
import static au.lyrael.stacywolves.registry.WolfType.SUBTERRANEAN;
import static net.minecraftforge.common.BiomeDictionary.Type.MOUNTAIN;

@WolfMetadata(name = "EntityEmeraldWolf", primaryColour = 0x7F7F7F, secondaryColour = 0x17DD62, type = SUBTERRANEAN, probability = SPAWN_PROBABILITY_SUPER_RARE,
        spawns = {
                @WolfSpawn(spawnBiomes = {
                        @WolfSpawnBiome(requireBiomeTypes = {MOUNTAIN}),
                }, weight = SPAWN_WEIGHT_SUPER_RARE, min = 1, max = 4),
        })
public class EntityEmeraldWolf extends EntitySubterraneanWolfBase implements IRenderableWolf {

    public EntityEmeraldWolf(World worldObj) {
        super(worldObj);
        addLikedItem(ItemRegistry.getWolfFood("emerald_bone"));
    }

    @Override
    public EntityWolfBase createChild(EntityAgeable parent) {
        EntityWolfBase child = new EntityEmeraldWolf(this.worldObj);
        return createChild(parent, child);
    }

    @Override
    public String getTextureFolderName() {
        return "emerald";
    }

    @Override
    public boolean getCanSpawnHere() {
		return getCanSpawnHere(40);
	}

}
