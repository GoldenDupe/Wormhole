package bet.astral.fusionflare;

import lombok.Getter;
import org.apache.maven.model.locator.ModelLocator;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public abstract class Model {
	@Getter
	protected final JavaPlugin javaPlugin;
	protected BukkitTask task;
	protected Location location;
	protected int timesToTick;
	protected int nextTick;
	public Model(JavaPlugin javaPlugin, Location location, int tickSpeed, int timesToTick) {
		this.javaPlugin = javaPlugin;
		this.location = location;
	}
	abstract protected void tick();

	protected BukkitTask task() {
		return task;
	}
	public void cancel(){
		if (task != null && !task.isCancelled()){
			task.cancel();
		}
	}

	public void move(Location location){
		this.location = location;
	}
}
