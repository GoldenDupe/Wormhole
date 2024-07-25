package xyz.goldendupe.database;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.datagen.Generate;
import xyz.goldendupe.datagen.defaults.PlayerDefault;
import xyz.goldendupe.models.GDPlayer;
import xyz.goldendupe.models.serializer.PlayerSerializer;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PlayerDatabase implements Generate {
	private final Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().registerTypeAdapter(GDPlayer.class, new PlayerSerializer()).create();
	private final GoldenDupe goldenDupe;
	private final Map<UUID, GDPlayer> players = new HashMap<>();

	public PlayerDatabase(GoldenDupe goldenDupe) {
		this.goldenDupe = goldenDupe;
	}

	public CompletableFuture<GDPlayer> load(Player player) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				File file = new File(goldenDupe.getDataFolder(), "users/" + player.getUniqueId() + ".json");
				BufferedReader reader = null;
				try {
					if (file.exists()) {
						reader = new BufferedReader(new FileReader(file));
					}
				} catch (FileNotFoundException e) {
					kick(player);
					goldenDupe.getSLF4JLogger().error("Error while trying to create a file for user {} ({})", player.getUniqueId(), player.getName(), e);
					return null;
				}
				if (!file.exists() || reader != null && reader.lines().toList().isEmpty()) {
					try {
						file = getOrCreate(file);
					} catch (IOException e) {
						kick(player);
						goldenDupe.getSLF4JLogger().error("Error while trying to create a file for user {} ({})", player.getUniqueId(), player.getName(), e);
						return null;
					}
					write(gson.toJsonTree(new PlayerDefault(player.getUniqueId()), GDPlayer.class).getAsJsonObject(), file);
				}
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						kick(player);
						goldenDupe.getSLF4JLogger().error("Error while trying to create a file for user {} ({})", player.getUniqueId(), player.getName(), e);
						return null;
					}
				}
				try {
					reader = new BufferedReader(new FileReader(file));
				} catch (FileNotFoundException e) {
					kick(player);
					goldenDupe.getSLF4JLogger().error("Error while trying to create a file for user {} ({})", player.getUniqueId(), player.getName(), e);
					return null;
				}
				try {
					return gson.fromJson(reader, GDPlayer.class);
				} finally {
					try {
						reader.close();
					} catch (IOException e) {
						kick(player);
						goldenDupe.getSLF4JLogger().error("Tried to load player data for {} ({}), but an internal error accorded!", player.getUniqueId(), player.getName(), e);
					}
				}
			} catch (Exception e) {
				kick(player);
				goldenDupe.getSLF4JLogger().error("Tried to load player data for {} ({}), but an internal error accorded!", player.getUniqueId(), player.getName(), e);
			}
			return null;
		});
	}

	private void kick(Player player){
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

	public CompletableFuture<Void> save(GDPlayer player) {
		return CompletableFuture.runAsync(() -> {
			try {
				goldenDupe.getLogger().info("Saving: "+ player.getUniqueId());
				goldenDupe.getLogger().info("Saving: "+ player.getUniqueId());
				goldenDupe.getLogger().info("Saving: "+ player.getUniqueId());
				goldenDupe.getLogger().info("Saving: "+ player.getUniqueId());
				File file = getOrCreate(new File(goldenDupe.getDataFolder(), "users/"+player.getUniqueId() + ".json"));
				if (file.exists()) {
					file.delete();
				}
				file.createNewFile();
				BufferedWriter writer = new BufferedWriter(new FileWriter(file));
				writer.write(gson.toJson(player, GDPlayer.class));
				writer.flush();
				writer.close();
				goldenDupe.getLogger().info("Saved: "+ player.getUniqueId());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
	}

	@Override
	public Gson getGson() {
		return gson;
	}
}
