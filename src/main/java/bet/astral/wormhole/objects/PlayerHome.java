package bet.astral.wormhole.objects;

import bet.astral.messenger.v2.placeholder.Placeholder;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderList;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

@Getter()
@Setter()
public class PlayerHome extends Warp {
    private UUID ownerId;

    public PlayerHome(Player player, String name) {
        super(player.getUniqueId(),
                name,
                System.currentTimeMillis(),
                player.getWorld().getUID(),
                player.getWorld().getName(),
                player.getX(), player.getY(),
                player.getZ(),
                player.getYaw(),
                player.getPitch());

    }

    public PlayerHome(UUID ownerId, UUID uniqueId, String name, long created, UUID worldId, String worldName, double x, double y, double z, float yaw, float pitch) {
        super(uniqueId, name, created, worldId, worldName, x, y, z, yaw, pitch);
        this.ownerId = ownerId;
    }

    public PlayerHome(UUID ownerId, String name, Location location) {
        super(name, location);
        this.ownerId = ownerId;
    }

    @Override
    public boolean canWarp(Player player) {
        return player.getUniqueId().equals(ownerId);
    }

    @Override
    public @NotNull ItemStack getDefaultIcon() {
        return ItemStack.of(Material.RED_BED);
    }

    @Override
    public @NotNull PlaceholderList toPlaceholders(@Nullable String prefix) {
        PlaceholderList placeholderList = new PlaceholderList(super.toPlaceholders(prefix));
        placeholderList.add(Placeholder.of(prefix, "owner", Component.text(Objects.requireNonNull(Bukkit.getOfflinePlayer(ownerId).getName()))));
        placeholderList.add(Placeholder.of(prefix, "name", Component.text(ownerId.toString())));
        return placeholderList;
    }
}
