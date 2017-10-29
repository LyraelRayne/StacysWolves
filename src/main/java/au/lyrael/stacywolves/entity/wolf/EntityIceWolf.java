package au.lyrael.stacywolves.entity.wolf;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.annotation.WolfSpawn;
import au.lyrael.stacywolves.annotation.WolfSpawnBiome;
import au.lyrael.stacywolves.client.render.IRenderableWolf;
import au.lyrael.stacywolves.registry.ItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

import static au.lyrael.stacywolves.entity.SpawnWeights.SPAWN_WEIGHT_COMMON;
import static net.minecraftforge.common.BiomeDictionary.Type.COLD;
import static net.minecraftforge.common.BiomeDictionary.Type.SNOWY;

@WolfMetadata(name = "EntityIceWolf", primaryColour = 0xEDFEFE, secondaryColour = 0x9EBAE7,
        spawns = {
                @WolfSpawn(spawnBiomes = {
                        @WolfSpawnBiome(requireBiomeTypes = {COLD, SNOWY}),
                        @WolfSpawnBiome(specificBiomes = {
                                "Extreme Hills",
                                "Extreme Hills+",
                                "Extreme Hills Edge",
                                "Extreme Hills M",
                                "Extreme Hills+ M",
                        }),
                }, weight = SPAWN_WEIGHT_COMMON, min = 1, max = 4),
        })
public class EntityIceWolf extends EntityWolfBase implements IRenderableWolf {

    public EntityIceWolf(World worldObj) {
        super(worldObj);
        addLikedItem(ItemRegistry.getWolfFood("ice_bone"));
    }

    @Override
    public EntityWolfBase createChild(EntityAgeable parent) {
        EntityWolfBase child = new EntityIceWolf(this.worldObj);
        return createChild(parent, child);
    }

    @Override
    public String getTextureFolderName() {
        return "ice";
    }

    @Override
    protected List<Block> getFloorBlocks() {
        return ICE_FLOOR_BLOCKS;
    }
}
