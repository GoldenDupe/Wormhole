package bet.astral.wormhole.integration;

import bet.astral.wormhole.objects.Request;
import bet.astral.wormhole.objects.data.PlayerHome;
import org.bukkit.Location;
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
    public boolean canTeleportToHomeOrWarp(Player player, PlayerHome playerHome) {
        return integrations.stream().allMatch(integration -> integration.canTeleportToHomeOrWarp(player, playerHome));
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
    public boolean canTeleportToPlayer(Player player, Player other) {
        return integrations.stream().allMatch(integration -> integration.canTeleportToPlayer(player, other));
    }

    @Override
    public boolean canTeleportPlayerHere(Player player, Player other) {
        return integrations.stream().allMatch(integration -> integration.canTeleportPlayerHere(player, other));
    }

    @Override
    public boolean canTeleportPlayerToHome(Player player, Player other) {
        return integrations.stream().allMatch(integration -> integration.canTeleportPlayerToHome(player, other));
    }

    @Override
    public boolean canAcceptTeleportRequest(Player player, Request.Type type, Request request) {
        return integrations.stream().allMatch(integration -> integration.canAcceptTeleportRequest(player, type, request));
    }

    @Override
    public boolean canCreatePlayerHome(Player player, String name, Location location) {
        return integrations.stream().allMatch(integration -> integration.canCreatePlayerHome(player, name, location));
    }

    @Override
    public boolean canCreatePlayerWarp(Player player, String name, Location location) {
        return integrations.stream().allMatch(integration -> integration.canCreatePlayerHome(player, name, location));
    }

    @Override
    public boolean canRenamePlayerHome(Player player, String oldName, String newName) {
        return integrations.stream().allMatch(integration -> integration.canRenamePlayerHome(player, oldName, newName));
    }

    @Override
    public boolean canRenamePlayerWarp(Player player, String oldName, String newName) {
        return integrations.stream().allMatch(integration -> integration.canRenamePlayerWarp(player, oldName, newName));
    }
}
