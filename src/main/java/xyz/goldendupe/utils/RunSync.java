package xyz.goldendupe.utils;

import org.bukkit.Bukkit;
import xyz.goldendupe.GoldenDupe;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

@FunctionalInterface
public interface RunSync {
    static <C> void runSync(RunSync runSync){
        Bukkit.getScheduler().runTask(GoldenDupe.instance(),()->runSync.run());
    }
    static <C> void runASync(RunSync runSync){
        CompletableFuture.runAsync(runSync::run);
    }

    void run();
}

