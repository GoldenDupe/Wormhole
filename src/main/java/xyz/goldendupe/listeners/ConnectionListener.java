package xyz.goldendupe.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.defaults.SpawnCommand;
import xyz.goldendupe.events.PlayerFirstJoinEvent;
import xyz.goldendupe.messenger.GoldenPlaceholderManager;
import xyz.goldendupe.models.chatcolor.Color;
import xyz.goldendupe.scoreboard.Scoreboard;
import xyz.goldendupe.scoreboard.scoreboard.DefaultScoreboard;


public class ConnectionListener implements GDListener {
	private final GoldenDupe goldenDupe;

	protected ConnectionListener(GoldenDupe goldenDupe) {
		this.goldenDupe = goldenDupe;
	}


	@EventHandler(priority = EventPriority.HIGHEST)
	private void onJoin(PlayerJoinEvent event) {
		goldenDupe.playerDatabase().load(event.getPlayer()).thenAccept(player -> {
			goldenDupe.playerDatabase().keepLoaded(player);
		});

		Player player = event.getPlayer();
		Bukkit.getScheduler().runTaskLaterAsynchronously(goldenDupe, () -> {
			goldenDupe.getServer().broadcast(event.getPlayer().name().appendSpace().append(Component.text("Joined using ").append(Component.text(player.getClientBrandName() != null ? player.getClientBrandName() : "Unknown Client Brand"))), "goldendupe.staff.client");
		}, 25);

		Bukkit.getScheduler().runTaskLater(goldenDupe,
				()->{
					Scoreboard.apply(player, new DefaultScoreboard(player));
				}, 5);

		event.joinMessage(
				Component.text("+", Color.EMERALD, TextDecoration.BOLD).appendSpace().append(
						Component.text().color(Color.WHITE).append(
								GoldenPlaceholderManager.prefixName(player)).decoration(TextDecoration.BOLD, false)));
		if (!event.getPlayer().hasPlayedBefore()) {
			event.joinMessage(
					Component.text("+", Color.EMERALD, TextDecoration.BOLD).appendSpace().append(
							Component.text().color(Color.WHITE).append(
							GoldenPlaceholderManager.prefixName(player)).decoration(TextDecoration.BOLD, false)));
				/*
								.appendNewline().append(
										Component.text("Click to send a welcome message for ", Color.YELLOW).append(Component.text(player.getName(), Color.EMERALD)).append(Component.text("!")
												.hoverEvent(HoverEvent.showText(Component.text("Click here to send a welcome to ", Color.GRAY).append(player.name())))
												.clickEvent(ClickEvent.runCommand("/welcome "+player.getName()))
										)
								)
				);
				 */
			goldenDupe.getSavedData().setTotalJoins(goldenDupe.getSavedData().getTotalJoins()+1);

			PlayerFirstJoinEvent playerFirstJoinEvent = new PlayerFirstJoinEvent(player, event.joinMessage());
			playerFirstJoinEvent.callEvent();
			event.joinMessage(playerFirstJoinEvent.joinMessage());

			Bukkit.getScheduler().runTaskLaterAsynchronously(goldenDupe, () -> {
				event.getPlayer().teleportAsync(goldenDupe.getSpawnDatabase().get(SpawnCommand.Spawn.OVERWORLD.getName()).asLocation());
			}, 5);
		}


		goldenDupe.getChatEventDispatcher().onJoin(player);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		goldenDupe.playerDatabase().unload(event.getPlayer());
		event.quitMessage(
				Component.text("-", Color.RED, TextDecoration.BOLD).appendSpace().append(
						Component.text().color(Color.WHITE).append(
								GoldenPlaceholderManager.prefixName(event.getPlayer())).decoration(TextDecoration.BOLD, false)));

	}

	@EventHandler
	private void onKick(PlayerKickEvent event) {
		goldenDupe.getServer().broadcast(event.getPlayer().name().appendSpace().append(Component.text("was kicked for ").append(event.reason())).append(Component.text(" (" + event.getCause().name() + ")", NamedTextColor.DARK_RED)), "goldendupe.admin.kicked");
	}

	@Override
	public GoldenDupe goldenDupe() {
		return goldenDupe;
	}
}





