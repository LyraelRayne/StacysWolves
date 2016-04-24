package au.lyrael.stacywolves.entity.wolf;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.annotation.WolfSpawn;
import au.lyrael.stacywolves.client.render.IRenderableWolf;
import au.lyrael.stacywolves.registry.ItemRegistry;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import static net.minecraft.entity.EnumCreatureType.creature;
import static net.minecraftforge.common.BiomeDictionary.Type.*;

@WolfMetadata(name = "EntityBirchWolf", primaryColour = 0x2F332C, secondaryColour = 0xEEEEE9,
        spawns = {
                @WolfSpawn(biomeTypes = {FOREST}, biomeTypeBlacklist = {CONIFEROUS, DENSE}, probability = 7, min = 1, max = 2),
        })
public class EntityBirchWolf extends EntityWolfBase implements IRenderableWolf {

    public EntityBirchWolf(World worldObj) {
        super(worldObj);
        addLikedItem(ItemRegistry.getWolfFood("birch_bone"));
        this.addEdibleItem(new ItemStack(Items.beef));
        this.addEdibleItem(new ItemStack(Items.chicken));
    }

    @Override
    public EntityWolfBase createChild(EntityAgeable parent) {
        EntityWolfBase child = new EntityBirchWolf(this.worldObj);
        return createChild(parent, child);
    }

    @Override
    public String getTextureFolderName() {
        return "birch";
    }

}
