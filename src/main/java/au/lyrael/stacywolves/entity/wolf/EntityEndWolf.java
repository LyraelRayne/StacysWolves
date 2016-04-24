package au.lyrael.stacywolves.entity.wolf;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.annotation.WolfSpawn;
import au.lyrael.stacywolves.client.render.IRenderableWolf;
import au.lyrael.stacywolves.registry.ItemRegistry;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import static net.minecraftforge.common.BiomeDictionary.Type.END;

@WolfMetadata(name = "EntityEndWolf", primaryColour = 0xF9F9C5, secondaryColour = 0xC3BD89,
        spawns = {
                @WolfSpawn(biomeTypes = END, probability = 5, min = 1, max = 2),
        })
public class EntityEndWolf extends EntityWolfBase implements IRenderableWolf {

    public EntityEndWolf(World worldObj) {
        super(worldObj);
        addLikedItem(ItemRegistry.getWolfFood("end_bone"));
        this.addEdibleItem(new ItemStack(Items.beef));
        this.addEdibleItem(new ItemStack(Items.chicken));
    }

    @Override
    public EntityWolfBase createChild(EntityAgeable parent) {
        EntityWolfBase child = new EntityEndWolf(this.worldObj);
        return createChild(parent, child);
    }

    @Override
    public String getTextureFolderName() {
        return "end";
    }
}
