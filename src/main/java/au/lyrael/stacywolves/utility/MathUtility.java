package au.lyrael.stacywolves.utility;

import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class MathUtility {

	public static float rad(float angle) {
		return angle * (float) Math.PI / 180;
	}

	public static Vec3 getFacing(float rotationYaw, float rotationPitch) {
		float vx = -MathHelper.sin(rad(rotationYaw)) * MathHelper.cos(rad(rotationPitch));
		float vz = MathHelper.cos(rad(rotationYaw)) * MathHelper.cos(rad(rotationPitch));
		float vy = -MathHelper.sin(rad(rotationPitch));
		return Vec3.createVectorHelper(vx, vy, vz);
	}

	public static Vec3 scalarMultiply(double scalar, Vec3 target) {
		return Vec3.createVectorHelper(target.xCoord * scalar, target.yCoord * scalar, target.zCoord * scalar);
	}

	public static Vec3 asVec3(ChunkCoordinates in) {
		return Vec3.createVectorHelper(in.posX, in.posY, in.posZ);
	}
}
