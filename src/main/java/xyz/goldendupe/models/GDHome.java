package xyz.goldendupe.models;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record GDHome(@NotNull String name, @NotNull String world, @NotNull String uuid,
                     double x, double y, double z, float pitch, float yaw) {

    public GDHome(@NotNull String name, @NotNull String world, double x, double y, double z, float pitch, float yaw) {
        this(name, world, UUID.randomUUID().toString(), x, y, z, pitch, yaw);
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