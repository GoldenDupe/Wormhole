package bet.astral.fusionflare.utils;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public final class LocationUtils{
	public static double[] toDoubleArray(Location location){
		return new double[]{location.getX(), location.getY(), location.getZ()};
	}

	public static Location fromDoubleArray(Location from, double[] array) {
		return from.set(array[0], array[1], array[2]);
	}

	public static double[] toDoubleArray(Vector vector){
		return new double[]{vector.getX(), vector.getY(), vector.getZ()};
	}

	public static Vector fromDoubleArray(double[] array) {
		return new Vector(array[0], array[1], array[2]);
	}

	public static Vector getDirectionBetweenLocations(Location Start, Location End) {
		Vector from = Start.toVector();
		Vector to = End.toVector();
		return to.subtract(from);
	}

	public static Rotation combine(Rotation rotation, Rotation rotation2){
		return rotation.add(rotation2.yaw(), rotation2.pitch(), rotation2.pitch());
	}

	public static Vector rotateAroundAxisX(Vector v, double cos, double sin) {
		double y = v.getY() * cos - v.getZ() * sin;
		double z = v.getY() * sin + v.getZ() * cos;
		return v.setY(y).setZ(z);
	}

	public static Vector rotateAroundAxisY(Vector v, double cos, double sin) {
		double x = v.getX() * cos + v.getZ() * sin;
		double z = v.getX() * -sin + v.getZ() * cos;
		return v.setX(x).setZ(z);
	}

	public static Vector rotateAroundAxisZ(Vector v, double cos, double sin) {
		double x = v.getX() * cos - v.getY() * sin;
		double y = v.getX() * sin + v.getY() * cos;
		return v.setX(x).setY(y);
	}
}
