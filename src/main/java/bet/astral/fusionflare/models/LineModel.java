package bet.astral.fusionflare.models;

import bet.astral.fusionflare.FusionFlare;
import bet.astral.fusionflare.particles.FFParticle;
import org.bukkit.Location;
import org.bukkit.util.NumberConversions;

public class LineModel extends FFModel {
	private double[][] locations;
	private Location end;
	private final double between;
	private boolean calculating = false;
	public LineModel(FusionFlare fusionFlare, FFParticle<?> particle, Location location, Location end, int timeBetweenDraws, double between) {
		super(fusionFlare, particle, location, -1, timeBetweenDraws);
		this.end = end;
		this.between = between;
		calculate();
	}

	public void moveEnd(Location location){
		this.end = location;
		calculate();
	}

	private void calculate() {
		if (calculating) {
			return;
		}
		calculating = true;
		locations = null;
		double x = end.getX() - location.getX();
		double y = end.getY() - location.getY();
		double z = end.getZ() - location.getZ();
		double length = Math.sqrt(NumberConversions.square(x) + NumberConversions.square(y) + NumberConversions.square(z));
		x /= length;
		y /= length;
		z /= length;

		try {
			double[][] newLocations = new double[(int) (length / between) + 12][];
			int j = 0;
			for (double i = 0; i < length + 0.001; i += between) {
				if (i > length) i = length;
				newLocations[j] = new double[]{location.getX() + x * i, location.getY() + y * i, location.getZ() + z * i};
				j++;
			}




			locations = newLocations;
			calculating = false;
			draw();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void move(Location center) {
		super.move(center);
		calculate();
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

		Location clone = location.clone();
		for (double[] location : locations){
			if (location == null){
				continue;
			}
			clone.set(location[0], location[1], location[2]);
			drawParticle(clone);
		}
	}
}
