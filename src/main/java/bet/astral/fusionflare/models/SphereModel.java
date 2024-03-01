package bet.astral.fusionflare.models;

import bet.astral.fusionflare.Model;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class SphereModel extends Model {
	public SphereModel(JavaPlugin javaPlugin, Location location, int tickSpeed, int timesToTick) {
		super(javaPlugin, location, tickSpeed, timesToTick);
	}

	@Override
	protected void tick() {

	}
}
