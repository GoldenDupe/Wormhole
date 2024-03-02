package bet.astral.fusionflare.particles;

public interface FFParticle<T> {
	double xOffSet();
	double yOffSet();
	double zOffSet();
	org.bukkit.Particle next();
	double extra();
	T data();

	FFParticle<?> amount(int i);
	int amount();
}
