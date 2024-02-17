package xyz.goldendupe;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.models.GDPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PlayerDatabase {
	private final GoldenDupe goldenDupe;
	private final Map<UUID, GDPlayer> players = new HashMap<>();

	public PlayerDatabase(GoldenDupe goldenDupe) {
		this.goldenDupe = goldenDupe;
	}

	public CompletableFuture<GDPlayer> load(Player player){
		return CompletableFuture.supplyAsync(()-> new GDPlayer(player));
	}

	public void unload(Player player){
		players.remove(player.getUniqueId());
	}
	public void unload(UUID uuid){
		players.remove(uuid);
	}

	public void keepLoaded(GDPlayer player){
		players.put(player.player().getUniqueId(), player);
	}

	@NotNull
	public GDPlayer fromPlayer(Player player){
		return players.get(player.getUniqueId());
	}
}
