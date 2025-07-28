package bet.astral.wormhole.objects;

import bet.astral.messenger.v2.Messenger;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderList;
import bet.astral.messenger.v2.translation.Translation;
import bet.astral.wormhole.plugin.WormholePlugin;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.function.*;

@Getter
public class Teleport {
    private final WormholePlugin wormholePlugin;
    private final Player player;
    private Player otherPlayer;
    private int ticksLeftUntilTeleport;
    private Function<Player, Boolean> hasMoved;
    private Function<Void, Location> teleportLocation;
    // Just make sure the warp exists and player can teleport there
    private Predicate<Player> canStillTeleport;
    // Teleporting..
    private Translation M_teleporting_player;
    // Teleporting..
    private Translation M_teleporting_other;
    // Player moved, message about cancelation
    private Translation M_moved_player;
    // The player has moved, the teleportation is cancelled -> to the other player
    private Translation M_moved_other;
    // 1, 2 or 3, 5 seconds until teleportation
    private Translation M_1_2_3_5_seconds;
    // The teleporting player is offline
    private Translation M_player_offline;
    // The player being teleported to is offline
    private Translation M_other_offline;
    // Random cancellation
    private Translation M_cancelled;
    private boolean isCancelled = false;

    public Teleport(WormholePlugin wormholePlugin, Player player, Player otherPlayer, int ticksLeftUntilTeleport, Function<Void, Location> teleportLocation, Predicate<Player> canStillTeleport, Translation m_teleporting_player, Translation m_teleporting_other, Translation m_moved_player, Translation m_moved_other, Translation m_1_2_3_5_seconds, Translation m_player_offline, Translation m_other_offline, Translation mCancelled) {
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
    }

    public void tick()  {
        Messenger messenger = wormholePlugin.getMessenger();
        PlaceholderList placeholders = new PlaceholderList();
        placeholders.add("player", player.getName());
        placeholders.add("player_displayname", player.displayName());

        boolean moved = hasMoved.apply(player);

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
            messenger.message(player, getM_cancelled(), placeholders);
        }

        if (!isCancelled){
            ticksLeftUntilTeleport--;

            if (ticksLeftUntilTeleport == 20
                    || ticksLeftUntilTeleport == 40
                    || ticksLeftUntilTeleport == 60
                    || ticksLeftUntilTeleport == 100) {
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
