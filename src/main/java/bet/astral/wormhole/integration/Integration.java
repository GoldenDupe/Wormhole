package bet.astral.wormhole.integration;

import org.bukkit.entity.Player;

public interface Integration {
    boolean canTeleportToHome(Player player);
    boolean canTeleportToWarp(Player player);
    boolean canTeleportToPlayerWarp(Player player);
    boolean canTeleportToSpawn(Player player);
    boolean canTeleportPlayersHere(Player player);

    boolean canSetHome(Player player);
    boolean canSetPlayerWarp(Player player);
}
