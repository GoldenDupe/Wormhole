package xyz.goldendupe.models;

import com.google.gson.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.events.GDChatChangeEvent;
import xyz.goldendupe.models.chatcolor.GDChatColor;
import xyz.goldendupe.models.impl.GDHome;
import xyz.goldendupe.models.impl.GDSpawn;
import xyz.goldendupe.models.savable.Savable;
import xyz.goldendupe.models.serializer.ChatColorSerializer;
import xyz.goldendupe.models.serializer.HomeSerializer;
import xyz.goldendupe.utils.flaggable.Flag;
import xyz.goldendupe.utils.flaggable.FlagImpl;
import xyz.goldendupe.utils.flaggable.Flaggable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class GDPlayer implements Flaggable, Savable<UUID> {
	public static final Gson GSON = new GsonBuilder()
			.registerTypeAdapter(GDChatColor.class, new ChatColorSerializer())
			.registerTypeAdapter(GDHome.class, new HomeSerializer())
			.create();
	@NotNull private final GoldenDupe goldenDupe;
	@NotNull private final UUID uniqueId;
	private GDSpawn teleportingSpawn;
	private GDChat chat;
	private boolean autoConfirmClearInv;
	private GDChatColor color = GDChatColor.DEFAULT;
	private boolean vanished;
	private boolean isToggled = true;
	private boolean isToggleDropItem = false;
	private boolean isTogglePickupItem = false;
	private boolean isToggleNightVision = true;
	private boolean isTogglePotionBottles = false;
	private boolean isToggleSpeed = false;
	@Getter
	@Setter
	private int timesDuped;
	@Getter
	@Setter
	private int itemsDuped;
	@Getter
	@Setter
	private int generatedRandomItems;
	@Getter(AccessLevel.PUBLIC) private final Map<String, GDHome> homes = new HashMap<>();
	@Getter
	private final Map<UUID, GDMessageGroup> messagegroups = new HashMap<>();
	@NotNull
	private final Map<NamespacedKey, Flag<?>> flags = new HashMap<>();

	public GDPlayer(@NotNull GoldenDupe goldenDupe, @NotNull Player player){
		this.goldenDupe = goldenDupe;
		this.uniqueId = player.getUniqueId();
		this.chat = GDChat.GLOBAL;
		this.teleportingSpawn = null;
		this.autoConfirmClearInv = false;
	}

	public GDPlayer(@NotNull GoldenDupe goldenDupe, java.util.@NotNull UUID uniqueId, GDChat chat, GDChatColor color, List<GDHome> homes, int itemsDuped, int timesDuped, int generatedRandomItems, boolean autoConfirmClearInv, boolean vanished, boolean isToggled, boolean isToggleDropItem, boolean isTogglePickupItem, boolean isToggleNightVision, boolean isTogglePotionBottles, boolean isToggleSpeed) {
		this.goldenDupe = goldenDupe;
		this.uniqueId = uniqueId;
		this.chat = chat;
		if (!homes.isEmpty()) {
			homes.forEach(home -> {
				this.homes.put(home.getName().toLowerCase(), home);
			});
		}
		this.autoConfirmClearInv = autoConfirmClearInv;
		this.color = color;
		this.timesDuped = timesDuped;
		this.itemsDuped = itemsDuped;
		this.generatedRandomItems = generatedRandomItems;
		this.vanished = vanished;
		this.isToggled = isToggled;
		this.isToggleDropItem = isToggleDropItem;
		this.isTogglePickupItem = isTogglePickupItem;
		this.isToggleNightVision = isToggleNightVision;
		this.isTogglePotionBottles = isTogglePotionBottles;
		this.isToggleSpeed = isToggleSpeed;
	}

	public boolean isToggled() {
		return isToggled;
	}

	public GDPlayer setToggled(boolean toggled) {
		isToggled = toggled;
		return this;
	}


	public GDSpawn teleportingSpawn() {
		return teleportingSpawn;
	}

	public GDPlayer setTeleportingSpawn(GDSpawn teleportingSpawn) {
		this.teleportingSpawn = teleportingSpawn;
		return this;
	}

	public GDChat chat() {
		return chat;
	}

	public GDPlayer setChat(GDChat chat) {
		GDChatChangeEvent event = new GDChatChangeEvent(Bukkit.getPlayer(uuid()), this.chat, chat);
		event.callEvent();
		this.chat = chat;
		return this;
	}

	public boolean autoConfirmClearInv() {
		return autoConfirmClearInv;
	}

	public GDPlayer setAutoConfirmClearInv(boolean autoConfirmClearInv) {
		this.autoConfirmClearInv = autoConfirmClearInv;
		return this;
	}

	public GDChatColor color() {
		return color;
	}

	public GDPlayer setColor(GDChatColor color) {
		this.color = color;
		return this;
	}

	public boolean vanished() {
		return vanished;
	}

	public GDPlayer setVanished(boolean vanished) {
		this.vanished = vanished;
		return this;
	}

	public boolean isToggleDropItem() {
		return isToggleDropItem;
	}

	public GDPlayer setToggleDropItem(boolean toggleDropItem) {
		isToggleDropItem = toggleDropItem;
		return this;
	}

	public boolean isTogglePickupItem() {
		return isTogglePickupItem;
	}

	public GDPlayer setTogglePickupItem(boolean togglePickupItem) {
		isTogglePickupItem = togglePickupItem;
		return this;
	}

	public boolean isToggleNightVision() {
		return isToggleNightVision;
	}

	public GDPlayer setToggleNightVision(boolean toggleNightVision) {
		isToggleNightVision = toggleNightVision;
		return this;
	}

	public boolean isTogglePotionBottles() {
		return isTogglePotionBottles;
	}

	public GDPlayer setTogglePotionBottles(boolean togglePotionBottles) {
		isTogglePotionBottles = togglePotionBottles;
		return this;
	}

	public boolean isToggleSpeed() {
		return isToggleSpeed;
	}

	public GDPlayer setToggleSpeed(boolean toggleSpeed) {
		isToggleSpeed = toggleSpeed;
		return this;
	}

	public int getMaxHomes() {
		//noinspection DataFlowIssue
		return LuckPermsProvider.get()
				.getUserManager()
				.getUser(uniqueId)
				.getCachedData()
				.getMetaData()
				.getMetaValue("homes", Integer::parseInt)
				.orElse(3);
	}

	@Override
	public <V> void addFlag(@NotNull Flag<V> flag) {
		flags.put(flag.getKey(), flag);
	}

	@Override
	public <V> void editFlag(@NotNull NamespacedKey key, @Nullable V newValue) throws IllegalStateException {
		if (flags.get(key) != null){
			//noinspection unchecked
			Flag<V> flag = (Flag<V>) flags.get(key);
			assert newValue != null;
			flag.setValue(newValue);
			return;
		}
		throw new IllegalStateException("Couldn't edit a flag which is not set!");
	}

	@Override
	public <V> void setIfAbsent(@NotNull Flag<V> flag) {
		flags.putIfAbsent(flag.getKey(), flag);
	}

	@Override
	public <V> void setIfAbsent(@NotNull NamespacedKey key, @Nullable V defaultValue) {
		flags.putIfAbsent(key, new FlagImpl<>(key, defaultValue, defaultValue));
	}

	@Override
	public <V> void setIfAbsent(@NotNull NamespacedKey key, @Nullable V defaultValue, @Nullable V currentValue) {
		flags.putIfAbsent(key, new FlagImpl<>(key, defaultValue, currentValue));
	}

	@Override
	public @NotNull <V> Flag<V> getFlag(@NotNull NamespacedKey key, @NotNull Flag<V> defaultFlag) {
		return getFlag(key) != null ? Objects.requireNonNull(getFlag(key)) : defaultFlag;
	}

	@Override
	public @Nullable <V> Flag<V> getFlag(@NotNull NamespacedKey key) {
		//noinspection unchecked
		return (Flag<V>) flags.get(key);
	}
	public java.util.@NotNull UUID uuid() {
		return uniqueId;
	}

	public @NotNull GoldenDupe getGoldenDupe() {
		return goldenDupe;
	}


	public static PreparedStatement createTable(Connection connection) throws SQLException {
		return connection.prepareStatement("CREATE TABLE IF NOT EXISTS gd_players (uniqueId VARCHAR(36), " +
				"chat VARCHAR(15), " +
				"chatColor JSON, " +
				"clearInvConfirm BOOLEAN, " +
				"homes JSON, "+
				"timesDuped INTEGER, " +
				"itemsDuped INTEGER, " +
				"randomItems INTEGER, " +
				"vanished BOOLEAN," +
				"toggleItems BOOLEAN, " +
				"toggleDrop BOOLEAN, " +
				"togglePickup BOOLEAN, " +
				"toggleNightVision BOOLEAN, " +
				"toggleBottles BOOLEAN, " +
				"toggleSpeed BOOLEAN" +
				"PRIMARY KEY (uniqueId));"
		);
	}
	public static PreparedStatement fetch(Connection connection,  Object key) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("GET * FROM gd_players WHERE uniqueId = ?");
		statement.setString(1, key.toString());
		return statement;
	}
	public static GDPlayer load(ResultSet resultSet) throws SQLException {
		java.util.UUID uniqueId = java.util.UUID.fromString(resultSet.getString("uniqueId"));
		GDChat chat = GDChat.valueOf(resultSet.getString("chat"));
		GDChatColor color = GSON.fromJson(resultSet.getString("chatColor"), GDChatColor.class);
		List<GDHome> homes = JsonParser.parseString(resultSet.getString("homes")).getAsJsonArray().asList().stream()
				.filter(home->!home.isJsonNull())
				.map(home->GSON.fromJson(home, GDHome.class))
				.toList();

		int itemsDuped = resultSet.getInt("timesDuped");
		int timesDuped = resultSet.getInt("itemsDuped");
		int receivedRandomItems = resultSet.getInt("randomItems");

		boolean vanished = resultSet.getBoolean("vanished");
		boolean invConfirm = resultSet.getBoolean("clearInvConfirm");
		boolean toggleItems = resultSet.getBoolean("toggleItems");
		boolean toggleDrop = resultSet.getBoolean("toggleDrop");
		boolean togglePickup = resultSet.getBoolean("togglePickup");
		boolean toggleNightVision = resultSet.getBoolean("toggleNightVision");
		boolean toggleBottles = resultSet.getBoolean("toggleBottles");
		boolean toggleSpeed = resultSet.getBoolean("toggleSpeed");

		return new GDPlayer(
				GoldenDupe.instance(),
				uniqueId,
				chat,
				color,
				homes,
				itemsDuped,
				timesDuped,
				receivedRandomItems,
				invConfirm,
				vanished,
				toggleItems,
				toggleDrop,
				togglePickup,
				toggleNightVision,
				toggleBottles,
				toggleSpeed
		);
	}

	@Override
	public @NotNull PreparedStatement insertStatement(@NotNull Connection connection) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("INSERT INTO gd_players (" +
				"uniqueId," +
				"chat," +
				"chatColor," +
				"homes, "+
				"timesDuped, "+
				"itemsDuped, "+
				"randomItems, "+
				"clearInvConfirm," +
				"vanished," +
				"toggleItems," +
				"toggleDrop," +
				"togglePickup," +
				"toggleNightVision," +
				"toggleBottles, " +
				"toggleSpeed" +
				") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
		);
		statement.setString(1, uniqueId.toString());
		statement.setString(2, chat.name());
		statement.setString(3, GSON.toJson(color));
		statement.setString(4, GSON.toJson(homes.values().stream().toList()));
		statement.setInt(5, timesDuped);
		statement.setInt(6, itemsDuped);
		statement.setBoolean(7, autoConfirmClearInv);
		statement.setBoolean(8, vanished);
		statement.setBoolean(9, isToggled);
		statement.setBoolean(10, isToggleDropItem);
		statement.setBoolean(11, isTogglePickupItem);
		statement.setBoolean(12, isToggleNightVision);
		statement.setBoolean(13, isTogglePotionBottles);
		statement.setBoolean(14, isToggleSpeed);
		return statement;
	}

	@Override
	public PreparedStatement updateStatement(@NotNull Connection connection) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("UPDATE gd_players SET ("+
				"chat = ?, " +
				"chatColor = ?, " +
				"homes = ?, " +
				"itemsDuped = ?, "+
				"timesDuped = ?, "+
				"randomItems = ?, "+
				"clearInvConfirm = ?, " +
				"vanished = ?, " +
				"toggleItems = ?, " +
				"toggleDrop = ?, " +
				"togglePickup = ?, " +
				"toggleNightVision = ?, " +
				"toggleBottles = ?, " +
				"toggleSpeed = ?" +
				") WHERE uniqueId = ?");

		statement.setString(1, chat.name());
		statement.setString(2, GSON.toJson(color));
		statement.setString(3, GSON.toJson(homes.values().stream().toList()));
		statement.setInt(4, timesDuped);
		statement.setInt(5, itemsDuped);
		statement.setBoolean(6, autoConfirmClearInv);
		statement.setBoolean(7, vanished);
		statement.setBoolean(8, isToggled);
		statement.setBoolean(9, isToggleDropItem);
		statement.setBoolean(10, isTogglePickupItem);
		statement.setBoolean(11, isToggleNightVision);
		statement.setBoolean(12, isTogglePotionBottles);
		statement.setBoolean(13, isToggleSpeed);
		statement.setString(14, uniqueId.toString());
		return statement;
	}

	@Override
	public PreparedStatement getStatement(@NotNull Connection connection, java.util.UUID uuid) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("SELECT * FROM gd_players WHERE uniqueId = ?");
		statement.setString(1, uuid.toString());
		return statement;
	}
}
