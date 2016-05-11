package au.lyrael.stacywolves.entity;

import au.lyrael.stacywolves.entity.wolf.EntityWolfBase;
import au.lyrael.stacywolves.registry.ItemRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Facing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import static au.lyrael.stacywolves.utility.WolfTransporterUtility.*;
import static au.lyrael.stacywolves.utility.WorldHelper.spawnParticle;

public class EntityWolfTransporter extends EntityThrowable {

    public static final String ENTITY_WOLF_TRANSPORTER = "EntityWolfTransporter";

    int count = 0;
    public EntityPlayer tosser;

    public EntityWolfTransporter(World par1World) {
        super(par1World);
        final ItemStack container = new ItemStack(ItemRegistry.wolf_transporter, 1);
        dataWatcher.addObject(2, container);
    }

    public EntityWolfTransporter(World world, EntityPlayer tosser, ItemStack container) {
        super(world, tosser);
        this.tosser = tosser;
        dataWatcher.addObject(2, container);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (worldObj.isRemote) {
            if (count == 2) {
                spawnParticle(worldObj, "portal", rand.nextInt(8) + 2, 1.0, posX, posY, posZ);
                count = 0;
            } else {
                count++;
            }
        }
    }

    public ItemStack getWolfContainer() {
        return dataWatcher.getWatchableObjectItemStack(2);
    }

    public boolean storeWolf(ItemStack itemstack, EntityWolfBase wolf) {
        if (wolf.worldObj.isRemote) {
            return false;
        }
        if (containsWolf(itemstack)) {
            return false;
        } else if (wolf.getOwner() == null || isOwnedByTosser(wolf)) {
            NBTTagCompound itemNbt = new NBTTagCompound();
            NBTTagCompound entityNbt = new NBTTagCompound();
            wolf.writeToNBT(entityNbt);

            // Unleash the wolf!
            entityNbt.removeTag("Leashed");
            entityNbt.removeTag("Leash");

            entityNbt.setString(WOLF_TYPE_TAG, (String) EntityList.classToStringMapping.get(wolf.getClass()));
            itemNbt.setTag(CAPTURED_ENTITY_DETAILS_TAG, entityNbt);
            itemstack.setTagCompound(itemNbt);
            this.worldObj.playSoundEffect(wolf.posX, wolf.posY, wolf.posZ, "mob.endermen.portal", 1.0F, 1.0F);

            wolf.setDead();
            return true;
        } else {
            return false;
        }
    }

    protected boolean isOwnedByTosser(EntityWolfBase wolf) {
        return wolf.getOwner().getUniqueID() == getTosser().getUniqueID();
    }

    private void dropItem(ItemStack stack) {
        if (!worldObj.isRemote && stack != null) {
            EntityItem itemDrop = new EntityItem(worldObj, posX, posY, posZ, stack);
            itemDrop.delayBeforeCanPickup = 40;
            worldObj.spawnEntityInWorld(itemDrop);
            setDead();
        }
    }


    /**
     * Called when this EntityThrowable hits a block or entity.
     */
    @Override
    protected void onImpact(MovingObjectPosition impactPoint) {
        if (worldObj.isRemote) {
            return;
        }

        ItemStack container = getWolfContainer();
        if (container != null) {
            switch (impactPoint.typeOfHit) {
                case BLOCK:
                    onImpactBlock(container, impactPoint);
                    break;
                case ENTITY:
                    onImpactEntity(container, impactPoint);
                    break;
                case MISS:
                    dropItem(container);
                    break;
            }
        }
    }

    private void onImpactEntity(ItemStack container, MovingObjectPosition impactPoint) {
        final Entity entityHit = impactPoint.entityHit;
        final boolean hasWolf = containsWolf(container);

        if (entityHit instanceof EntityWolfBase && !hasWolf) {
            if (storeWolf(container, (EntityWolfBase) entityHit)) {
                dropItem(container);
            } else {
                // Drop a fresh stack in case something was partially written to the container.
                dropItem(new ItemStack(ItemRegistry.wolf_transporter));
            }
        } else if (hasWolf) {
            final EntityWolfBase spawnedWolf = spawnWolfOrDropItemAt(container,
                    MathHelper.floor_double(entityHit.posX),
                    MathHelper.floor_double(entityHit.posY),
                    MathHelper.floor_double(entityHit.posZ));
            setAttackTargetIfAppropriate(spawnedWolf, entityHit);
        } else {
            dropItem(container);
        }
    }

    private void setAttackTargetIfAppropriate(EntityWolfBase spawnedWolf, Entity entityHit) {
        if (spawnedWolf != null && entityHit instanceof EntityLivingBase) {
            boolean shouldAttack = true;

            if (entityHit instanceof EntityTameable) {
                final EntityLivingBase spawnedWolfOwner = spawnedWolf.getOwner();
                final EntityTameable tameableHit = (EntityTameable) entityHit;
                final EntityLivingBase tameableHitOwner = tameableHit.getOwner();
                if (tameableHit.isTamed() && tameableHitOwner == spawnedWolfOwner || tameableHitOwner == tosser)
                    shouldAttack = false;
            } else {
                if (entityHit instanceof EntityPlayer)
                    shouldAttack = false;
            }
            if (shouldAttack)
                spawnedWolf.setAttackTarget((EntityLivingBase) entityHit);
        }
    }

    private void onImpactBlock(ItemStack container, MovingObjectPosition impactPoint) {
        if (!containsWolf(container)) {
            dropItem(container);
        } else {
            final int sideHit = impactPoint.sideHit;
            spawnWolfOrDropItemAt(container,
                    impactPoint.blockX + Facing.offsetsXForSide[sideHit],
                    impactPoint.blockY + Facing.offsetsYForSide[sideHit],
                    impactPoint.blockZ + Facing.offsetsZForSide[sideHit]);

        }
    }

    private EntityWolfBase spawnWolfOrDropItemAt(ItemStack container, int x, int y, int z) {
        final EntityWolfBase spawnedWolf = loadWolf(container, worldObj);
        if (spawnedWolf != null) {
            spawnedWolf.setPosition(x, y, z);
            worldObj.spawnEntityInWorld(spawnedWolf);
            spawnedWolf.playLivingSound();
            this.worldObj.playSoundEffect(x, y, z, "mob.endermen.portal", 1.0F, 1.0F);
            setDead();
        } else {
            dropItem(container);
        }
        return spawnedWolf;
    }

    public EntityPlayer getTosser() {
        return tosser;
    }

    public int getTextureIndex() {
        if (containsWolf(getWolfContainer())) {
            return 1;
        } else {
            return 0;
        }
    }
}
