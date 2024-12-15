package xyz.goldendupe.database;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.datagen.Generate;
import xyz.goldendupe.models.GDPlayer;
import xyz.goldendupe.models.serializer.JsonPlayerSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public abstract class PlayerDatabase implements Generate {
	private final Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().registerTypeAdapter(GDPlayer.class, new JsonPlayerSerializer()).create();
	protected final GoldenDupe goldenDupe;
	private final Map<UUID, GDPlayer> players = new HashMap<>();

	public PlayerDatabase(GoldenDupe goldenDupe) {
		this.goldenDupe = goldenDupe;
	}

	public abstract CompletableFuture<GDPlayer> load(Player player);
	protected void kick(Player player){
		player.getScheduler().run(goldenDupe, (t) -> {
			player.kick(
					Component.empty().append(Component.text("Golden", NamedTextColor.GOLD, TextDecoration.BOLD)
									.append(Component.text("Dupe", NamedTextColor.WHITE, TextDecoration.BOLD))
									.appendSpace()
									.append(Component.text("Internal Error", NamedTextColor.RED, TextDecoration.BOLD)))
							.append(Component.text().decoration(TextDecoration.BOLD, false))
							.appendNewline()
							.append(Component.text("Internal error has accorded while loading your player data!", NamedTextColor.RED))
							.appendNewline()
							.appendNewline()
							.append(Component.text("UUID: " + player.getUniqueId(), NamedTextColor.YELLOW))
							.appendNewline()
							.append(Component.text("Name: " + player.getName(), NamedTextColor.YELLOW))
							.appendNewline()
							.append(Component.text("DISCORD: " + GoldenDupe.DISCORD, NamedTextColor.AQUA)).decoration(TextDecoration.BOLD, false));
		}, null);
	}

	public void unload(Player player){
		players.remove(player.getUniqueId());
	}
	public void unload(UUID uuid){
		players.remove(uuid);
	}

	public void keepLoaded(GDPlayer player){
		players.put(player.uuid(), player);
	}

	public GDPlayer fromPlayer(Player player){
		return players.get(player.getUniqueId());
	}

	public abstract CompletableFuture<Void> save(GDPlayer player);

	@Override
	public Gson getGson() {
		return gson;
	}
}
