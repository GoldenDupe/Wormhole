package bet.astral.fusionflare.particles;

import org.bukkit.Particle;

public class LiteralParticle<T> implements FFParticle<T> {
	private final Particle particle;
	private final double x;
	private final double y;
	private final double z;
	private final double extra;
	private final T data;
	private int amount = 1;
	private LiteralParticle(LiteralParticle<T> particle, int amount){
		this.particle = particle.particle;
		this.x = particle.x;
		this.y = particle.y;
		this.z = particle.z;
		this.extra = particle.extra;
		this.data = particle.data;
		this.amount = amount;
	}
	public LiteralParticle(Particle particle){
		this.particle = particle;
		this.x = 0;
		this.y = 0;
		this.z = 0;
		this.extra = 0;
		this.data = null;
	}
	public LiteralParticle(Particle particle, double extra){
		this.particle = particle;
		this.x = 0;
		this.y = 0;
		this.z = 0;
		this.extra = extra;
		this.data = null;
	}
	public LiteralParticle(Particle particle, T data){
		this.particle = particle;
		this.x = 0;
		this.y = 0;
		this.z = 0;
		this.extra = 0;
		this.data = data;
	}
	public LiteralParticle(Particle particle, double extra, T data){
		this.particle = particle;
		this.x = 0;
		this.y = 0;
		this.z = 0;
		this.extra = extra;
		this.data = data;
	}

	public LiteralParticle(Particle particle, double xOffSet, double yOffSet, double zOffSet){
		this.particle = particle;
		this.x = xOffSet;
		this.y = yOffSet;
		this.z = zOffSet;
		this.extra = 0;
		this.data = null;
	}
	public LiteralParticle(Particle particle, double xOffSet, double yOffSet, double zOffSet, double extra){
		this.particle = particle;
		this.x = xOffSet;
		this.y = yOffSet;
		this.z = zOffSet;
		this.extra = extra;
		this.data = null;
	}
	public LiteralParticle(Particle particle, double xOffSet, double yOffSet, double zOffSet, double extra, T data){
		this.particle = particle;
		this.x = xOffSet;
		this.y = yOffSet;
		this.z = zOffSet;
		this.extra = extra;
		this.data = data;
	}

	@Override
	public double xOffSet() {
		return x;
	}

	@Override
	public double yOffSet() {
		return y;
	}

	@Override
	public double zOffSet() {
		return z;
	}

	@Override
	public Particle next() {
		return particle;
	}

	@Override
	public double extra() {
		return extra;
	}

	@Override
	public T data() {
		return data;
	}

	@Override
	public FFParticle<T> amount(int i) {
		return new LiteralParticle<>(this, i);
	}

	@Override
	public int amount() {
		return amount;
	}
}
