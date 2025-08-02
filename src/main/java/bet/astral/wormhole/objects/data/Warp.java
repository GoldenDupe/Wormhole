package bet.astral.wormhole.objects.data;

import bet.astral.messenger.v2.Messenger;
import bet.astral.messenger.v2.permission.Permission;
import bet.astral.messenger.v2.placeholder.Placeholder;
import bet.astral.messenger.v2.placeholder.Placeholderable;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderList;
import bet.astral.messenger.v3.minecraft.paper.PaperMessenger;
import bet.astral.wormhole.managers.TeleportManager;
import bet.astral.wormhole.objects.Teleport;
import bet.astral.wormhole.plugin.translation.Translations;
import bet.astral.wormhole.plugin.WormholePlugin;
import bet.astral.wormhole.plugin.configuration.Configuration;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

@Getter()
@Setter()
public class Warp implements Placeholderable {
    public static WormholePlugin wormholePlugin = WormholePlugin.getPlugin(WormholePlugin.class);
    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd:MM:yyyy HH:mm:ss");
    @Setter(AccessLevel.NONE)
    private List<UUID> allowedVisitors;
    @Setter(AccessLevel.NONE)
    private List<UUID> bannedVisitors;
    private final UUID uniqueId;
    private String name;
    private Component displayname = null;
    private Component description = null;
    private Permission permission = Permission.empty();
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
    private Map<UUID, Long> cooldowns = new HashMap<>();
    private long cooldown;
    private boolean exists = true;

