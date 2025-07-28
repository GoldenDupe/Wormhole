package bet.astral.wormhole.managers.data;

import bet.astral.wormhole.objects.Warp;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface WarpDataManager<T extends Warp> {
    CompletableFuture<Void> saveWarp(T warp);
    CompletableFuture<Void> deleteWarp(T warp);
    CompletableFuture<T> getWarp(UUID uuid);
}
