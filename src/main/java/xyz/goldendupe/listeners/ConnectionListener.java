package xyz.goldendupe.listeners;

import bet.astral.goldenmessenger.GoldenMessenger;
import bet.astral.messagemanager.placeholder.Placeholder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.Season;

import java.util.ArrayList;
import java.util.List;

@Season(added = 1, unlock = 1, alwaysUnlocked = true)
public class ConnectionListener extends GDListener{
	public ConnectionListener(GoldenDupe goldenDupe) {
		super(goldenDupe);
	}


	@EventHandler(priority = EventPriority.HIGHEST)
	private void onJoin(PlayerJoinEvent event){
		goldenDupe.playerDatabase().load(event.getPlayer()).thenAccept(player->{
			goldenDupe.playerDatabase().keepLoaded(player);
		});
		Player player = event.getPlayer();
		List<Placeholder> placeholders = new ArrayList<>(goldenDupe.messenger().createPlaceholders("player", player));
		placeholders.add(new Placeholder("player_brand", player.getClientBrandName() != null ? player.getClientBrandName() : "Unknown Client Brand"));

		goldenDupe.messenger().broadcast(GoldenMessenger.MessageChannel.STAFF, "player-join-brand", 5, placeholders);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event){
		goldenDupe.playerDatabase().unload(event.getPlayer());
	}

	@EventHandler
	private void onKick(PlayerKickEvent event){
		Player player = event.getPlayer();
		PlayerKickEvent.Cause cause = event.getCause();
		List<Placeholder> placeholders = new ArrayList<>(goldenDupe.messenger().createPlaceholders("player", player));
		placeholders.add(new Placeholder("cause", cause.name()));
		placeholders.add(new Placeholder("player_brand", player.getClientBrandName() != null ? player.getClientBrandName() : "Unknown Client Brand"));

		goldenDupe.messenger().broadcast(GoldenMessenger.MessageChannel.STAFF, "player-kick-cause", 5, placeholders);
	}
}














