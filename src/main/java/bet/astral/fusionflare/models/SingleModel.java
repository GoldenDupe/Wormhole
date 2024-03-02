package bet.astral.fusionflare.models;

import bet.astral.fusionflare.FusionFlare;
import bet.astral.fusionflare.particles.FFParticle;
import org.bukkit.Location;

public class SingleModel extends FFModel{
	public SingleModel(FusionFlare fusionFlare, FFParticle<?> particle, Location location, int drawTime) {
		super(fusionFlare, particle, location, -1, drawTime);
	}

	@Override
	public void tick() {

	}

	@Override
	public void draw() {
		drawParticle(location);
	}
}
