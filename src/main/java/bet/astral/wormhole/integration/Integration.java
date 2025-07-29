package bet.astral.wormhole.integration;

import bet.astral.wormhole.objects.Request;
import bet.astral.wormhole.objects.data.PlayerHome;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface Integration {
    boolean canTeleportToHomeOrWarp(Player player, PlayerHome playerHome);
    boolean canTeleportToPlayerWarp(Player player);
    boolean canTeleportToSpawn(Player player);

    boolean canTeleportToPlayer(Player player, Player other);
    boolean canTeleportPlayerHere(Player player, Player other);
    boolean canTeleportPlayerToHome(Player player, Player other);

    boolean canAcceptTeleportRequest(Player player, Request.Type type, Request request);

    boolean canCreatePlayerHome(Player player, String name, Location location);
    boolean canCreatePlayerWarp(Player player, String name, Location location);
}
