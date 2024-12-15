package xyz.goldendupe.database.json;

import org.bukkit.entity.Player;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.database.PlayerDatabase;
import xyz.goldendupe.datagen.defaults.PlayerDefault;
import xyz.goldendupe.models.GDPlayer;

import java.io.*;
import java.util.concurrent.CompletableFuture;

public class JsonPlayerDatabase extends PlayerDatabase {
    public JsonPlayerDatabase(GoldenDupe goldenDupe) {
        super(goldenDupe);
    }
    @Override
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
                    write(getGson().toJsonTree(new PlayerDefault(player.getUniqueId()), GDPlayer.class).getAsJsonObject(), file);
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
                    return getGson().fromJson(reader, GDPlayer.class);
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
    @Override
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
                writer.write(getGson().toJson(player, GDPlayer.class));
                writer.flush();
                writer.close();
                goldenDupe.getLogger().info("Saved: "+ player.getUniqueId());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
