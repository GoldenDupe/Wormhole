package bet.astral.wormhole.managers;

import bet.astral.wormhole.managers.data.PlayerWarpDataManager;
import bet.astral.wormhole.objects.PlayerData;
import bet.astral.wormhole.objects.PlayerHome;
import bet.astral.wormhole.objects.PlayerWarp;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

public class PlayerCacheManager {
    private PlayerWarpDataManager<PlayerHome> warpDataManager;

    public PlayerCacheManager(PlayerWarpDataManager<PlayerHome> warpDataManager) {
        this.warpDataManager = warpDataManager;
    }

    public PlayerData getCache(Player sender) {
        return null;
    }

    public void save(PlayerData playerData) {
        playerData.getDeletedWarpsAndHomes().forEach(warpDataManager::deleteWarp);
        playerData.getNewWarpsAndHomes().forEach(warpDataManager::saveWarp);

    }

}
