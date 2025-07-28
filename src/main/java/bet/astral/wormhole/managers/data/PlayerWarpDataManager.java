package bet.astral.wormhole.managers.data;

import bet.astral.wormhole.objects.data.PlayerHome;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface PlayerWarpDataManager<T extends PlayerHome> extends WarpDataManager<T>{
    @Override
    CompletableFuture<Void> saveWarp(T warp);

    @Override
    CompletableFuture<Void> deleteWarp(T warp);

    @Override
    CompletableFuture<T> getWarp(UUID uuid);

    @NotNull
    CompletableFuture<@NotNull List<T>> getWarps(UUID ownerId);
}
