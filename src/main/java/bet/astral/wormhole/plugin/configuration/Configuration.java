package bet.astral.wormhole.plugin.configuration;

import bet.astral.more4j.tuples.Pair;
import bet.astral.more4j.tuples.mutable.MutablePair;
import bet.astral.wormhole.objects.Request;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

public class Configuration {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final File file;
    private ConfigurationData data;

    public Configuration(File file) {
        this.file = file;
        load();
    }
    private void ensureConfigExistsOrIsComplete() {
        try {
            ConfigurationData defaultData;

            try (Reader reader = new InputStreamReader(
                    Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("config.json")),
                    StandardCharsets.UTF_8)) {
                defaultData = gson.fromJson(reader, ConfigurationData.class);
            }

            if (!file.exists()) {
                try (Writer writer = new FileWriter(file)) {
                    gson.toJson(defaultData, writer);
                }
                return;
            }

            ConfigurationData existing;
            try (Reader reader = new FileReader(file)) {
                existing = gson.fromJson(reader, ConfigurationData.class);
            }

            boolean changed = false;

            if (existing.teleport_delay == null) {
                existing.teleport_delay = defaultData.teleport_delay;
                changed = true;
            }
            if (existing.teleport_request_time == null) {
                existing.teleport_request_time = defaultData.teleport_request_time;
                changed = true;
            }
            if (existing.max_homes == null) {
                existing.max_homes = defaultData.max_homes;
                changed = true;
            }
            if (existing.max_player_warps == null) {
                existing.max_player_warps = defaultData.max_player_warps;
                changed = true;
            }

            if (changed) {
                try (Writer writer = new FileWriter(file)) {
                    gson.toJson(existing, writer);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void load() {
        ensureConfigExistsOrIsComplete();
        try (FileReader reader = new FileReader(file)) {
            this.data = gson.fromJson(reader, ConfigurationData.class);
        } catch (IOException e) {
            e.printStackTrace();
            this.data = new ConfigurationData();
        }
    }

    public void save() {
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(this.data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getMaxHomes(Player player) {
        return getPermissionBasedValue(player, data.max_homes, "wormhole.home.limit", 1);
    }

    public int getMaxPlayerWarps(Player player) {
        return getPermissionBasedValue(player, data.max_player_warps, "wormhole.playerwarp.limit", 1);
    }

    // Utility method
    private int getPermissionBasedValue(Player player, Map<String, Integer> values, String permissionPrefix, int defaultValue) {
        if (player.hasPermission(permissionPrefix+".no-limit")) {
            return 9999;
        }
        MutablePair<String, Integer> best = Pair.mutable("default", values.getOrDefault("default", defaultValue));
        for (Map.Entry<String, Integer> entry : values.entrySet()) {
            if (entry.getValue() > best.getSecond()) {
                if (player.hasPermission(permissionPrefix + "." + entry.getKey())) {
                    best.setFirst(entry.getKey());
                    best.setSecond(entry.getValue());
                }
            }
        }
        return best.getSecond();
    }

    public int getTeleportRequestTime(TeleportType teleportType) {
        Map<String, Integer> typeData = data.teleport_request_time.get(teleportType.name);
        if (typeData != null) {
            return typeData.getOrDefault("default", 600);
        }
        return 600;
    }

    public int getTeleportRequestTime(Player player, TeleportType teleportType) {
        Map<String, Integer> typeData = data.teleport_request_time.get(teleportType.name);
        int defaultValue = 600;
        if (typeData != null) {
            MutablePair<String, Integer> lowest = Pair.mutable("default", typeData.getOrDefault("default", defaultValue));
            for (Map.Entry<String, Integer> entry : typeData.entrySet()) {
                if (entry.getValue() < lowest.getSecond()) {
                    if (player.hasPermission("wormhole." + teleportType.name + ".request-time." + entry.getKey())) {
                        lowest.setFirst(entry.getKey());
                        lowest.setSecond(entry.getValue());
                    }
                }
            }
            return lowest.getSecond();
        }
        return defaultValue;
    }

    public int getDefaultTeleportDelay(TeleportType teleportType) {
        Map<String, Integer> typeData = data.teleport_delay.get(teleportType.name);
        return typeData != null ? typeData.getOrDefault("default", 0) : 0;
    }

    public int getTeleportDelay(Player player, TeleportType teleportType) {
        Map<String, Integer> typeData = data.teleport_delay.get(teleportType.name);
        int defaultValue = 0;
        if (typeData != null) {
            MutablePair<String, Integer> lowest = Pair.mutable("default", typeData.getOrDefault("default", defaultValue));
            for (Map.Entry<String, Integer> entry : typeData.entrySet()) {
                if (entry.getValue() < lowest.getSecond()) {
                    if (player.hasPermission("wormhole." + teleportType.name + ".delay." + entry.getKey())) {
                        lowest.setFirst(entry.getKey());
                        lowest.setSecond(entry.getValue());
                    }
                }
            }
            return lowest.getSecond();
        }
        return defaultValue;
    }

    @Getter
    public enum TeleportType {
        @ApiStatus.Experimental
        TELEPORT_TO("tpa"),
        @ApiStatus.Experimental
        TELEPORT_MY_LOCATION("tpahere"),
        @ApiStatus.Experimental
        TELEPORT_TO_HOME("tpahome"),
        @ApiStatus.Experimental
        TELEPORT_TO_MY_HOME("tpamyhome"),
        TELEPORT_TO_WARP("warp"),
        TELEPORT_TO_SPAWN("spawn");

        private final String name;

        TeleportType(String name) {
            this.name = name;
        }

        @NotNull
        public static TeleportType fromType(@NotNull Request.Type type) {
            return switch (type) {
                case Request.Type.TO_PLAYER -> TeleportType.TELEPORT_TO;
                case Request.Type.PLAYER_HERE -> TeleportType.TELEPORT_MY_LOCATION;
                case Request.Type.TO_OWN_HOME -> TeleportType.TELEPORT_TO_HOME;
            };
        }
    }
}
