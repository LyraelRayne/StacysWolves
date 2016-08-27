package au.lyrael.stacywolves.entity.wolf;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.annotation.WolfSpawn;
import au.lyrael.stacywolves.annotation.WolfSpawnBiome;
import au.lyrael.stacywolves.client.render.IRenderableWolf;
import au.lyrael.stacywolves.registry.ItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

import static au.lyrael.stacywolves.registry.WolfType.ORE;
import static au.lyrael.stacywolves.utility.WorldHelper.canSeeTheSky;
import static net.minecraftforge.common.BiomeDictionary.Type.MOUNTAIN;

@WolfMetadata(name = "EntityEmeraldWolf", primaryColour = 0x7F7F7F, secondaryColour = 0x17DD62, type = ORE,
        spawns = {
                @WolfSpawn(spawnBiomes = {
                        @WolfSpawnBiome(requireBiomeTypes = {MOUNTAIN}),
                }, probability = 5, min = 1, max = 4),
        })
public class EntityEmeraldWolf extends EntityWolfBase implements IRenderableWolf {

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
        return posY < 40
                && !canSeeTheSky(getWorldObj(), posX, posY, posZ)
                && isStandingOn(getFloorBlocks().toArray(new Block[0]))
                && isStandingOnSuitableFloor();
    }

    @Override
    public boolean canSpawnNow(World world, float x, float y, float z) {
        return true;
    }

    @Override
    protected List<Block> getFloorBlocks() {
        return ORE_FLOOR_BLOCKS;
    }
}
