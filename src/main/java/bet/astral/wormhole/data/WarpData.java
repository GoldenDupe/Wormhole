package bet.astral.wormhole.data;

import java.util.List;
import java.util.UUID;

@Getter()
@Setter()
public class WarpData {
    private List<UUID> allowedVisitors;
    private List<UUID> bannedVisitors;
    private final UUID uniqueId;
    private String name;
    private Component displayname = null;
    private Component description = null;
    private Permission permission = Permission.none();
    private long created;
    private UUID worldId;
    // Backup option, only fall back to this if the world is nt found !!
    private String worldName;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private ItemStack icon = null;

    public WarpData(UUID uniqueId, String name, long created, UUID worldId, String worldName, double x, double y, double z, float yaw, float pitch) {
        this.uniqueId = uniqueId;
        this.name = name;
        this.created = created;
        this.worldId = worldId;
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public WarpData(String name, Location location) {
        this.uniqueId = UUID.randomUUID();
        this.name = name;
        this.created = System.currentTimeMillis();
        this.worldId = location.getWorld().getId();
        this.worldName = location.getWorld().getName();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }

    public void updateLocation(Location location) {
        this.worldId = location.getWorld().getId();
        this.worldName = location.getWorld().getName();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }

    public boolean canWarp(Player player) {
        UUID uniqueId = player.getUniqueId();
        if (allowedVisitors == null && bannedVisitors == null) {
            return permission != null ? permission.test(player) : true;
        } else if (allowedVisitors != null && allowedVisitors.contains(uniqueId)) {
            return true;
        } else if (bannedVisitors != null && bannedVisitors.contains(uniqueId)) {
            return false;
        }
        return permission != null ? permission.test(player) : true;
    }
}
