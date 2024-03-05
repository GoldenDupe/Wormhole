package bet.astral.fusionflare.models;

import bet.astral.fusionflare.FusionFlare;
import bet.astral.fusionflare.particles.FFParticle;
import bet.astral.fusionflare.utils.Expandable;
import bet.astral.fusionflare.utils.LocationUtils;
import bet.astral.fusionflare.utils.Rotatable;
import bet.astral.fusionflare.utils.Rotation;
import org.apache.commons.math3.util.FastMath;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Range;

import static bet.astral.fusionflare.utils.LocationUtils.*;

public class CircleModel extends FFModel implements Expandable, Rotatable {
	private double size;
	private Rotation rotation;
	private double[][] locations;
	private boolean calculating = false;
 	private float completion;
	public CircleModel(FusionFlare fusionFlare, FFParticle<?> particle, Location location, @Range(from = 1, to = 40) int timeBetweenDraws, @Range(from = 0, to = 40) double size, Rotation rotation, @Range(from = 0, to = 1) float completion) {
		super(fusionFlare, particle, location, -1, timeBetweenDraws);
		this.rotation = rotation;
		this.size = size;
		this.completion = completion;
	}
	public CircleModel(FusionFlare fusionFlare, FFParticle<?> particle, Location location, @Range(from = 1, to = 40) int timeBetweenDraws, @Range(from = 0, to = 40) double size, Rotation rotation) {
		super(fusionFlare, particle, location, -1, timeBetweenDraws);
		this.rotation = rotation;
		this.size = size;
		this.completion = 1;
	}

	private void calculate() {
		if (calculating) {
			return;
		}
		calculating = true;

		double xangle = FastMath.toRadians(rotation.yaw());
		double xAxisCos = FastMath.cos(xangle);
		double xAxisSin = FastMath.sin(xangle);

		double yangle = FastMath.toRadians(rotation.pitch());
		double yAxisCos = FastMath.cos(-yangle);
		double yAxisSin = FastMath.sin(-yangle);

		double zangle = FastMath.toRadians(rotation.roll());
		double zAxisCos = FastMath.cos(zangle);
		double zAxisSin = FastMath.sin(zangle);


		int degrees = (int) (360 * completion);
		locations = new double[degrees][];

		Location clone = location.clone();
		for (int degree = 0; degree < degrees; degree++) {
			double radians = FastMath.toRadians(degree);
			double x = Math.cos(radians) * size;
			double z = Math.sin(radians) * size;


			Vector vec = new Vector(x, 0, z);
			rotateAroundAxisX(vec, xAxisCos, xAxisSin);
			rotateAroundAxisY(vec, yAxisCos, yAxisSin);
			rotateAroundAxisZ(vec, zAxisCos, zAxisSin);
//			clone.add(x, 0, z);
			clone.add(vec);

			locations[degree] = LocationUtils.toDoubleArray(clone);

			clone.subtract(vec);
//			clone.subtract(x, 0, z);
		}
		calculating = false;
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
			double[] before = LocationUtils.toDoubleArray(clone);
			clone.set(location[0], location[1], location[2]);
			drawParticle(clone);
			LocationUtils.fromDoubleArray(clone, before);
		}
	}

	@Override
	public void expand(double amount) {
		this.size+=amount;
		locations = null;
	}

	@Override
	public double getSize() {
		return size;
	}

	@Override
	public void rotate(Rotation rotation) {
		LocationUtils.combine(this.rotation, rotation);
		locations = null;
	}

	@Override
	public void setRotation(Rotation rotation) {
		this.rotation = rotation;
		locations = null;
	}
}
