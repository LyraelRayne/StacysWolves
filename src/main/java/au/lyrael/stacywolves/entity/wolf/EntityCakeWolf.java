package au.lyrael.stacywolves.entity.wolf;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.client.render.IRenderableWolf;
import au.lyrael.stacywolves.entity.SpawnWeights;
import au.lyrael.stacywolves.registry.ItemRegistry;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.world.World;

@WolfMetadata(name = "EntityCakeWolf", primaryColour = 0xB35922, secondaryColour = 0xE41717, probability = SpawnWeights.SPAWN_PROBABILITY_SUPER_RARE)
public class EntityCakeWolf extends EntityWolfBase implements IRenderableWolf, IWolf {

    public EntityCakeWolf(World worldObj) {
        super(worldObj);
        addLikedItem(ItemRegistry.getWolfFood("cake_bone"));
    }

    @Override
    public boolean getCanSpawnHere() {
        return super.getCanSpawnHere() && scanForVillage(8) != null;
    }

    @Override
    public EntityWolfBase createChild(EntityAgeable parent) {
        EntityWolfBase child = new EntityCakeWolf(this.worldObj);
        return createChild(parent, child);
    }

    @Override
    public String getTextureFolderName() {
        return "cake";
    }
}
