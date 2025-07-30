package bet.astral.wormhole.managers.data.sql;

import bet.astral.wormhole.managers.data.PlayerDataManager;
import bet.astral.wormhole.objects.data.PlayerData;
import bet.astral.wormhole.plugin.WormholePlugin;

import java.sql.*;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class SQLitePlayerDataManager implements PlayerDataManager {
    private final WormholePlugin wormhole;
    private final SQLDatabase database;
    public SQLitePlayerDataManager(WormholePlugin wormhole, SQLDatabase database) {
        this.wormhole = wormhole;
        this.database = database;
        try {
            createTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS player_data (" +
                "  unique_id TEXT PRIMARY KEY," +
                "  extra_homes INTEGER," +
                "  extra_warps INTEGER," +
                "  primary_home TEXT" +
                ");";
        try (Statement statement = database.getConnection().createStatement()) {
            statement.execute(sql);
        }
    }
    @Override
    public CompletableFuture<Void> load(PlayerData playerData) {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = database.getConnection()) {
                try (PreparedStatement stmt = connection.prepareStatement(
                        "SELECT extra_homes, extra_warps, primary_home FROM player_data WHERE unique_id = ?")) {
                    stmt.setString(1, playerData.getUniqueId().toString());

                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            playerData.setExtraHomes(rs.getInt("extra_homes"));
                            playerData.setExtraWarps(rs.getInt("extra_warps"));

                            String primaryHomeStr = rs.getString("primary_home");
                            playerData.setPrimaryHome(primaryHomeStr == null ? null : UUID.fromString(primaryHomeStr));
                        }
                    }
                }
            } catch (SQLException e) {
                wormhole.getLogger().severe(e.getMessage());
            }
        });
    }

    @Override
    public CompletableFuture<Void> save(PlayerData playerData) {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = database.getConnection()) {
                try (PreparedStatement stmt = connection.prepareStatement(
                        "INSERT INTO player_data (unique_id, extra_homes, extra_warps, primary_home) " +
                                "VALUES (?, ?, ?, ?) " +
                                "ON CONFLICT(unique_id) DO UPDATE SET extra_homes=excluded.extra_homes, extra_warps=excluded.extra_warps, primary_home=excluded.primary_home")) {
                    stmt.setString(1, playerData.getUniqueId().toString());
                    stmt.setInt(2, playerData.getExtraHomes());
                    stmt.setInt(3, playerData.getExtraWarps());
                    stmt.setString(4, playerData.getPrimaryHome() == null ? null : playerData.getPrimaryHome().toString());
                    stmt.executeUpdate();
                }
            } catch (SQLException e) {
                wormhole.getLogger().severe(e.getMessage());
            }
        });
    }

}
