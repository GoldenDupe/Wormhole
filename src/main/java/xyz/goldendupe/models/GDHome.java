package xyz.goldendupe.models;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.utils.annotations.temporal.RequireSave;

import java.util.UUID;

@RequireSave
public record GDHome(@NotNull String name, @NotNull String world, @NotNull String uuid,
                     double x, double y, double z, float yaw, float pitch) {

    public GDHome(@NotNull String name, @NotNull String world, double x, double y, double z, float yaw, float pitch) {
        this(name, world, UUID.randomUUID().toString(), x, y, z, yaw, pitch);
    }

    @Contract(pure = true)
    @Override
    @NotNull
    public String name() {
        return name;
    }

    @Contract(pure = true)
    @Override
    @NotNull
    public String world() {
        return world;
    }


    public Location asLocation() {
        return new Location(Bukkit.getWorld(world), x, y ,z, yaw, pitch);
    }
}