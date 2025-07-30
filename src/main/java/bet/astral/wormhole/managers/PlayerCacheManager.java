package bet.astral.wormhole.managers;

import bet.astral.wormhole.managers.data.PlayerDataManager;
import bet.astral.wormhole.managers.data.PlayerWarpDataManager;
import bet.astral.wormhole.objects.data.PlayerData;
import bet.astral.wormhole.objects.data.PlayerHome;
import bet.astral.wormhole.objects.data.PlayerWarp;
import bet.astral.wormhole.objects.data.Warp;
import bet.astral.wormhole.plugin.WormholePlugin;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerCacheManager {
    private PlayerWarpDataManager<PlayerHome> warpDataManager;
    private PlayerDataManager playerDataManager;
    private Map<UUID, PlayerData> playerDataMap = new HashMap<>();

    public PlayerCacheManager(WormholePlugin plugin) {
        this.warpDataManager = plugin.getPlayerWarpDataManager();
        this.playerDataManager = plugin.getPlayerDataManager();
    }

    public PlayerData getCache(Player sender) {
        return playerDataMap.get(sender.getUniqueId());
    }

    public void save(@NotNull PlayerData playerData) {
        playerData.getDeletedWarpsAndHomes().forEach(warpDataManager::deleteWarp);
        playerData.getNewWarpsAndHomes().stream().map(playerData::getHomeOrWarp).forEach(warpDataManager::saveWarp);
    }

    public void load(@NotNull Player player) {
        PlayerData playerData = new PlayerData(player.getUniqueId());

        warpDataManager.getWarps(player.getUniqueId()).thenAccept(warps -> {
            for (Warp warp : warps) {
                if (warp instanceof PlayerWarp playerWarp) {
                    playerData.addWarp(playerWarp);
                } else if (warp instanceof PlayerHome playerHome) {
                    playerData.addHome(playerHome);
                }
            }
            playerData.getDeletedWarpsAndHomes().clear();
            playerData.getNewWarpsAndHomes().clear();
            playerData.getUpdatedWarpsAndHomes().clear();
        });

        playerDataMap.put(player.getUniqueId(), playerData);
    }

    public void unload(@NotNull Player player) {
        playerDataMap.remove(player.getUniqueId());
    }
}
