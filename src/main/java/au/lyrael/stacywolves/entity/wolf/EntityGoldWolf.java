package au.lyrael.stacywolves.entity.wolf;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.annotation.WolfSpawn;
import au.lyrael.stacywolves.client.render.IRenderableWolf;
import au.lyrael.stacywolves.registry.ItemRegistry;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import static au.lyrael.stacywolves.utility.WorldHelper.canSeeTheSky;
import static net.minecraftforge.common.BiomeDictionary.Type.*;

@WolfMetadata(name = "EntityGoldWolf", primaryColour = 0x7F7F7F, secondaryColour = 0xF8AF2B,
        spawns = {
                @WolfSpawn(biomeTypes = PLAINS, probability = 2, min = 1, max = 4),
                @WolfSpawn(biomeTypes = FOREST, probability = 2, min = 1, max = 4),
                @WolfSpawn(biomeTypes = HILLS, probability = 2, min = 1, max = 4),
                @WolfSpawn(biomeTypes = SANDY, probability = 2, min = 1, max = 4),
        })
public class EntityGoldWolf extends EntityWolfBase implements IRenderableWolf {

    public EntityGoldWolf(World worldObj) {
        super(worldObj);
        addLikedItem(ItemRegistry.getWolfFood("gold_bone"));
        this.addEdibleItem(new ItemStack(Items.beef));
        this.addEdibleItem(new ItemStack(Items.chicken));
    }

    @Override
    public EntityWolfBase createChild(EntityAgeable parent) {
        EntityWolfBase child = new EntityGoldWolf(this.worldObj);
        return createChild(parent, child);
    }

    @Override
    public String getTextureFolderName() {
        return "gold";
    }

    @Override
    public boolean getCanSpawnHere() {
        return !canSeeTheSky(getWorldObj(), posX, posY, posZ) && this.posY < 30 && creatureCanSpawnHere();
    }
}
