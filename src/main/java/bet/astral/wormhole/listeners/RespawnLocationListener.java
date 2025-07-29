package bet.astral.wormhole.listeners;

import bet.astral.wormhole.objects.data.PlayerData;
import bet.astral.wormhole.objects.data.PlayerHome;
import bet.astral.wormhole.plugin.WormholePlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class RespawnLocationListener implements Listener {
    private final WormholePlugin wormhole;
    public RespawnLocationListener(@NotNull WormholePlugin wormhole) {
        this.wormhole = wormhole;
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        PlayerData playerData = wormhole.getPlayerCache().getCache(e.getPlayer());
        UUID primaryHome = playerData.getPrimaryHome();
        if (primaryHome == null) {
            return;
        }

        PlayerHome home = playerData.getHome(primaryHome);
        if (home == null || !home.isExists()) {
            return;
        }

        e.setRespawnLocation(home.getLocation());
    }
}
