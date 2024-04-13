package xyz.goldendupe.listeners;

import bet.astral.messenger.placeholder.Placeholder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.persistence.PersistentDataType;
import xyz.goldendupe.command.staff.VanishCommand;
import xyz.goldendupe.events.PlayerFirstJoinEvent;
import xyz.goldendupe.messenger.GoldenMessenger;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.messenger.GoldenPlaceholderManager;
import xyz.goldendupe.models.chatcolor.Color;

import java.util.ArrayList;
import java.util.List;

public class ConnectionListener implements GDListener{
	private final GoldenDupe goldenDupe;
	protected ConnectionListener(GoldenDupe goldenDupe) {
		this.goldenDupe = goldenDupe;
	}


	@EventHandler(priority = EventPriority.HIGHEST)
	private void onJoin(PlayerJoinEvent event) {
		goldenDupe.playerDatabase().load(event.getPlayer()).thenAccept(player -> {
			goldenDupe.playerDatabase().keepLoaded(player);
			if (player.vanished()) {
				event.getPlayer().getPersistentDataContainer().set(VanishCommand.KEY_VANISHED, PersistentDataType.BOOLEAN, true);
			} else {
				event.getPlayer().getPersistentDataContainer().remove(VanishCommand.KEY_VANISHED);
			}
		});
		Player player = event.getPlayer();
		Bukkit.getScheduler().runTaskLaterAsynchronously(goldenDupe, ()->{
			List<Placeholder> placeholders = new ArrayList<>(goldenDupe.messenger().getPlaceholderManager().playerPlaceholders("player", player));
			placeholders.add(new Placeholder("player_brand", player.getClientBrandName() != null ? player.getClientBrandName() : "Unknown Client Brand"));
			goldenDupe.messenger().broadcast(GoldenMessenger.MessageChannel.STAFF, "player-join-brand", 5, placeholders);
		}, 10);

		if (event.getPlayer().getPersistentDataContainer().has(VanishCommand.KEY_VANISHED)) {
			event.joinMessage(null);
		} else {
			event.joinMessage(
					Component.text("+", Color.EMERALD, TextDecoration.BOLD)
							.appendSpace().append(GoldenPlaceholderManager.prefixName(player))
			);
			if (!event.getPlayer().hasPlayedBefore()) {
				event.joinMessage(
						Component.text("+", Color.EMERALD, TextDecoration.BOLD)
								.appendSpace().append(GoldenPlaceholderManager.prefixName(player))
								.appendNewline().append(
										Component.text("Click to send a welcome message for ", Color.MINECOIN).append(Component.text(player.getName(), Color.EMERALD)).append(Component.text("!")
												.hoverEvent(HoverEvent.showText(Component.text("Click here to send a welcome to ", Color.GRAY).append(player.name())))
												.clickEvent(ClickEvent.runCommand("/welcome "+player.getName()))
										)
								)
				);
				PlayerFirstJoinEvent playerFirstJoinEvent = new PlayerFirstJoinEvent(player, event.joinMessage());
				playerFirstJoinEvent.callEvent();
				event.joinMessage(playerFirstJoinEvent.joinMessage());
			}
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event){
		goldenDupe.playerDatabase().unload(event.getPlayer());
	}

	@EventHandler
	private void onKick(PlayerKickEvent event){
		Player player = event.getPlayer();
		PlayerKickEvent.Cause cause = event.getCause();
		List<Placeholder> placeholders = new ArrayList<>(goldenDupe.messenger().getPlaceholderManager().playerPlaceholders("player", player));
		placeholders.add(new Placeholder("cause", cause.name()));
		placeholders.add(new Placeholder("player_brand", player.getClientBrandName() != null ? player.getClientBrandName() : "Unknown Client Brand"));

		goldenDupe.messenger().broadcast(GoldenMessenger.MessageChannel.STAFF, "player-kick-cause", 5, placeholders);
	}

	@Override
	public GoldenDupe goldenDupe() {
		return goldenDupe;
	}
}














