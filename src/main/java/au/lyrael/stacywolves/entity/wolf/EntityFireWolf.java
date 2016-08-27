package au.lyrael.stacywolves.entity.wolf;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.client.render.IRenderableWolf;
import au.lyrael.stacywolves.registry.ItemRegistry;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@WolfMetadata(name = "EntityFireWolf", primaryColour = 0xF4C923, secondaryColour = 0x6D2C04)
public class EntityFireWolf extends EntityWolfBase implements IRenderableWolf {


    public EntityFireWolf(World worldObj) {
        super(worldObj);
        addLikedItem(ItemRegistry.getWolfFood("fire_bone"));
        this.isImmuneToFire = true;
    }

    @Override
    public EntityWolfBase createChild(EntityAgeable parent) {
        EntityWolfBase child = new EntityFireWolf(this.worldObj);
        return createChild(parent, child);
    }

    @Override
    public void onLivingUpdate() {
        if (!this.worldObj.isRemote) {
            hurtIfWet();
        } else {
            for (int count = 0; count < 2; ++count) {
                this.worldObj.spawnParticle("smoke",
                        this.posX + (this.rand.nextDouble() - 0.5D) * (double) this.width,
                        this.posY + this.rand.nextDouble() * (double) this.height + 0.3,
                        this.posZ + (this.rand.nextDouble() - 0.5D) * (double) this.width * 3, 0.0D, 0.0D, 0.0D);
            }

            for (int count = 0; count < 2; count++) {
                this.worldObj.spawnParticle("flame",
                        this.posX + (this.rand.nextDouble() - 0.5D) * (double) this.width,
                        this.posY + this.rand.nextDouble() * (double) this.height + 0.3,
                        this.posZ + (this.rand.nextDouble() - 0.5D) * (double) this.width, 0.0D, 0.0D, 0.0D);
            }
        }

        super.onLivingUpdate();
    }

    @Override
    public boolean getCanSpawnHere() {
        return isStandingOn(Blocks.nether_brick) && creatureCanSpawnHere();
    }

    @Override
    public String getTextureFolderName() {
        return "fire";
    }

    @Override
    public boolean canSpawnNow(World world, float x, float y, float z) {
        return true;
    }

    @Override
    public boolean alwaysAvoidsWater() {
        return !isTamed();
    }
}
