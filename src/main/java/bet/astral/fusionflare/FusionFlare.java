package bet.astral.fusionflare;

import bet.astral.fusionflare.models.FFModel;
import bet.astral.fusionflare.particles.FFParticle;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
public class FusionFlare {
	private final JavaPlugin javaPlugin;
	private final Set<FFModel> runningModels = new HashSet<>();
	@Setter
	@Getter
	private boolean forceParticles;

	public FusionFlare(JavaPlugin javaPlugin) {
		this.javaPlugin = javaPlugin;
	}

	public void cancel(FFModel ffModel) {
		runningModels.remove(ffModel);
	}

	public void run(FFModel model){
		runningModels.add(model);
		model.draw();
	}

	public void spawnParticle(List<Player> receivers, Location location, int amount, FFParticle<?> particle){
		location.getWorld().spawnParticle(particle.next(), receivers, null, location.getX(), location.getY(), location.getZ(), amount, particle.xOffSet(), particle.yOffSet(), particle.zOffSet(), particle.extra(), particle.data(), forceParticles);
	}

	public void ready() {
		getJavaPlugin().getServer().getScheduler().runTaskTimerAsynchronously(
				getJavaPlugin(),
				()->{
					runningModels.removeIf(Objects::isNull);
					runningModels.stream().filter(Objects::nonNull).forEach(model->{
						int ticks = model.getUntilNextTick();
						int draws = model.getUntilNextDraw();
						if (ticks>=-1){
							ticks--;
						}
						draws--;
						if (ticks == 0){
							ticks = model.getTickTime();
							model.tick();
						}
						if (draws < 1){
							draws = model.getDrawTime();
							model.draw();
						}

						model.setUntilNextDraw(draws);
						model.setUntilNextTick(ticks);
					});
				},
				10,
				4
		);
	}
}
