package bet.astral.wormhole.objects;

import bet.astral.messenger.v2.Messenger;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderList;
import bet.astral.messenger.v2.translation.TranslationKey;
import bet.astral.wormhole.plugin.WormholePlugin;
import lombok.Getter;
import net.kyori.adventure.util.Ticks;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.function.*;

@Getter
public class Teleport {
    private final WormholePlugin wormholePlugin;
    private final Player player;
    private Player otherPlayer;
    private int ticksLeftUntilTeleport;
    private final Location originalLocation;
    private final Function<Void, Location> teleportLocation;
    // Just make sure the warp exists and player can teleport there
    private final Predicate<Player> canStillTeleport;
    // Teleporting..
    private final TranslationKey M_teleporting_player;
    // Teleporting..
    private final TranslationKey M_teleporting_other;
    // Player moved, message about cancelation
    private final TranslationKey M_moved_player;
    // The player has moved, the teleportation is cancelled -> to the other player
    private final TranslationKey M_moved_other;
    // 1, 2 or 3, 5 seconds until teleportation
    private final TranslationKey M_1_2_3_5_seconds;
    // The teleporting player is offline
    private final TranslationKey M_player_offline;
    // The player being teleported to is offline
    private final TranslationKey M_other_offline;
    // Random cancellation
    private final TranslationKey M_cancelled;
    private boolean isCancelled = false;

    public Teleport(WormholePlugin wormholePlugin, Player player, Player otherPlayer, int ticksLeftUntilTeleport, Function<Void, Location> teleportLocation, Predicate<Player> canStillTeleport, TranslationKey m_teleporting_player, TranslationKey m_teleporting_other, TranslationKey m_moved_player, TranslationKey m_moved_other, TranslationKey m_1_2_3_5_seconds, TranslationKey m_player_offline, TranslationKey m_other_offline, TranslationKey mCancelled) {
        this.wormholePlugin = wormholePlugin;
        this.player = player;
        this.otherPlayer = otherPlayer;
        this.ticksLeftUntilTeleport = ticksLeftUntilTeleport;
        this.teleportLocation = teleportLocation;
        this.canStillTeleport = canStillTeleport;
        M_teleporting_player = m_teleporting_player;
        M_teleporting_other = m_teleporting_other;
        M_moved_player = m_moved_player;
        M_moved_other = m_moved_other;
        M_1_2_3_5_seconds = m_1_2_3_5_seconds;
        M_player_offline = m_player_offline;
        M_other_offline = m_other_offline;
        M_cancelled = mCancelled;

        originalLocation = player.getLocation();
    }

    public void tick()  {
        Messenger messenger = wormholePlugin.getMessenger();
        PlaceholderList placeholders = new PlaceholderList();
        placeholders.add("player", player.getName());
        placeholders.add("player_displayname", player.displayName());

        boolean moved = player.getLocation().distanceSquared(originalLocation) > 0.5;

        if (otherPlayer != null) {
            placeholders.add("to_name", otherPlayer.getName());
            placeholders.add("to_displayname", otherPlayer.getName());
            if (!otherPlayer.isOnline()){
                isCancelled = true;
                messenger.message(otherPlayer, getM_other_offline(), placeholders);
            } else if (!player.isOnline()) {
                isCancelled = true;
                messenger.message(player, getM_player_offline(), placeholders);
            } else if (moved){
                isCancelled = true;
                messenger.message(player, getM_moved_player(), placeholders);
                messenger.message(otherPlayer, getM_moved_other(), placeholders);
            }
        }

        if (!canStillTeleport.test(player) && !isCancelled()) {
            isCancelled = true;
            if (getM_cancelled() != null) {
                messenger.message(player, getM_cancelled(), placeholders);
            }
        }

        if (!isCancelled){
            ticksLeftUntilTeleport--;

            if (ticksLeftUntilTeleport == Ticks.TICKS_PER_SECOND
                    || ticksLeftUntilTeleport == 2 * Ticks.TICKS_PER_SECOND
                    || ticksLeftUntilTeleport == 3 * Ticks.TICKS_PER_SECOND
                    || ticksLeftUntilTeleport == 5 * Ticks.TICKS_PER_SECOND) {
                placeholders.add("seconds", ticksLeftUntilTeleport/20);
                messenger.message(player, M_1_2_3_5_seconds, placeholders);
            }
        }
    }

    public boolean isTeleportation() {
        return ticksLeftUntilTeleport <= 0;
    }

    public void teleport() {
        // Send teleportation messages
        Messenger messenger = wormholePlugin.getMessenger();
        PlaceholderList placeholders = new PlaceholderList();
        placeholders.add("player", player.getName());
        placeholders.add("player_displayname", player.displayName());
        if (otherPlayer != null) {
            placeholders.add("to_name", otherPlayer.getName());
            placeholders.add("to_displayname", otherPlayer.getName());

            messenger.message(otherPlayer, getM_teleporting_other(), placeholders);
        }
        messenger.message(player, getM_teleporting_player(), placeholders);

        Location location = getTeleportLocation().apply(null);
        player.teleportAsync(location);
    }
}
