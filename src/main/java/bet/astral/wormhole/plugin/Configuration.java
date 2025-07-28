package bet.astral.wormhole.plugin;

import bet.astral.more4j.tuples.Pair;
import bet.astral.more4j.tuples.mutable.MutablePair;
import bet.astral.wormhole.objects.Request;
import jdk.jfr.Experimental;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class Configuration {
    private final Map<String, Object> data = new HashMap<>();

    public int getTeleportRequestTime(TeleportType teleportType) {
        Object dataObj = getData("teleport_request_time");
        Object teleportTypeObj = getData((Map<String, Object>) dataObj, teleportType.name);
        if (teleportTypeObj instanceof Map<?, ?> map) {
            return getDataOfType((Map<String, Object>) map, "default", 0);
        } else if (teleportTypeObj != null) {
            return ((Number) teleportTypeObj).intValue();
        }
        return 600;
    }

    public int getTeleportRequestTime(Player player, TeleportType teleportType) {
        Object dataObj = getData("teleport_request_time");
        Object teleportTypeObj = getData((Map<String, Object>) dataObj, teleportType.name);
        if (teleportTypeObj instanceof Map<?, ?> map) {
            Map<String, Object> mapData = (Map<String, Object>) map;
            MutablePair<String, Integer> lowest = Pair.mutable("default", getDefaultTeleportDelay(teleportType));

            for (Map.Entry<String, Object> entry : mapData.entrySet()) {
                if ((float) entry.getValue() < lowest.getSecond()) {
                    if (player.hasPermission("wormhole."+teleportType.name+".request-time."+entry.getKey())) {
                        lowest.setFirst(entry.getKey());
                        lowest.setSecond((int) entry.getValue());
                    }
                }
            }
        } else if (teleportTypeObj != null) {
            return ((Number) teleportTypeObj).intValue();
        }
        return 600;
    }
    public int getDefaultTeleportDelay(TeleportType teleportType) {
        Object dataObj = getData("teleport_delay");
        Object teleportTypeObj = getData((Map<String, Object>) dataObj, teleportType.name);
        if (teleportTypeObj instanceof Map<?, ?> map) {
            return getDataOfType((Map<String, Object>) map, "default", 0);
        } else if (teleportTypeObj != null) {
            return ((Number) teleportTypeObj).intValue();
        }
        return 0;
    }

    public int getTeleportDelay(Player player, TeleportType teleportType) {
        Object dataObj = getData("teleport_delay");
        Object teleportTypeObj = getData((Map<String, Object>) dataObj, teleportType.name);
        if (teleportTypeObj instanceof Map<?, ?> map) {
            Map<String, Object> mapData = (Map<String, Object>) map;
            MutablePair<String, Integer> lowest = Pair.mutable("default", getDefaultTeleportDelay(teleportType));

            for (Map.Entry<String, Object> entry : mapData.entrySet()) {
                if ((float) entry.getValue() < lowest.getSecond()) {
                    if (player.hasPermission("wormhole."+teleportType.name+".delay."+entry.getKey())) {
                        lowest.setFirst(entry.getKey());
                        lowest.setSecond((int) entry.getValue());
                    }
                }
            }
        } else if (teleportTypeObj != null) {
            return ((Number) teleportTypeObj).intValue();
        }
        return 0;
    }

    public Object getData(String key) {
        return data.get(key);
    }

    public <T> T getData(String key, T defaultValue) {
        return (T) data.getOrDefault(key, defaultValue);
    }
    public <T> T getDataOfType(String key, T defaultValue) {
        return (T) data.getOrDefault(key, defaultValue);
    }
    public Object getData(Map<String, Object> data, String key) {
        return data.get(key);
    }

    public Object getData(Map<String, Object> data, String key, Object defaultValue) {
        return data.getOrDefault(key, defaultValue);
    }
    public <T> T getDataOfType(Map<String, Object> data, String key, T defaultValue) {
        return (T) data.getOrDefault(key, defaultValue);
    }
    @Getter
    public enum TeleportType {
        @Experimental
        TELEPORT_TO("tpa"),
        @Experimental
        TELEPORT_MY_LOCATION("tpahere"),
        @Experimental
        TELEPORT_TO_HOME("tpahome"),
        @Experimental
        TELEPORT_TO_MY_HOME("tpamyhome"),
        TELEPORT_TO_WARP("warp"),
        TELEPORT_TO_SPAWN("spawn")

        ;

        @NotNull
        public static TeleportType fromType(@NotNull Request.Type type) {
            return switch (type) {
                case Request.Type.TO_PLAYER -> TeleportType.TELEPORT_TO;
                case Request.Type.PLAYER_HERE -> TeleportType.TELEPORT_MY_LOCATION;
                case Request.Type.TO_OWN_HOME -> TeleportType.TELEPORT_TO_HOME;
            };
        }

        private final String name;
        TeleportType(String name) {
            this.name = name;
        }
    }
}
