package xyz.goldendupe.events;

import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.models.GDChat;

@Getter
public class GDChatChangeEvent extends PlayerEvent {
	@Getter(AccessLevel.NONE)
	private static final HandlerList HANDLER_LIST = new HandlerList();
	private final GDChat from;
	private final GDChat to;

	public GDChatChangeEvent(Player player, GDChat from, GDChat to) {
		super(player);
		this.from = from;
		this.to = to;
	}


	public HandlerList getHandlerList(){
		return HANDLER_LIST;
	}

	@NotNull
	public HandlerList getHandlers() {
		return HANDLER_LIST;
	}
}
