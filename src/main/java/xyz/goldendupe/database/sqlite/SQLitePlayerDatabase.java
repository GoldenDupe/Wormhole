package xyz.goldendupe.database.sqlite;

import bet.astral.data.helper.PackedPreparedStatement;
import bet.astral.data.helper.PackedResultSet;
import net.kyori.adventure.text.Component;
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
            goldenDupe.getSLF4JLogger().error("Error while trying to create players table!", e);
        }
    }
    @Override
    public CompletableFuture<GDPlayer> load(Player player) {
        return CompletableFuture.supplyAsync(()-> {
            try {
                List<GDHome> homes = homeDatabase.loadHomes(player);
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE uniqueId = ?");
                PackedPreparedStatement preparedStatement = new PackedPreparedStatement(statement);
                preparedStatement.setUUID(1, player.getUniqueId());
                PackedResultSet resultSet = new PackedResultSet(
                        preparedStatement.executeQuery());

                if (!resultSet.isClosed() && resultSet.next()) {
                    return new GDPlayer(
                            goldenDupe,
                            player.getUniqueId(),
                            resultSet.getEnum("chatMode", GDChat.class),
                            resultSet.getJson("chatFormat", GDChatColor.class),
                            homes,
                            resultSet.getLong("itemsDuped"),
                            resultSet.getLong("timesDuped"),
                            resultSet.getLong("itemsGenerated"),
                            resultSet.getBoolean("toggleAutoClearInventory"),
                            resultSet.getBoolean("toggleRandomItem"),
                            resultSet.getBoolean("toggleDrop"),
                            resultSet.getBoolean("togglePickup"),
                            resultSet.getBoolean("toggleNightVision"),
                            resultSet.getBoolean("toggleBottles"),
                            resultSet.getBoolean("toggleSpeed")
                    );
                }

                if (!resultSet.isClosed()){
                    resultSet.close();
                }
                preparedStatement.close();

                return new GDPlayer(goldenDupe, player.getUniqueId());
            } catch (SQLException e) {
                goldenDupe.getSLF4JLogger().error("Error while trying to load " + player.getUniqueId(), e);
                if (Bukkit.getPlayer(player.getUniqueId()) != null) {
                    Bukkit.getPlayer(player.getUniqueId()).kick(Component.text("INTERNAL ERROR RETRY TO JOIN AND CREATE A TICKET! " + GoldenDupe.DISCORD));
                }
                return null;
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
                                    "toggleRandomItem = ?, toggleDrop = ?, togglePickup = ?, toggleNightVision = ?, toggleBottles = ?, toggleSpeed = ? WHERE uniqueId = ?"),
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
                                    "itemsGenerated, toggleAutoClearInventory, toggleRandomItem, toggleDrop, togglePickup," +
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
                goldenDupe.getSLF4JLogger().error("Error while trying to save "+player.getUniqueId(), e);
                if (Bukkit.getPlayer(player.getUniqueId())!= null){
                    Bukkit.getPlayer(player.getUniqueId()).kick(Component.text("INTERNAL ERROR RETRY TO JOIN AND CREATE A TICKET! "+ GoldenDupe.DISCORD));
                }
            }
        });
    }

    @Override
    public void saveHome(GDPlayer player, GDHome home) {
        homeDatabase.saveHome(player, home);
    }

    @Override
    public void deleteHome(GDPlayer player, GDHome home){
        homeDatabase.deleteHome(player, home.getName());
    }
    @Override
    public void deleteHome(GDPlayer player, String home){
        homeDatabase.deleteHome(player, home);
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
                goldenDupe.getSLF4JLogger().error("Error while trying to create table", e);
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
                            resultSet.getUUID("uniqueId"),
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
                statement.close();
                return homes;
            } catch (SQLException e) {
                goldenDupe.getSLF4JLogger().error("Error while trying to load homes of "+player.getUniqueId(), e);
                player.kick(Component.text("INTERNAL ERROR RETRY TO JOIN AND CREATE A TICKET! "+ GoldenDupe.DISCORD));
                return null;
            }
        }

        public void deleteHome(GDPlayer player, String name){
            try {
                PackedPreparedStatement statement = new PackedPreparedStatement(
                        connection.prepareStatement("DELETE FROM homes WHERE name = ? AND ownerId = ?")
                );
                statement.setString(1, name);
                statement.setUUID(2, player.getUniqueId());
                statement.executeUpdate();
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        public void saveHome(GDPlayer player, GDHome home){
            try {
                PackedPreparedStatement getStatement = new PackedPreparedStatement(
                        connection.prepareStatement("SELECT * FROM homes WHERE uniqueId = ?")
                );
                getStatement.setUUID(1, home.getUniqueId());
                ResultSet set = getStatement.executeQuery();
                if (set == null || !set.next()) {
                    PackedPreparedStatement insertStatement = new PackedPreparedStatement(
                            connection.prepareStatement("INSERT INTO homes (ownerId, uniqueId, name, x, y, z, yaw, world) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"));
                    insertStatement.setUUID(1, player.getUniqueId());
                    insertStatement.setUUID(2, home.getUniqueId());
                    insertStatement.setString(3, home.getName());
                    insertStatement.setDouble(4, home.getX());
                    insertStatement.setDouble(5, home.getY());
                    insertStatement.setDouble(6, home.getZ());
                    insertStatement.setFloat(7, home.getYaw());
                    insertStatement.setUUID(8, home.getWorld().getUID());
                    insertStatement.executeUpdate();
                    insertStatement.close();
                } else {
                    PackedPreparedStatement updateStatement = new PackedPreparedStatement(
                            connection.prepareStatement("UPDATE homes ownerId = ?, name = ?, x = ?, y = ?, z = ?, yaw = ?, world = ? where uniqueId = ?"));
                    updateStatement.setUUID(1, player.getUniqueId());
                    updateStatement.setString(2, home.getName());
                    updateStatement.setDouble(3, home.getX());
                    updateStatement.setDouble(4, home.getY());
                    updateStatement.setDouble(5, home.getZ());
                    updateStatement.setFloat(6, home.getYaw());
                    updateStatement.setUUID(7, home.getWorld().getUID());
                    updateStatement.setUUID(8, home.getUniqueId());
                    updateStatement.executeUpdate();
                    updateStatement.close();
                }

                if (set != null && !set.isClosed()){
                    set.close();
                }
                getStatement.close();

            } catch (SQLException e) {
                goldenDupe.getSLF4JLogger().error("Error while trying to save home of "+player.getUniqueId(), e);
                Bukkit.getPlayer(player.getUniqueId()).kick(Component.text("INTERNAL ERROR RETRY TO JOIN AND CREATE A TICKET! "+ GoldenDupe.DISCORD));
            }
        }
    }
}