    public Warp(UUID uniqueId, String name, long created, UUID worldId, String worldName, double x, double y, double z, float yaw, float pitch) {
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

    public Warp(String name, Location location) {
        this.uniqueId = UUID.randomUUID();
        this.name = name;
        this.created = System.currentTimeMillis();
        this.worldId = location.getWorld().getUID();
        this.worldName = location.getWorld().getName();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }

    public Component getDisplayname() {
        return displayname != null ? displayname : Component.text(name);
    }

    public Component getDescription() {
        return description != null ? description : Component.text("");
    }

    @Override
    public @NotNull Collection<@NotNull Placeholder> toPlaceholders(@Nullable String prefix) {
        return new PlaceholderList(List.of(
                Placeholder.of(prefix, "name", Component.text(getName())),
                Placeholder.of(prefix, "id", Component.text(getUniqueId().toString())),
                Placeholder.of(prefix, "displayname", getDisplayname()),
                Placeholder.of(prefix, "description", Component.text(getName())),
                Placeholder.of(prefix, "created", Component.text(DATE_FORMAT.format(Date.from(Instant.ofEpochMilli(created))))),
                Placeholder.of(prefix, "world", Component.text(worldName)),
                Placeholder.of(prefix, "x", Component.text(getX())),
                Placeholder.of(prefix, "y", Component.text(getY())),
                Placeholder.of(prefix, "z", Component.text(getZ())),
                Placeholder.of(prefix, "yaw", Component.text(getY())),
                Placeholder.of(prefix, "pitch", Component.text(getPitch()))
        ));
    }

    public void updateLocation(Location location) {
        this.worldId = location.getWorld().getUID();
        this.worldName = location.getWorld().getName();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }

    public void addVisitor(UUID uniqueId) {
        if (this.allowedVisitors == null) {
            this.allowedVisitors = new ArrayList<>();
        }
        this.allowedVisitors.add(uniqueId);
    }

    public void removeVisitor(UUID uniqueId) {
        if (this.allowedVisitors != null) {
            this.allowedVisitors.remove(uniqueId);
            if (this.allowedVisitors.isEmpty()) {
                this.allowedVisitors = null;
            }
        }
    }

    public void addBannedVisitor(UUID uniqueId) {
        if (this.bannedVisitors == null) {
            this.bannedVisitors = new ArrayList<>();
        }
        this.bannedVisitors.add(uniqueId);
    }

    public void removeBannedVisitor(UUID uniqueId) {
        if (this.bannedVisitors != null) {
            this.bannedVisitors.remove(uniqueId);
            if (this.bannedVisitors.isEmpty()) {
                this.bannedVisitors = null;
            }
        }
    }

    public World getWorld() {
        return Bukkit.getWorld(uniqueId) != null ? Bukkit.getWorld(uniqueId) : Bukkit.getWorld(name);
    }

    public boolean canWarp(Player player) {
        UUID uniqueId = player.getUniqueId();
        if (allowedVisitors == null && bannedVisitors == null) {
            return permission != null ? permission.test(PaperMessenger.playerManager.players.get(player.getUniqueId())) : true;
        } else if (allowedVisitors != null && allowedVisitors.contains(uniqueId)) {
            return true;
        } else if (bannedVisitors != null && bannedVisitors.contains(uniqueId)) {
            return false;
        }
        return permission != null ? permission.test(PaperMessenger.playerManager.players.get(player.getUniqueId())) : true;
    }

    private PlayerWarpState canWarpState(Player player) {
        UUID uniqueId = player.getUniqueId();
        if (allowedVisitors == null && bannedVisitors == null) {
            return permission != null ? permission.test(PaperMessenger.playerManager.players.get(player.getUniqueId())) ? PlayerWarpState.ALLOWED : PlayerWarpState.NO_PERMISSION : PlayerWarpState.ALLOWED;
        } else if (allowedVisitors != null && allowedVisitors.contains(uniqueId)) {
            return PlayerWarpState.ALLOWED;
        } else if (bannedVisitors != null && bannedVisitors.contains(uniqueId)) {
            return PlayerWarpState.BANNED;
        }
        return permission != null ? permission.test(PaperMessenger.playerManager.players.get(player.getUniqueId())) ? PlayerWarpState.ALLOWED : PlayerWarpState.NO_PERMISSION : PlayerWarpState.ALLOWED;
    }

    @NotNull
    public ItemStack getDefaultIcon() {
        return ItemStack.of(Material.OAK_DOOR);
    }

    @NotNull
    public final ItemStack getIconOrDefault() {
        return icon != null ? icon : getDefaultIcon();
    }

    public void warp(Player player) {
        Messenger messenger = wormholePlugin.getMessenger();
        PlaceholderList placeholders = new PlaceholderList();
        placeholders.addAll(
                Placeholder.of("name", name),
                Placeholder.of("displayname", displayname != null ? displayname : Component.text(name)),
                Placeholder.of("description", description != null ? description : Component.text("-"))
        );

        PlayerWarpState playerWarpState = canWarpState(player);
        switch (playerWarpState) {
            case COOLDOWN -> {
                long cooldown = System.currentTimeMillis() - this.cooldowns.get(player.getUniqueId());
                double seconds = cooldown / 1000.0;
                placeholders.add("cooldown", DECIMAL_FORMAT.format(seconds));
                messenger.message(player, Translations.M_WARP_COOLDOWN, placeholders);
            }
            case ALLOWED -> {
                TeleportManager teleportManager = wormholePlugin.getTeleportManager();
                int ticksDelay = wormholePlugin.getConfiguration().getTeleportDelay(player, Configuration.TeleportType.TELEPORT_TO_HOME);
                teleportManager.addTeleport(
                        new Teleport(
                                wormholePlugin,
                                player,
                                null,
                                ticksDelay,
                                _-> getLocation(),
                                _ -> isExists(),
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null
                        )
                );
            }
            case BANNED -> {
                messenger.message(player, Translations.M_WARP_BANNED, placeholders);
            }
            case NO_PERMISSION -> {
                messenger.message(player, Translations.M_WARP_NO_PERMISSION, placeholders);
            }
        }
    }

    public Location getLocation() {
        return new Location(Bukkit.getWorld(worldId), x, y, z, yaw, pitch);
    }

    public void relocate(@NotNull Location location) {
        this.worldId = location.getWorld().getUID();
        this.worldName = location.getWorld().getName();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }

    enum PlayerWarpState {
        COOLDOWN,
        NO_PERMISSION,
        BANNED,
        ALLOWED,
    }
}
