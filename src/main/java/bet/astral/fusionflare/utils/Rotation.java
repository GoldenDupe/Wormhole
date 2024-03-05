package bet.astral.fusionflare.utils;


import org.bukkit.util.Vector;

import java.util.Objects;

public final class Rotation {
	private float pitch;
	private float yaw;
	private float roll;

	public Rotation(float yaw, float pitch, float roll) {
		this.pitch = pitch;
		this.yaw = yaw;
		this.roll = roll;
		checkAxel();
	}

	public float pitch() {
		return pitch;
	}

	public float yaw() {
		return yaw;
	}

	public float roll() {
		return roll;
	}

	public Rotation add(float yaw, float pitch, float roll){
		this.pitch+=pitch;
		this.yaw+= yaw;
		this.roll+= roll;
		checkAxel();
		return this;
	}


	private void checkAxel() {
//		this.pitch = (this.pitch % 360 + 360) % 360;
//		this.yaw = (this.yaw % 360 + 360) % 360;
//		this.roll = (this.roll % 360 + 360) % 360;
	}


	public Vector toVector(){
		return new Vector(yaw, pitch, roll);
	}


	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (Rotation) obj;
		return Float.floatToIntBits(this.yaw) == Float.floatToIntBits(that.yaw) &&
				Float.floatToIntBits(this.pitch) == Float.floatToIntBits(that.pitch) &&
				Float.floatToIntBits(this.roll) == Float.floatToIntBits(that.roll);
	}

	@Override
	public int hashCode() {
		return Objects.hash(pitch, yaw);
	}

}
