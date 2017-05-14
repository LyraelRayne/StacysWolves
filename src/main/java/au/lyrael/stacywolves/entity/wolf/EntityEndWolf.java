package au.lyrael.stacywolves.entity.wolf;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.annotation.WolfSpawn;
import au.lyrael.stacywolves.annotation.WolfSpawnBiome;
import au.lyrael.stacywolves.client.render.IRenderableWolf;
import au.lyrael.stacywolves.registry.ItemRegistry;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.world.World;

import static au.lyrael.stacywolves.registry.WolfType.MOB;
import static net.minecraftforge.common.BiomeDictionary.Type.END;
import static net.minecraftforge.common.BiomeDictionary.Type.NETHER;

@WolfMetadata(name = "EntityEndWolf", primaryColour = 0xF9F9C5, secondaryColour = 0xC3BD89, type = MOB,
        spawns = {
                @WolfSpawn(spawnBiomes = {
                        @WolfSpawnBiome(requireBiomeTypes = {END}, excludeBiomeTypes = {NETHER}, excludeBiomeNames = "Eldritch"),
                }, weight = 10, min = 1, max = 2),
        })
public class EntityEndWolf extends EntityWolfBase implements IRenderableWolf {

    public EntityEndWolf(World worldObj) {
        super(worldObj);
        addLikedItem(ItemRegistry.getWolfFood("end_bone"));
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

    @Override
    public boolean canSpawnNow(World world, float x, float y, float z) {
        return true;
    }

    @Override
    protected boolean isStandingOnSuitableFloor() {
        return true;
    }
}
