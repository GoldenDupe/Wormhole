package xyz.goldendupe.events;

import lombok.AccessLevel;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerFirstJoinEvent extends PlayerJoinEvent {
	@Getter(AccessLevel.NONE)
	private static final HandlerList HANDLER_LIST = new HandlerList();
	public PlayerFirstJoinEvent(@NotNull Player playerJoined, @Nullable Component joinMessage) {
		super(playerJoined, joinMessage);
	}


	public static HandlerList getHandlerList(){
		return HANDLER_LIST;
	}

	@NotNull
	public HandlerList getHandlers() {
		return HANDLER_LIST;
	}
}
