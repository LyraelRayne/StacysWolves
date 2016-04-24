package au.lyrael.stacywolves.entity.wolf;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.annotation.WolfSpawn;
import au.lyrael.stacywolves.client.render.IRenderableWolf;
import au.lyrael.stacywolves.registry.ItemRegistry;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import static net.minecraftforge.common.BiomeDictionary.Type.MUSHROOM;

@WolfMetadata(name = "EntityMushroomWolf", primaryColour = 0xB11917, secondaryColour = 0xD5D5D5,
        spawns = {
                @WolfSpawn(biomeTypes = {MUSHROOM}, probability = 5, min = 1, max = 4),
        })
public class EntityMushroomWolf extends EntityWolfBase implements IRenderableWolf {

    public EntityMushroomWolf(World worldObj) {
        super(worldObj);
        addLikedItem(ItemRegistry.getWolfFood("mushroom_bone"));
        this.addEdibleItem(new ItemStack(Items.beef));
        this.addEdibleItem(new ItemStack(Items.chicken));
    }

    @Override
    public EntityWolfBase createChild(EntityAgeable parent) {
        EntityWolfBase child = new EntityMushroomWolf(this.worldObj);
        return createChild(parent, child);
    }

    @Override
    public String getTextureFolderName() {
        return "mushroom";
    }
}
