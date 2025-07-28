package bet.astral.wormhole.plugin;

import bet.astral.more4j.tuples.Pair;
import bet.astral.more4j.tuples.mutable.MutablePair;
import jdk.jfr.Experimental;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class Configuration {
    private final Map<String, Object> data = new HashMap<>();

    public float getDefaultTeleportDelay(TeleportType teleportType) {
        Object dataObj = getData("teleport_delay");
        Object teleportTypeObj = getData((Map<String, Object>) dataObj, teleportType.name);
        if (teleportTypeObj instanceof Map<?, ?> map) {
            return getDataOfType((Map<String, Object>) map, "default", 0);
        } else if (teleportTypeObj != null) {
            return ((Number) teleportTypeObj).floatValue();
        }
        return 0;
    }

    public float getTeleportDelay(Player player, TeleportType teleportType) {
        Object dataObj = getData("teleport_delay");
        Object teleportTypeObj = getData((Map<String, Object>) dataObj, teleportType.name);
        if (teleportTypeObj instanceof Map<?, ?> map) {
            Map<String, Object> mapData = (Map<String, Object>) map;
            MutablePair<String, Float> lowest = Pair.mutable("default", getDefaultTeleportDelay(teleportType));

            for (Map.Entry<String, Object> entry : mapData.entrySet()) {
                if ((float) entry.getValue() < lowest.getSecond()) {
                    if (player.hasPermission("wormhole."+teleportType.name+".delay."+entry.getKey())) {
                        lowest.setFirst(entry.getKey());
                        lowest.setSecond((float) entry.getValue());
                    }
                }
            }
        } else if (teleportTypeObj != null) {
            return ((Number) teleportTypeObj).floatValue();
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
    enum TeleportType {
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

        private final String name;
        TeleportType(String name) {
            this.name = name;
        }
    }
}
