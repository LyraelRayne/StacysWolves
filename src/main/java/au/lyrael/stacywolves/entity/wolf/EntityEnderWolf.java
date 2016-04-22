package au.lyrael.stacywolves.entity.wolf;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.annotation.WolfSpawn;
import au.lyrael.stacywolves.client.render.IRenderableWolf;
import au.lyrael.stacywolves.registry.ItemRegistry;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import static net.minecraftforge.common.BiomeDictionary.Type.HILLS;
import static net.minecraftforge.common.BiomeDictionary.Type.PLAINS;

@WolfMetadata(name = "EntityEnderWolf", primaryColour = 0x000000, secondaryColour = 0xCC00FA,
        spawns = {
                @WolfSpawn(biomeTypes = PLAINS, probability = 20, min = 1, max = 1),
                @WolfSpawn(biomeTypes = HILLS, probability = 5, min = 1, max = 2),
        })
public class EntityEnderWolf extends EntityWolfBase implements IRenderableWolf {

    public EntityEnderWolf(World worldObj) {
        super(worldObj);
        addLikedItem(ItemRegistry.getWolfFood("ender_bone"));
        this.addEdibleItem(new ItemStack(Items.beef));
        this.addEdibleItem(new ItemStack(Items.chicken));
    }

    @Override
    public float getHealAmount(ItemStack itemstack) {
        if (canEat(itemstack))
            return 2F;
        else
            return 0F;
    }

    @Override
    public EntityWolfBase createChild(EntityAgeable parent) {
        EntityWolfBase child = new EntityEnderWolf(this.worldObj);
        String s = this.func_152113_b();

        if (s != null && s.trim().length() > 0) {
            child.func_152115_b(s);
            child.setTamed(true);
        }

        return child;
    }

    @Override
    public String getTextureFolderName() {
        return "ender";
    }

    @Override
    public void onLivingUpdate() {
        if (!this.worldObj.isRemote) {
            if (this.isWet()) {
                this.attackEntityFrom(DamageSource.drown, 1.0F);
            }
        } else {
            for (int loop = 0; loop < 2; ++loop)
            {
                this.worldObj.spawnParticle("portal",
                        this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width,
                        this.posY + this.rand.nextDouble() * (double)this.height - 0.25D,
                        this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width,
                        (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D);
            }

        }

        super.onLivingUpdate();
    }

}
