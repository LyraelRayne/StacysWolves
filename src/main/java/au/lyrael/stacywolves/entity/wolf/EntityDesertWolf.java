package au.lyrael.stacywolves.entity.wolf;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.annotation.WolfSpawn;
import au.lyrael.stacywolves.annotation.WolfSpawnBiome;
import au.lyrael.stacywolves.client.render.IRenderableWolf;
import au.lyrael.stacywolves.registry.ItemRegistry;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import static net.minecraftforge.common.BiomeDictionary.Type.*;

@WolfMetadata(name = "EntityDesertWolf", primaryColour = 0xE0D6A6, secondaryColour = 0xBDB286,
        spawns = {
                @WolfSpawn(spawnBiomes = {
                        @WolfSpawnBiome(requireBiomeTypes = {SANDY, HOT, DRY})
                }, probability = 7, min = 1, max = 3),
        })
public class EntityDesertWolf extends EntityWolfBase implements IRenderableWolf {

    public EntityDesertWolf(World worldObj) {
        super(worldObj);
        addLikedItem(ItemRegistry.getWolfFood("desert_bone"));
        this.addEdibleItem(new ItemStack(Items.beef));
        this.addEdibleItem(new ItemStack(Items.chicken));
    }

    @Override
    public EntityWolfBase createChild(EntityAgeable parent) {
        EntityWolfBase child = new EntityDesertWolf(this.worldObj);
        return createChild(parent, child);
    }

    @Override
    public String getTextureFolderName() {
        return "desert";
    }
}
