package xyz.goldendupe.command.cloud;

import io.papermc.paper.brigadier.NullCommandSender;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NullSourceStack implements CommandSourceStack {
	public static final NullSourceStack INSTANCE = new NullSourceStack();
	private final CommandSender sender = NullCommandSender.INSTANCE;
	private final Location location = new Location(null, 0, 0, 0);
	@Override
	public @NotNull Location getLocation() {
		return location;
	}

	@Override
	public @NotNull CommandSender getSender() {
		return sender;
	}

	@Override
	public @Nullable Entity getExecutor() {
		return null;
	}
}
