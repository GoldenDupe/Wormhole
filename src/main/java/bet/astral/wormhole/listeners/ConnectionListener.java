package bet.astral.wormhole.listeners;

import bet.astral.wormhole.plugin.WormholePlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener implements Listener {
    private final WormholePlugin wormhole;
    public ConnectionListener(WormholePlugin wormhole) {
        this.wormhole = wormhole;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        wormhole.getPlayerCache().load(e.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        wormhole.getPlayerCache().unload(e.getPlayer());
    }
}
