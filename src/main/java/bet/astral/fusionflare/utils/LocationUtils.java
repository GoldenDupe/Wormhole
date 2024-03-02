package bet.astral.fusionflare.utils;

import org.bukkit.Location;

public final class LocationUtils{
	public static double[] toDoubleArray(Location location){
		return new double[]{location.getX(), location.getY(), location.getZ()};
	}

	public static Location fromDoubleArray(Location from, double[] array) {
		return from.set(array[0], array[1], array[2]);
	}
}
