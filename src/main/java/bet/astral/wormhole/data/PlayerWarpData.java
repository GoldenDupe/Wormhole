package bet.astral.wormhole.data;

import java.util.UUID;

@Getter()
@Setter()
public class PlayerWarpData extends PlayerHomeData {
    private boolean isPublic = false;
    private boolean allowWarpingIfOwnerOffline = true;

    public boolean canWarp(Player player) {
        OfflinePlayer owner = Bukkit.getOfflinePlayer(getOwnerId());
        if (!owner.isOnline() && !allowWarpingIfOwnerOffline) {
            return false;
        }
        UUID uniqueId = player.getUniqueId();
        if (allowedVisitors == null && bannedVisitors == null) {
            return isPublic;
        } else if (allowedVisitors != null && allowedVisitors.contains(uniqueId)) {
            return true;
        } else if (bannedVisitors != null && bannedVisitors.contains(uniqueId)) {
            return false;
        }
        return isPublic;
    }
}
