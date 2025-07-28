package bet.astral.wormhole.managers;

import bet.astral.wormhole.objects.Teleport;
import bet.astral.wormhole.plugin.WormholePlugin;
import net.kyori.adventure.util.Ticks;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class TeleportManager implements TickableManager {
    private final WormholePlugin wormholePlugin;
    private Map<UUID, Teleport> teleports = new HashMap<>();

    public TeleportManager(WormholePlugin wormholePlugin) {
        this.wormholePlugin = wormholePlugin;
    }

    @Override
    public void startTicking() {
        Bukkit.getAsyncScheduler()
                .runAtFixedRate(wormholePlugin,
                        scheduledTask -> {

                    // Handle the teleport ticking
                    Set<UUID> removeList = new HashSet<>();
                    for (Map.Entry<UUID, Teleport> entry : teleports.entrySet()) {
                        UUID playerId = entry.getKey();
                        Teleport teleport = entry.getValue();

                        // Tick and teleport if ticks is 0 or less
                        teleport.tick();
                        if (teleport.isTeleportation()){
                            teleport.teleport();
                            removeList.add(playerId);
                        } else if (teleport.isCancelled()) {
                            removeList.add(playerId);
                        }
                    }

                    // Remove teleports done
                    removeList.forEach(teleports::remove);


                        }, 100,
                        Ticks.SINGLE_TICK_DURATION_MS,
                        TimeUnit.MILLISECONDS);
    }

    @Override
    public void stopTicking() {

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
