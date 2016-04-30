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
import static net.minecraftforge.common.BiomeDictionary.Type.BEACH;
import static net.minecraftforge.common.BiomeDictionary.Type.WASTELAND;

@WolfMetadata(name = "EntityPrismarineWolf", primaryColour = 0x42689B, secondaryColour = 0x68516F,
        spawns = {
        @WolfSpawn(spawnBiomes = {
                @WolfSpawnBiome(specificBiomes = "Deep Ocean"),
        }, probability = 7, min = 1, max = 6),
})
public class EntityPrismarineWolf extends EntityWolfBase implements IRenderableWolf {

    public EntityPrismarineWolf(World worldObj) {
        super(worldObj);
        addLikedItem(ItemRegistry.getWolfFood("prismarine_bone"));
        this.addEdibleItem(new ItemStack(Items.beef));
        this.addEdibleItem(new ItemStack(Items.chicken));
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
