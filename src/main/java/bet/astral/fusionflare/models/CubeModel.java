package bet.astral.fusionflare.models;

import bet.astral.fusionflare.FusionFlare;
import bet.astral.fusionflare.particles.FFParticle;
import bet.astral.fusionflare.utils.Expandable;
import org.bukkit.Location;
import org.codehaus.plexus.util.Expand;

public class CubeModel extends CuboidModel implements Expandable {
	private static double[][] create(double size){
		double[][] corners = new double[8][];
		double divided = size/2;
		corners[0] = new double[]{-divided, divided, -divided};
		corners[1] = new double[]{divided, divided, -divided};
		corners[2] = new double[]{-divided, divided, divided};
		corners[3] = new double[]{divided, divided, divided};

		corners[4] = new double[]{-divided, -divided, -divided};
		corners[5] = new double[]{divided, -divided, -divided};
		corners[6] = new double[]{-divided, -divided, divided};
		corners[7] = new double[]{divided, -divided, divided};
		return corners;
	}
	private double size;
	public CubeModel(FusionFlare fusionFlare, FFParticle<?> particle, Location location, int timeBetweenTicks, int timeBetweenDraws, double betweenEach, double size) {
		super(fusionFlare, particle, location, timeBetweenTicks, timeBetweenDraws, betweenEach, create(size));

	}

	@Override
	public void expand(double amount) {
		this.size+=amount;
		super.resize(create(amount));
	}

	@Override
	public double getSize() {
		return size;
	}
}
