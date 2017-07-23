package au.lyrael.stacywolves.utility;

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
}
