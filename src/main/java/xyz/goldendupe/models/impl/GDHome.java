package xyz.goldendupe.models.impl;

import bet.astral.messenger.placeholder.Placeholder;
import bet.astral.messenger.placeholder.Placeholderable;
import bet.astral.messenger.utils.PlaceholderUtils;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.utils.Position;

import java.util.Collection;
import java.util.List;

public class GDHome extends Position implements Placeholderable {
	public GDHome(String name, double x, double y, double z, float yaw, @NotNull World world) {
		super(name, java.util.UUID.randomUUID(), x, y, z, yaw, world);
	}
	public GDHome(String name, java.util.UUID uniqueId, double x, double y, double z, float yaw, @NotNull String world) {
		super(name, java.util.UUID.randomUUID(), x, y, z, yaw, world);
	}
	@Override
	public Collection<Placeholder> asPlaceholder(String s) {
		return List.of(
				PlaceholderUtils.createPlaceholder(s, "x", getX()),
				PlaceholderUtils.createPlaceholder(s, "y", getY()),
				PlaceholderUtils.createPlaceholder(s, "z", getZ()),
				PlaceholderUtils.createPlaceholder(s, "yaw", getYaw()),
				PlaceholderUtils.createPlaceholder(s, "pitch", 90),
				PlaceholderUtils.createPlaceholder(s, "xyz", "x: "+getX()+", y: "+getY()+", z: " + getZ()),
				PlaceholderUtils.createPlaceholder(s, "world", getWorldName()),
				PlaceholderUtils.createPlaceholder(s, "name_id", getName().toLowerCase()),
				PlaceholderUtils.createPlaceholder(s, "name", getName()),
				PlaceholderUtils.createPlaceholder(s, "id", getUniqueId().toString()));
	}
}
