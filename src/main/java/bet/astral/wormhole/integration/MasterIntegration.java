package bet.astral.wormhole.integration;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class MasterIntegration implements Integration {
    private Set<Integration> integrations = new HashSet<>();

    public void registerIntegration(Integration integration) {
        integrations.add(integration);
    }

    public void unregisterIntegration(Integration integration) {
        integrations.remove(integration);
    }

    @Override
    public boolean canTeleportToHome(Player player) {
        return integrations.stream().allMatch(integration -> integration.canTeleportToHome(player));
    }

    @Override
    public boolean canTeleportToWarp(Player player) {
        return integrations.stream().allMatch(integration -> integration.canTeleportToWarp(player));
    }

    @Override
    public boolean canTeleportToPlayerWarp(Player player) {
        return integrations.stream().allMatch(integration -> integration.canTeleportToPlayerWarp(player));
    }

    @Override
    public boolean canTeleportToSpawn(Player player) {
        return integrations.stream().allMatch(integration -> integration.canTeleportToSpawn(player));
    }

    @Override
    public boolean canTeleportPlayersHere(Player player) {
        return integrations.stream().allMatch(integration -> integration.canTeleportPlayersHere(player));
    }
}
