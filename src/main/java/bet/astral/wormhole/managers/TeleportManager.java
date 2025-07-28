package bet.astral.wormhole.managers;

import bet.astral.wormhole.objects.Teleport;
import bet.astral.wormhole.plugin.WormholePlugin;
import org.bukkit.entity.Player;

import java.util.*;

public class TeleportManager implements TickableManager {
    private final WormholePlugin wormholePlugin;
    private Map<UUID, Teleport> teleports = new HashMap<>();

    public TeleportManager(WormholePlugin wormholePlugin) {
        this.wormholePlugin = wormholePlugin;
    }

    @Override
    public void tick() {
        // Handle the teleport ticking
        Set<UUID> removeList = new HashSet<>();
        for (Map.Entry<UUID, Teleport> entry : teleports.entrySet()) {
            UUID playerId = entry.getKey();
            Teleport teleport = entry.getValue();

            // Tick and teleport if ticks is 0 or less
            teleport.tick();
            if (teleport.isTeleportation()) {
                teleport.teleport();
                removeList.add(playerId);
            } else if (teleport.isCancelled()) {
                removeList.add(playerId);
            }
        }

        // Remove teleports done
        removeList.forEach(teleports::remove);
    }

    public void addTeleport(Teleport teleport) {
    }
    public void removeTeleport(Teleport teleport) {
        if (this.teleports.get(teleport.getPlayer().getUniqueId()).equals(teleport)) {
            this.teleports.remove(teleport.getPlayer().getUniqueId());
        }
    }
    public void removeTeleport(Player player) {
        this.teleports.remove(player.getUniqueId());
    }
}
