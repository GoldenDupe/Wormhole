package bet.astral.fusionflare.particles;

import org.bukkit.Particle;

import java.util.List;

public class AnimatedParticle<T> implements FFParticle<T>{
	private final List<Particle> particles;
	private final double x;
	private final double y;
	private final double z;
	private final double extra;
	private int current = 0;
	private final T data;
	private int amount = 1;
	private AnimatedParticle(AnimatedParticle<T> particle, int amount){
		this.particles = particle.particles;
		this.x = particle.x;
		this.y = particle.y;
		this.z = particle.z;
		this.extra = particle.extra;
		this.data = particle.data;
		this.amount = amount;
	}
	public AnimatedParticle(List<Particle> particle){
		this.particles = particle;
		this.x = 0;
		this.y = 0;
		this.z = 0;
		this.extra = 0;
		this.data = null;
	}
	public AnimatedParticle(List<Particle> particles, double extra){
		this.particles = particles;
		this.x = 0;
		this.y = 0;
		this.z = 0;
		this.extra = extra;
		this.data = null;
	}
	public AnimatedParticle(List<Particle> particles, T data){
		this.particles = particles;
		this.x = 0;
		this.y = 0;
		this.z = 0;
		this.extra = 0;
		this.data = data;
	}
	public AnimatedParticle(List<Particle> particles, double extra, T data){
		this.particles = particles;
		this.x = 0;
		this.y = 0;
		this.z = 0;
		this.extra = extra;
		this.data = data;
	}

	public AnimatedParticle(List<Particle> particles, double xOffSet, double yOffSet, double zOffSet){
		this.particles = particles;
		this.x = xOffSet;
		this.y = yOffSet;
		this.z = zOffSet;
		this.extra = 0;
		this.data = null;
	}
	public AnimatedParticle(List<Particle> particles, double xOffSet, double yOffSet, double zOffSet, double extra){
		this.particles = particles;
		this.x = xOffSet;
		this.y = yOffSet;
		this.z = zOffSet;
		this.extra = extra;
		this.data = null;
	}
	public AnimatedParticle(List<Particle> particles, double xOffSet, double yOffSet, double zOffSet, double extra, T data){
		this.particles = particles;
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
		Particle particle = particles.get(current);
		current++;

		if (current==particles.size()){
			current=0;
		}

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
		return new AnimatedParticle<>(this, i);
	}

	@Override
	public int amount() {
		return amount;
	}
}
