package xyz.goldendupe.utils.impl;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.utils.Position;
import xyz.goldendupe.utils.annotations.temporal.RequireSave;

@RequireSave
public class SpawnPosition extends Position {

    @NotNull
    @Getter
    public String permission = "";

    public SpawnPosition(String name, @NotNull String world, double x, double y, double z, float yaw) {
        super(name, null, x, y, z, yaw, world);
    }

    public SpawnPosition(String name, @NotNull String world, @NotNull String permission, double x, double y, double z, float yaw) {
        super(name, null, x, y, z, yaw, world);
        this.permission = permission;
    }

}
