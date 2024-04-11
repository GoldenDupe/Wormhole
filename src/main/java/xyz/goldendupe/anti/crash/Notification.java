package xyz.goldendupe.anti.crash;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.Nullable;

@Getter
public class Notification {
	private final long issued;
	private final int id;
	private final Location location;
	@Nullable
	private final OfflinePlayer player;
	private final Type type;

	public Notification(long issued, int id, Location location, @Nullable OfflinePlayer player, Type type) {
		this.issued = issued;
		this.id = id;
		this.location = location;
		this.player = player;
		this.type = type;
	}

	enum Type {
		ARROW_LANDED_TOO_MANY_TIMES,
		REDSTONE_200,
		REDSTONE_400,
		REDSTONE_600,
		REDSTONE_800,

	}
}
