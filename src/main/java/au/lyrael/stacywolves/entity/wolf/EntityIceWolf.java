package au.lyrael.stacywolves.entity.wolf;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.annotation.WolfSpawn;
import au.lyrael.stacywolves.client.render.IRenderableWolf;
import au.lyrael.stacywolves.registry.ItemRegistry;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary;

import static net.minecraftforge.common.BiomeDictionary.Type.COLD;
import static net.minecraftforge.common.BiomeDictionary.Type.END;

@WolfMetadata(name = "EntityIceWolf", primaryColour = 0xEDFEFE, secondaryColour = 0x9EBAE7,
        spawns = {
                @WolfSpawn(biomeTypes = {COLD}, biomeTypeBlacklist = {END}, probability = 5, min = 1, max = 4),
        })
public class EntityIceWolf extends EntityWolfBase implements IRenderableWolf {

    public EntityIceWolf(World worldObj) {
        super(worldObj);
        addLikedItem(ItemRegistry.getWolfFood("ice_bone"));
        this.addEdibleItem(new ItemStack(Items.beef));
        this.addEdibleItem(new ItemStack(Items.chicken));
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
}
