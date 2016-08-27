package au.lyrael.stacywolves.entity.wolf;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.annotation.WolfSpawn;
import au.lyrael.stacywolves.annotation.WolfSpawnBiome;
import au.lyrael.stacywolves.client.render.IRenderableWolf;
import au.lyrael.stacywolves.integration.EtFuturumHolder;
import au.lyrael.stacywolves.registry.ItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import static au.lyrael.stacywolves.registry.WolfType.WATER;

@WolfMetadata(name = "EntityPrismarineWolf", primaryColour = 0x42689B, secondaryColour = 0x68516F, type = WATER,
        spawns = {
                @WolfSpawn(spawnBiomes = {
                        @WolfSpawnBiome(specificBiomes = "Deep Ocean"),
                }, probability = 7, min = 1, max = 6),
        })
public class EntityPrismarineWolf extends EntityWolfBase implements IRenderableWolf {

    public EntityPrismarineWolf(World worldObj) {
        super(worldObj);
        addLikedItem(ItemRegistry.getWolfFood("prismarine_bone"));
    }

    @Override
    public boolean isPushedByWater() {
        return false;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
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

    @Override
    public boolean canSpawnNow(World world, float x, float y, float z) {
        return true;
    }

    @Override
    public boolean getCanSpawnHere() {
        // Naming this variable "down" will cause the vector to have some weird value. No, really. It's insane.
        final Vec3 downwards = Vec3.createVectorHelper(0D, -5.0D, 0D);
        final Vec3 up = Vec3.createVectorHelper(0D, 5D, 0D);
        return this.worldObj.checkNoEntityCollision(this.boundingBox)
                && isBlockInDirectionPrismarine(downwards)
                && isBlockInDirectionPrismarine(up);
    }

    protected boolean isBlockInDirectionPrismarine(Vec3 direction) {
        final Block prismarine_block = EtFuturumHolder.prismarine_block;
        if (prismarine_block != null) {
            final Block blockBelow = getBlockInDirection(direction);
            return blockBelow == prismarine_block;
        }
        return false;
    }

    private Block getBlockInDirection(Vec3 direction) {
        final Vec3 location = Vec3.createVectorHelper(posX, posY, posZ);
        final Vec3 target = location.addVector(direction.xCoord, direction.yCoord, direction.zCoord);
        final MovingObjectPosition blockPosition = getWorldObj().rayTraceBlocks(location, target);
        return blockPosition != null
                ? getWorldObj().getBlock(blockPosition.blockX, blockPosition.blockY, blockPosition.blockZ)
                : null;
    }

    @Override
    protected void doWolfShaking() {

    }

    @Override
    public boolean handleWaterMovement() {
        if (!shouldSwimToSurface()) {
            this.setIsInWater(false);
            return false;
        } else
            return super.handleWaterMovement();
    }

    @Override
    public boolean shouldSwimToSurface() {
        final Block blockInDirection = getBlockInDirection(Vec3.createVectorHelper(0D, 30D, 0D));
        return blockInDirection == null;
    }

    @Override
    public boolean normallyAvoidsWater() {
        return false;
    }

    @Override
    public boolean alwaysAvoidsWater() {
        return false;
    }
}
