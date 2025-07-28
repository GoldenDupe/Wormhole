package bet.astral.wormhole.objects.data;

import bet.astral.messenger.v2.placeholder.Placeholder;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderList;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@Getter()
@Setter()
public class PlayerWarp extends PlayerHome {
    private boolean isPublic = false;
    private boolean allowWarpingIfOwnerOffline = true;
    private boolean requireTeleportAccept = true;

    public PlayerWarp(UUID ownerId, String name, Location location) {
        super(ownerId, name, location);
    }

    public PlayerWarp(UUID ownerId, UUID uniqueId, String name, long created, UUID worldId, String worldName, double x, double y, double z, float yaw, float pitch, boolean isPublic, boolean allowWarpingWhileOffline, boolean requireTeleportAccept) {
        super(ownerId, uniqueId, name, created, worldId, worldName, x, y, z, yaw, pitch);
        this.isPublic = isPublic;
        this.allowWarpingIfOwnerOffline = allowWarpingWhileOffline;
        this.requireTeleportAccept = requireTeleportAccept;
    }

    public boolean canWarp(Player player) {
        OfflinePlayer owner = Bukkit.getOfflinePlayer(getOwnerId());
        if (!owner.isOnline() && !allowWarpingIfOwnerOffline) {
            return false;
        }
        UUID uniqueId = player.getUniqueId();
        if (getAllowedVisitors() == null && getBannedVisitors() == null) {
            return isPublic;
        } else if (getAllowedVisitors() != null && getAllowedVisitors().contains(uniqueId)) {
            return true;
        } else if (getBannedVisitors() != null && getBannedVisitors().contains(uniqueId)) {
            return false;
        }
        return isPublic;
    }

    @Override
    public @NotNull ItemStack getDefaultIcon() {
        return (requireTeleportAccept) ? ItemStack.of(Material.OAK_DOOR) : ItemStack.of(Material.IRON_DOOR);
    }

    @Override
    public @NotNull PlaceholderList toPlaceholders(@Nullable String prefix) {
        PlaceholderList placeholders = super.toPlaceholders(prefix);
        placeholders.add(Placeholder.of(prefix, "public", Component.text(isPublic ? "Yes" : "No", isPublic ? NamedTextColor.GREEN : NamedTextColor.RED)));
        placeholders.add(Placeholder.of(prefix, "allow_teleporting_offline", Component.text(allowWarpingIfOwnerOffline ? "Yes" : "No", allowWarpingIfOwnerOffline ? NamedTextColor.GREEN : NamedTextColor.RED)));
        placeholders.add(Placeholder.of(prefix, "require_teleport_accept", Component.text(requireTeleportAccept ? "Yes" : "No", requireTeleportAccept ? NamedTextColor.GREEN : NamedTextColor.RED)));
        return placeholders;
    }
}
