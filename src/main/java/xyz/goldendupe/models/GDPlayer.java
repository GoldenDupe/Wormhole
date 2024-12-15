package xyz.goldendupe.models;

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
import xyz.goldendupe.utils.flaggable.Flag;
import xyz.goldendupe.utils.flaggable.FlagImpl;
import xyz.goldendupe.utils.flaggable.Flaggable;

import java.util.*;

@Getter
@Setter
public class GDPlayer implements Flaggable {
	@NotNull private final GoldenDupe goldenDupe;
	@NotNull private final UUID uniqueId;
	private GDSpawn teleportingSpawn;
	private GDChat chat;
	private boolean isToggleAutoConfirmClearInventory;
	private GDChatColor color = GDChatColor.DEFAULT;
	private boolean isToggleRandomItems = true;
	private boolean isToggleDropItem = false;
	private boolean isTogglePickupItem = false;
	private boolean isToggleNightVision = true;
	private boolean isTogglePotionBottles = false;
	private boolean isToggleSpeed = false;
	private long timesDuped;
	private long itemsDuped;
	private long generatedRandomItems;
	@Getter(AccessLevel.PUBLIC)
	private final Map<String, GDHome> homes = new HashMap<>();
	@NotNull
	private final Map<NamespacedKey, Flag<?>> flags = new HashMap<>();

	public GDPlayer(@NotNull GoldenDupe goldenDupe, @NotNull UUID player){
		this.goldenDupe = goldenDupe;
		this.uniqueId = player;
		this.chat = GDChat.GLOBAL;
		this.teleportingSpawn = null;
		this.isToggleAutoConfirmClearInventory = false;
	}

	public GDPlayer(@NotNull GoldenDupe goldenDupe, java.util.@NotNull UUID uniqueId, GDChat chat, GDChatColor color, List<GDHome> homes, long itemsDuped, long timesDuped, long generatedRandomItems, boolean isToggleAutoConfirmClearInventory, boolean isToggleRandomItems, boolean isToggleDropItem, boolean isTogglePickupItem, boolean isToggleNightVision, boolean isTogglePotionBottles, boolean isToggleSpeed) {
		this.goldenDupe = goldenDupe;
		this.uniqueId = uniqueId;
		this.chat = chat;
		if (!homes.isEmpty()) {
			homes.forEach(home -> {
				this.homes.put(home.getName().toLowerCase(), home);
			});
		}
		this.isToggleAutoConfirmClearInventory = isToggleAutoConfirmClearInventory;
		this.color = color;
		this.timesDuped = timesDuped;
		this.itemsDuped = itemsDuped;
		this.generatedRandomItems = generatedRandomItems;
		this.isToggleRandomItems = isToggleRandomItems;
		this.isToggleDropItem = isToggleDropItem;
		this.isTogglePickupItem = isTogglePickupItem;
		this.isToggleNightVision = isToggleNightVision;
		this.isTogglePotionBottles = isTogglePotionBottles;
		this.isToggleSpeed = isToggleSpeed;
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

	public GDChatColor color() {
		return color;
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


}
