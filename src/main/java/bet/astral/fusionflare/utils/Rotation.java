package bet.astral.fusionflare.utils;


import org.bukkit.util.Vector;

import java.util.Objects;

public final class Rotation {
	private float pitch;
	private float yaw;
	private float roll;

	public Rotation(float pitch, float yaw, float roll) {
		this.pitch = pitch;
		this.yaw = yaw;
		this.roll = roll;
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

	public void add(float pitch, float yaw, float roll){
		this.pitch+=pitch;
		this.yaw+= yaw;
	}

	public Vector toVector(){
		return new Vector(pitch, yaw, roll);
	}


	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (Rotation) obj;
		return Float.floatToIntBits(this.pitch) == Float.floatToIntBits(that.pitch) &&
				Float.floatToIntBits(this.yaw) == Float.floatToIntBits(that.yaw);
	}

	@Override
	public int hashCode() {
		return Objects.hash(pitch, yaw);
	}

}
