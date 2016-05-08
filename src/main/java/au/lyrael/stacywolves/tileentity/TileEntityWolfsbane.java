package au.lyrael.stacywolves.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static au.lyrael.stacywolves.StacyWolves.MOD_ID;
import static au.lyrael.stacywolves.StacyWolves.WOLFSBANE_REGISTRY;

public class TileEntityWolfsbane extends TileEntity {
    private static final Logger LOGGER = LogManager.getLogger(MOD_ID + ".wolfsbane");

    private int rangeHorizontal;
    private int rangeUp;
    private int rangeDown;

    private AxisAlignedBB boundingBox;

    public TileEntityWolfsbane()
    {
        this(16, 16, 4);
    }

    public TileEntityWolfsbane(int rangeHorizontal, int rangeUp, int rangeDown)
    {
        super();
        this.rangeHorizontal = rangeHorizontal;
        this.rangeUp = rangeUp;
        this.rangeDown = rangeDown;
    }


    /**
     * Determines if this TileEntity requires update calls.
     *
     * @return True if you want updateEntity() to be called, false if not
     */
    @Override
    public boolean canUpdate() {
        return false;
    }

    /**
     * invalidates a tile entity
     */
    @Override
    public void invalidate() {
        if(isOnServer())
            WOLFSBANE_REGISTRY.removeWolfsbane(this);
        super.invalidate();
    }

    /**
     * validates a tile entity
     */
    @Override
    public void validate() {
        if(isOnServer())
            WOLFSBANE_REGISTRY.addWolfsbane(this);
        super.validate();
    }

    /**
     * Called when the chunk this TileEntity is on is Unloaded.
     */
    @Override
    public void onChunkUnload() {
        if(isOnServer())
            WOLFSBANE_REGISTRY.removeWolfsbane(this);

        super.onChunkUnload();
    }

    protected boolean isOnServer() {
        return !getWorldObj().isRemote;
    }

    public boolean isWithinRange(float targetX, float targetY, float targetZ) {
       return isWithinRange((double)targetX, (double)targetY, (double)targetZ);
    }

    public boolean isWithinRange(double targetX, double targetY, double targetZ) {
        return getBoundingBox().isVecInside(Vec3.createVectorHelper(targetX, targetY, targetZ));
    }

    @Override
    public String toString() {
        return String.format("Wolfsbane(%s,%s,%s) range=(%s,%s,%s)", xCoord,yCoord, zCoord, rangeHorizontal, -rangeDown, rangeUp);
    }

    protected AxisAlignedBB getBoundingBox() {

        // Init here because the X,Y and Z position of the entity are not set in the constructor.

        if(this.boundingBox == null) {
            int neX = xCoord - this.rangeHorizontal;
            int neZ = zCoord - this.rangeHorizontal;
            int swX = xCoord + this.rangeHorizontal;
            int swZ = zCoord + this.rangeHorizontal;
            int botY = yCoord - this.rangeDown;
            int topY = yCoord + this.rangeDown;
            boundingBox = AxisAlignedBB.getBoundingBox(neX, botY, neZ, swX, topY, swZ);
        }
        return boundingBox;
    }
}
