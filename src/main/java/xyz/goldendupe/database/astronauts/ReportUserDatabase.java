package xyz.goldendupe.database.astronauts;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.models.astronauts.RUser;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class ReportUserDatabase implements Listener {
	private final Map<OfflinePlayer, RUser> users = new HashMap<>();
	private final Set<RUser> requestedSaves = new HashSet<>();
	private final GoldenDupe goldenDupe;
	public ReportUserDatabase(GoldenDupe goldenDupe){
		this.goldenDupe = goldenDupe;
	}
	public RUser asUser(Player player){
		if (users.get(player) == null){
			// The item is probably loading the player so just give a false one
			return new RUser(player.getUniqueId());
		}
		return users.get(player);
	}

	private void unload(Player player){
		users.remove(player);
	}
	public CompletableFuture<RUser> load(OfflinePlayer player){
		return CompletableFuture.supplyAsync(new Supplier<RUser>() {
			@Override
			public RUser get() {
				return new RUser(player.getUniqueId());
			}
		});
	}

	public void requestSave(RUser user){
		if (requestedSaves.contains(user)){
			return;
		}
		requestedSaves.add(user);
	}

	@EventHandler
	private void onJoin(PlayerJoinEvent event){
		load(event.getPlayer()).thenAccept(user->{
			users.put(event.getPlayer(), user);
		});
	}

	@EventHandler
	private void onQuit(PlayerQuitEvent event){
		requestSave(users.get(event.getPlayer()));
		unload(event.getPlayer());
	}
}
