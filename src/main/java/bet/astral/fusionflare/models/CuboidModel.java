package bet.astral.fusionflare.models;

import bet.astral.fusionflare.FusionFlare;
import bet.astral.fusionflare.particles.FFParticle;
import bet.astral.fusionflare.utils.LocationUtils;
import org.bukkit.Location;
import org.bukkit.util.NumberConversions;

public class CuboidModel extends FFModel{
	private double[][][] locations;
	private final double[][] corners;
	private final double betweenEach;
	private boolean calculating = false;

	public CuboidModel(FusionFlare fusionFlare, FFParticle<?> particle, Location location, int timeBetweenTicks, int timeBetweenDraws, double betweenEach, double[] northWestCornerUp, double[] northEastCornerUp, double[] southWestCornerUp, double[] southEastCornerUp, double[] northWestCornerDown, double[] northEastCornerDown, double[] southWestCornerDown, double[] southEastCornerDown) {
		super(fusionFlare, particle, location, timeBetweenTicks, timeBetweenDraws);
		this.corners = new double[8][];
		//
		// DownSouthWest -> UpSouthWest -> UpSouthEast -> DownSouthEast -> DownSouthWest -> DownNorthWest -> DownNorth
		//
		//
		corners[0] = northWestCornerUp;
		corners[1] = northEastCornerUp;
		corners[2] = southWestCornerUp;
		corners[3] = southEastCornerUp;

		corners[4] = northWestCornerDown;
		corners[5] = northEastCornerDown;
		corners[6] = southWestCornerDown;
		corners[7] = southEastCornerDown;
		this.betweenEach = betweenEach;
	}
	protected CuboidModel(FusionFlare fusionFlare, FFParticle<?> particle, Location location, int timeBetweenTicks, int timeBetweenDraws, double betweenEach, double[][] corners) {
		super(fusionFlare, particle, location, timeBetweenTicks, timeBetweenDraws);
		this.corners = corners;
		this.betweenEach = betweenEach;
	}

	private void calculate(){
		if (calculating){
			return;
		}
		double[][][] locations = new double[12][][];
		Location clone = location.clone();
		Location clone2 = location.clone();
		locations[0] = calculate(clone2, clone, add(clone, this.corners[0]), add(clone, this.corners[4])); // NWU->NWD
		locations[1] = calculate(clone2, clone, add(clone, this.corners[1]), add(clone, this.corners[5])); // NEU->NED
		locations[2] = calculate(clone2, clone, add(clone, this.corners[2]), add(clone, this.corners[6])); // SWU->SWD
		locations[3] = calculate(clone2, clone, add(clone, this.corners[3]), add(clone, this.corners[7])); // SEU->SED

		locations[4] = calculate(clone2, clone, add(clone, this.corners[0]), add(clone, this.corners[2])); // NWU->SWU
		locations[5] = calculate(clone2, clone, add(clone, this.corners[0]), add(clone, this.corners[1])); // NWU->NEU

		locations[6] = calculate(clone2, clone, add(clone, this.corners[4]), add(clone, this.corners[6])); // NWD->SWD
		locations[7] = calculate(clone2, clone, add(clone, this.corners[4]), add(clone, this.corners[5])); // NWD->SED
		locations[8] = calculate(clone2, clone, add(clone, this.corners[7]), add(clone, this.corners[6])); // SED->SWD
		locations[9] = calculate(clone2, clone, add(clone, this.corners[7]), add(clone, this.corners[5])); // SED->SED
		locations[10] = calculate(clone2, clone, add(clone, this.corners[3]), add(clone, this.corners[2])); // SEU->SWU
		locations[11] = calculate(clone2, clone, add(clone, this.corners[3]), add(clone, this.corners[1])); // SEU->SEU

//§		locations[0] = calculate(clone2, clone, add(clone, this.corners[0]), add(clone, this.corners[4])); // NWU->NWD
//		locations[1] = calculate(clone2, clone, add(clone, this.corners[0]), add(clone, this.corners[2])); // NWU->SWU
//		locations[2] = calculate(clone2, clone, add(clone, this.corners[0]), add(clone, this.corners[1])); // NWU->NEU
//
//		locations[3] = calculate(clone2, clone, add(clone, this.corners[4]), add(clone, this.corners[6])); // NWD->SWD
//		locations[4] = calculate(clone2, clone, add(clone, this.corners[4]), add(clone, this.corners[5])); // NWD->SED
//		locations[4] = calculate(clone2, clone, add(clone2, this.corners[0]), add(clone2, this.corners[1]));
//		locations[5] = calculate(clone2, clone, add(clone2, this.corners[0]), add(clone2, this.corners[1]));

		this.locations = locations;
	}

	private double[] add(Location location, double[] points){
		return new double[]{location.getX()+points[0], location.getY()+points[1], location.getZ()+points[2]};
	}

	private double[][] calculate(Location from, Location to, double[] first, double[] last){
		double[] beforeFirst = LocationUtils.toDoubleArray(from);
		double[] beforeLast = LocationUtils.toDoubleArray(to);

		LocationUtils.fromDoubleArray(from, first);
		LocationUtils.fromDoubleArray(to, last);

		double[][] locations = null;
		double x = to.getX() - from.getX();
		double y = to.getY() - from.getY();
		double z = to.getZ() - from.getZ();
		double length = Math.sqrt(NumberConversions.square(x) + NumberConversions.square(y) + NumberConversions.square(z));
		x /= length;
		y /= length;
		z /= length;

		try {
			double[][] newLocations = new double[(int) (length / betweenEach) + 12][];
			int j = 0;
			for (double i = 0; i < length + 0.001; i += betweenEach) {
				if (i > length) i = length;
				newLocations[j] = new double[]{from.getX() + x * i, from.getY() + y * i, from.getZ() + z * i};
				j++;
			}

			locations = newLocations;
			calculating = false;
		} catch (Exception e) {
			e.printStackTrace();
		}

		LocationUtils.fromDoubleArray(from, beforeFirst);
		LocationUtils.fromDoubleArray(to, beforeLast);

		return locations;
	}

	@Override
	public void tick() {

	}

	@Override
	public void draw() {
		if (locations == null){
			calculate();
		}
		if (calculating){
			return;
		}
		Location from = location.clone();

		for (double[][] locations : this.locations){
			if (locations==null){
				continue;
			}
			for (double[] location : locations){
				if (location==null){
					continue;
				}
				double[] before = LocationUtils.toDoubleArray(from);
				drawParticle(LocationUtils.fromDoubleArray(from, location));

				LocationUtils.fromDoubleArray(from, before);
			}
		}
	}

}
