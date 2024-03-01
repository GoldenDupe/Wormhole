package xyz.goldendupe.models;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.goldendupe.utils.annotations.temporal.RequireSave;

@RequireSave
public record GDSpawn(String name, String world, String permission, double x, double y, double z, float yaw, float pitch) {

	public GDSpawn(@NotNull String name, @NotNull String world, @Nullable String permission,
				   double x, double y, double z, float yaw, float pitch) {
		this.name = name;
		this.world = world;
		this.permission = permission != null ? permission : "";
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
	}

	@Contract(pure = true)
	@Override
	@NotNull
	public String name() {
		return name;
	}

	@Contract(pure = true)
	@Override
	@NotNull
	public String world() {
		return world;
	}


	public Location asLocation() {
		return new Location(Bukkit.getWorld(world), x, y ,z, yaw, pitch);
	}
}
