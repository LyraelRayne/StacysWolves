package au.lyrael.stacywolves.entity.wolf;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.client.render.IRenderableWolf;
import au.lyrael.stacywolves.registry.ItemRegistry;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@WolfMetadata(name = "EntityPrismarineWolf", primaryColour = 0x42689B, secondaryColour = 0x68516F,
        spawns = {})
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
