package bet.astral.wormhole.managers;

import bet.astral.wormhole.managers.data.PlayerWarpDataManager;
import bet.astral.wormhole.objects.data.PlayerData;
import bet.astral.wormhole.objects.data.PlayerHome;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlayerCacheManager {
    private PlayerWarpDataManager<PlayerHome> warpDataManager;

    public PlayerCacheManager(PlayerWarpDataManager<PlayerHome> warpDataManager) {
        this.warpDataManager = warpDataManager;
    }

    public PlayerData getCache(Player sender) {
        return null;
    }

    public void save(@NotNull PlayerData playerData) {
        playerData.getDeletedWarpsAndHomes().forEach(warpDataManager::deleteWarp);
        playerData.getNewWarpsAndHomes().stream().map(playerData::getHomeOrWarp).forEach(warpDataManager::saveWarp);
    }

}
