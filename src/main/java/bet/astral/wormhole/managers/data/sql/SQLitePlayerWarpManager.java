package bet.astral.wormhole.managers.data.sql;

import bet.astral.wormhole.managers.data.PlayerWarpDataManager;
import bet.astral.wormhole.objects.PlayerHome;
import bet.astral.wormhole.objects.PlayerWarp;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class SQLitePlayerWarpManager implements PlayerWarpDataManager<PlayerHome> {
    private SQLDatabase database;

    public SQLitePlayerWarpManager(SQLDatabase database) {
        this.database = database;
    }

    public void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS homes (" +
                "uniqueId VARCHAR(36) PRIMARY KEY, " +
                "name VARCHAR(50), " +
                "description VARCHAR(1000), " +
                "created BIGINT, " +
                "ownerId VARCHAR(36), " +
                "worldId VARCHAR(36), " +
                "worldName VARCHAR(50), " +
                "x REAL, " +
                "y REAL, " +
                "z REAL, " +
                "yaw REAL, " +
                "pitch REAL, " +
                "isPublic BOOLEAN, " +
                "allowWarpingWhileOffline BOOLEAN, " +
                "requireTeleportAccept BOOLEAN" +
                ")";

        try (Statement statement = database.getConnection().createStatement()) {
            statement.execute(sql);
        }
    }


    @Override
    public CompletableFuture<Void> saveWarp(PlayerHome warp) {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = database.getConnection()) {
                boolean exists = false;

                try (PreparedStatement checkStmt = connection.prepareStatement(
                        "SELECT 1 FROM homes WHERE uniqueId = ?")) {
                    checkStmt.setString(1, warp.getUniqueId().toString());
                    try (ResultSet rs = checkStmt.executeQuery()) {
                        exists = rs.next();
                    }
                }

                String sql;
                if (exists) {
                    sql = "UPDATE homes SET " +
                            "name = ?, description = ?, created = ?, ownerId = ?, " +
                            "worldId = ?, worldName = ?, x = ?, y = ?, z = ?, yaw = ?, pitch = ?, " +
                            "isPublic = ?, allowWarpingWhileOffline = ?, requireTeleportAccept = ?, isWarp = ? " +
                            "WHERE uniqueId = ?";
                } else {
                    sql = "INSERT INTO homes (" +
                            "uniqueId, name, description, created, ownerId, " +
                            "worldId, worldName, x, y, z, yaw, pitch, " +
                            "isPublic, allowWarpingWhileOffline, requireTeleportAccept, isWarp) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                }

                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    boolean isInsert = !exists;
                    int i = 1;

                    if (isInsert) {
                        stmt.setString(i++, warp.getUniqueId().toString());
                    }

                    stmt.setString(i++, warp.getName());
                    stmt.setString(i++, GsonComponentSerializer.gson().serialize(warp.getDescription()));
                    stmt.setLong(i++, warp.getCreated());
                    stmt.setString(i++, warp.getOwnerId().toString());
                    stmt.setString(i++, warp.getWorldId().toString());
                    stmt.setString(i++, warp.getWorldName());
                    stmt.setDouble(i++, warp.getX());
                    stmt.setDouble(i++, warp.getY());
                    stmt.setDouble(i++, warp.getZ());
                    stmt.setDouble(i++, warp.getYaw());
                    stmt.setDouble(i++, warp.getPitch());
                    stmt.setBoolean(i++, warp instanceof PlayerWarp);

                    boolean isPublic = false;
                    boolean allowWarpingOffline = false;
                    boolean requireTeleportAccept = true;

                    if (warp instanceof PlayerWarp playerWarp) {
                        isPublic = playerWarp.isPublic();
                        allowWarpingOffline = playerWarp.isAllowWarpingIfOwnerOffline();
                        requireTeleportAccept = playerWarp.isRequireTeleportAccept();
                    }

                    stmt.setBoolean(i++, isPublic);
                    stmt.setBoolean(i++, allowWarpingOffline);
                    stmt.setBoolean(i++, requireTeleportAccept);

                    if (!isInsert) {
                        stmt.setString(i, warp.getUniqueId().toString());
                    }

                    stmt.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }


    @Override
    public CompletableFuture<Void> deleteWarp(PlayerHome warp) {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = database.getConnection();
                 PreparedStatement stmt = connection.prepareStatement(
                         "DELETE FROM homes WHERE uniqueId = ?")) {
                stmt.setString(1, warp.getUniqueId().toString());
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public CompletableFuture<PlayerHome> getWarp(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = database.getConnection();
                 PreparedStatement stmt = connection.prepareStatement(
                         "SELECT * FROM homes WHERE uniqueId = ?")) {
                stmt.setString(1, uuid.toString());

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return parseWarp(rs);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    @Override
    public @NotNull CompletableFuture<@NotNull List<PlayerHome>> getWarps(UUID ownerId) {
        return CompletableFuture.supplyAsync(() -> {
            List<PlayerHome> warps = new ArrayList<>();
            try (Connection connection = database.getConnection();
                 PreparedStatement stmt = connection.prepareStatement(
                         "SELECT * FROM homes WHERE ownerId = ?")) {
                stmt.setString(1, ownerId.toString());

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        PlayerHome warp = parseWarp(rs);
                        warps.add(warp);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return warps;
        });
    }

    private PlayerHome parseWarp(ResultSet rs) throws SQLException {
        Component description = GsonComponentSerializer.gson().deserialize(
                rs.getString("description"));
        PlayerHome warp = null;
        if (!rs.getBoolean("isWarp")) {
            warp = new PlayerHome(
                    UUID.fromString(rs.getString("ownerId")),
                    UUID.fromString(rs.getString("uniqueId")),
                    rs.getString("name"),
                    rs.getLong("created"),
                    UUID.fromString(rs.getString("worldId")),
                    rs.getString("worldName"),
                    rs.getDouble("x"),
                    rs.getDouble("y"),
                    rs.getDouble("z"),
                    rs.getFloat("yaw"),
                    rs.getFloat("pitch"));
            warp.setDescription(description);
        } else {
            warp = new PlayerWarp(
                    UUID.fromString(rs.getString("ownerId")),
                    UUID.fromString(rs.getString("uniqueId")),
                    rs.getString("name"),
                    rs.getLong("created"),
                    UUID.fromString(rs.getString("worldId")),
                    rs.getString("worldName"),
                    rs.getDouble("x"),
                    rs.getDouble("y"),
                    rs.getDouble("z"),
                    rs.getFloat("yaw"),
                    rs.getFloat("pitch"),
                    rs.getBoolean("isPublic"),
                    rs.getBoolean("allowWarpingWhileOffline"),
                    rs.getBoolean("requireTeleportAccept")
            );
            warp.setDescription(description);
        }
        return warp;
    }
}
