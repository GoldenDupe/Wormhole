package xyz.goldendupe.database.sqlite;

import bet.astral.data.helper.PackedPreparedStatement;
import bet.astral.data.helper.PackedResultSet;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.database.PlayerDatabase;
import xyz.goldendupe.models.GDChat;
import xyz.goldendupe.models.GDPlayer;
import xyz.goldendupe.models.chatcolor.GDChatColor;
import xyz.goldendupe.models.impl.GDHome;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SQLitePlayerDatabase extends PlayerDatabase {
    private Connection connection;
    private HomeDatabase homeDatabase;
    public SQLitePlayerDatabase(GoldenDupe goldenDupe) {
        super(goldenDupe);
        connection = connect();
        createTable();
        homeDatabase = new HomeDatabase(goldenDupe, connection);
    }

    private Connection connect() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:"+new File(goldenDupe.getDataFolder(), "users.db"));
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void createTable() {
        try {
            PreparedStatement statement = connection.
                    prepareStatement("CREATE TABLE IF NOT EXISTS users (uniqueId VARCHAR(36), " +
                            "chatMode VARCHAR(15), " +
                            "chatFormat TEXT, " +
                            "itemsGenerated INTEGER, " +
                            "itemsDuped INTEGER, " +
                            "timesDuped INTEGER, " +
                            "toggleRandomItem BOOLEAN, " +
                            "toggleDrop BOOLEAN, " +
                            "togglePickup BOOLEAN, " +
                            "toggleBottles BOOLEAN, " +
                            "toggleSpeed BOOLEAN, " +
                            "toggleNightVision BOOLEAN, " +
                            "toggleAutoClearInventory BOOLEAN)");
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public CompletableFuture<GDPlayer> load(Player player) {
        return CompletableFuture.supplyAsync(()->{
            try {
                PackedPreparedStatement preparedStatement = new PackedPreparedStatement(
                        connection.prepareStatement("SELECT * FROM users WHERE uniqueId = ?"));
                preparedStatement.setUUID(1, player.getUniqueId());
                PackedResultSet resultSet = new PackedResultSet(
                        preparedStatement.executeQuery());

                if (!resultSet.isClosed() && resultSet.next()){
                    return new GDPlayer(
                            goldenDupe,
                            player.getUniqueId(),
                            resultSet.getEnum("chat_mode", GDChat.class),
                            resultSet.getJson("chatFormat", GDChatColor.class),
                            homeDatabase.loadHomes(player),
                            resultSet.getLong("itemsDuped"),
                            resultSet.getLong("timesDuped"),
                            resultSet.getLong("itemsGenerated"),
                            resultSet.getBoolean("toggleAutoClearInventory"),
                            resultSet.getBoolean("toggleRandomItems"),
                            resultSet.getBoolean("toggleDrop"),
                            resultSet.getBoolean("togglePickup"),
                            resultSet.getBoolean("toggleNightVision"),
                            resultSet.getBoolean("toggleBottles"),
                            resultSet.getBoolean("toggleSpeed")
                    );
                }
                return new GDPlayer(goldenDupe, player.getUniqueId());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public CompletableFuture<Void> save(GDPlayer player) {
        return CompletableFuture.runAsync(()->{
            try {
                PackedPreparedStatement getStatement = new PackedPreparedStatement(connection.prepareStatement("SELECT * FROM users WHERE uniqueId = ?"));
                ResultSet resultSet = getStatement.executeQuery();
                if (resultSet != null && resultSet.next()){
                    PackedPreparedStatement updateStatement = new PackedPreparedStatement(
                            connection.prepareStatement("UPDATE users SET chatMode = ?, chatFormat = ?, itemsDuped = ?, timesDuped = ?, itemsGenerated = ?, toggleAutoClearInventory = ?," +
                                    "toggleRandomItems = ?, toggleDrop = ?, togglePickup = ?, toggleNightVision = ?, toggleBottles = ?, toggleSpeed = ? WHERE uniqueId = ?"),
                            getGson()
                    );
                    updateStatement.setEnum(1, player.getChat());
                    updateStatement.setJson(2, player.color(), GDChatColor.class);
                    updateStatement.setLong(3, player.getItemsDuped());
                    updateStatement.setLong(4, player.getTimesDuped());
                    updateStatement.setLong(5, player.getGeneratedRandomItems());
                    updateStatement.setBoolean(6, player.isToggleAutoConfirmClearInventory());
                    updateStatement.setBoolean(7, player.isToggleRandomItems());
                    updateStatement.setBoolean(8, player.isToggleDropItem());
                    updateStatement.setBoolean(9, player.isTogglePickupItem());
                    updateStatement.setBoolean(10, player.isTogglePotionBottles());
                    updateStatement.setBoolean(11, player.isToggleSpeed());
                    updateStatement.setUUID(12, player.getUniqueId());
                    updateStatement.executeUpdate();
                    updateStatement.close();
                } else {
                    PackedPreparedStatement insertStatement = new PackedPreparedStatement(
                            connection.prepareStatement("INSERT INTO users (uniqueId, chatMode, chatFormat, itemsDuped, timesDuped, " +
                                    "itemsGenerated, toggleAutoClearInventory, toggleRandomItems, toggleDrop, togglePickup," +
                                    "toggleNightVision, toggleBottles, toggleSpeed) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")
                    );
                    insertStatement.setUUID(1, player.getUniqueId());
                    insertStatement.setEnum(2, player.getChat());
                    insertStatement.setJson(3, player.color(), GDChatColor.class);
                    insertStatement.setLong(4, player.getItemsDuped());
                    insertStatement.setLong(5, player.getTimesDuped());
                    insertStatement.setLong(6, player.getGeneratedRandomItems());
                    insertStatement.setBoolean(7, player.isToggleAutoConfirmClearInventory());
                    insertStatement.setBoolean(8, player.isToggleRandomItems());
                    insertStatement.setBoolean(9, player.isToggleDropItem());
                    insertStatement.setBoolean(10, player.isTogglePickupItem());
                    insertStatement.setBoolean(11, player.isTogglePotionBottles());
                    insertStatement.setBoolean(12, player.isToggleSpeed());
                    insertStatement.executeUpdate();
                    insertStatement.close();
                }

                if (resultSet != null && !resultSet.isClosed()){
                    resultSet.close();
                }
                if (!getStatement.isClosed()){
                    getStatement.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static class HomeDatabase {
        GoldenDupe goldenDupe;
        Connection connection;

        public HomeDatabase(GoldenDupe goldenDupe, Connection connection) {
            this.goldenDupe = goldenDupe;
            this.connection = connection;
            createTable();
        }

        public void createTable(){
            try {
                PreparedStatement statement = connection
                        .prepareStatement("CREATE TABLE IF NOT EXISTS homes (ownerId VARCHAR(36), " +
                                "uniqueId VARCHAR(36), " +
                                "name VARCHAR(20), " +
                                "world VARCHAR(36)," +
                                "x DOUBLE," +
                                "y DOUBLE," +
                                "z DOUBLE," +
                                "yaw DOUBLE);");
                statement.execute();
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        public List<GDHome> loadHomes(Player player){
            try {
                PackedPreparedStatement statement =
                        new PackedPreparedStatement(connection.prepareStatement("SELECT * FROM homes WHERE ownerid = ?"));
                statement.setUUID(1, player.getUniqueId());


                PackedResultSet resultSet = new PackedResultSet(
                        statement.executeQuery());

                List<GDHome> homes = new ArrayList<>();
                while (!resultSet.isClosed() && resultSet.next()){
                    GDHome home = new GDHome(
                            resultSet.getString("name"),
                            resultSet.getDouble("x"),
                            resultSet.getDouble("y"),
                            resultSet.getDouble("z"),
                            resultSet.getFloat("yaw"),
                            Bukkit.getWorld(resultSet.getUUID("world"))
                    );
                    homes.add(home);
                }

                if (!resultSet.isClosed()){
                    resultSet.close();
                }
                connection.close();
                return homes;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
