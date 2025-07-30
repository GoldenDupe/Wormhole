package bet.astral.wormhole.managers.data;

import bet.astral.wormhole.objects.data.PlayerData;

import java.util.concurrent.CompletableFuture;

public interface PlayerDataManager {
    CompletableFuture<Void> load(PlayerData playerData);
    CompletableFuture<Void> save(PlayerData playerData);
}
