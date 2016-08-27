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

import static net.minecraftforge.common.BiomeDictionary.Type.SAVANNA;

@WolfMetadata(name = "EntitySavannahWolf", primaryColour = 0xE4D7B0, secondaryColour = 0xB0B252,
        spawns = {
                @WolfSpawn(spawnBiomes = {
                        @WolfSpawnBiome(requireBiomeTypes = {SAVANNA}),
                }, probability = 7, min = 1, max = 4),
        })
public class EntitySavannahWolf extends EntityWolfBase implements IRenderableWolf {

    public EntitySavannahWolf(World worldObj) {
        super(worldObj);
        addLikedItem(ItemRegistry.getWolfFood("savannah_bone"));
    }

    @Override
    public EntityWolfBase createChild(EntityAgeable parent) {
        EntityWolfBase child = new EntitySavannahWolf(this.worldObj);
        return createChild(parent, child);
    }

    @Override
    public String getTextureFolderName() {
        return "savannah";
    }
}
