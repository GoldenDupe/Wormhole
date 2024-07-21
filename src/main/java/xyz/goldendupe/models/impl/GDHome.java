package xyz.goldendupe.models.impl;

import bet.astral.messenger.v2.placeholder.Placeholder;
import bet.astral.messenger.v2.placeholder.Placeholderable;
import net.kyori.adventure.text.Component;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.goldendupe.messenger.GoldenMessenger;
import xyz.goldendupe.utils.Position;

import java.util.Collection;
import java.util.List;

public class GDHome extends Position implements Placeholderable {
	public GDHome(String name, double x, double y, double z, float yaw, @NotNull World world) {
		super(name, java.util.UUID.randomUUID(), x, y, z, yaw, world);
	}
	public GDHome(String name, java.util.UUID uniqueId, double x, double y, double z, float yaw, @NotNull String world) {
		super(name, uniqueId, x, y, z, yaw, world);
	}

	@Override
	public @NotNull Collection<@NotNull Placeholder> toPlaceholders(@Nullable String s) {
		return List.of(
				Placeholder.of(s, "x", Component.text(GoldenMessenger.format(getX()))),
				Placeholder.of(s, "y", Component.text(GoldenMessenger.format(getY()))),
				Placeholder.of(s, "z", Component.text(GoldenMessenger.format(getZ()))),
				Placeholder.of(s, "yaw", Component.text(GoldenMessenger.format(getYaw()))),
				Placeholder.of(s, "pitch", Component.text(GoldenMessenger.format(90))),
				Placeholder.of(s, "world", Component.text(getWorldName())),
				Placeholder.of(s, "name_id", Component.text(getName().toLowerCase())),
				Placeholder.of(s, "name", Component.text(getName())),
				Placeholder.of(s, "id", Component.text(getUniqueId().toString())));
	}
}
