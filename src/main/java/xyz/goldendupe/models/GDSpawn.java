package xyz.goldendupe.models;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.security.Permission;

public record GDSpawn(String name, String world, String permission, double x, double y, double z, float pitch, float yaw) {
	public GDSpawn(@NotNull String name, @NotNull String world, @Nullable String permission, double x, double y, double z, float pitch, float yaw) {
		this.name = name;
		this.world = world;
		this.permission = permission != null ? permission : "";
		this.x = x;
		this.y = y;
		this.z = z;
		this.pitch = pitch;
		this.yaw = yaw;
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


}
