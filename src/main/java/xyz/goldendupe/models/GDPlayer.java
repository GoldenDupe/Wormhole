package xyz.goldendupe.models;

import it.unimi.dsi.fastutil.chars.CharHeapSemiIndirectPriorityQueue;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.goldendupe.models.chatcolor.GDChatColor;
import xyz.goldendupe.utils.annotations.temporal.RequireSave;
import xyz.goldendupe.utils.flaggable.Flag;
import xyz.goldendupe.utils.flaggable.FlagImpl;
import xyz.goldendupe.utils.flaggable.Flaggable;
import xyz.goldendupe.utils.reference.PlayerReference;

import java.util.*;

@RequireSave
@SuppressWarnings({"UnusedReturnValue", "unused"})
public class GDPlayer implements Flaggable, PlayerReference {
	@NotNull private final UUID uniqueId;

	private GDSpawn teleportingSpawn;
	private GDChat chat;
	@RequireSave
	private boolean autoConfirmClearInv;
	@RequireSave
	private GDChatColor color = GDChatColor.DEFAULT;
	@RequireSave
	private boolean vanished;
	@RequireSave
	private boolean isToggled = true;
	@RequireSave
	private boolean isToggleDropItem = false;
	@RequireSave
	private boolean isTogglePickupItem = false;
	@RequireSave
	private boolean isToggleNightVision = true;
	@RequireSave
	private boolean isTogglePotionBottles = false;
	@RequireSave
	private boolean isToggleSpeed = false;
	@RequireSave
	@Getter(AccessLevel.PUBLIC) private final Map<String, GDHome> homes = new HashMap<>();
	@Getter
	private final Map<UUID, GDMessageGroup> messagegroups = new HashMap<>();
	@NotNull
	private final Map<NamespacedKey, Flag<?>> flags = new HashMap<>();

	public GDPlayer(@NotNull Player player){
		this.uniqueId = player.getUniqueId();
		this.chat = GDChat.GLOBAL;
		this.teleportingSpawn = null;
		this.autoConfirmClearInv = false;
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
	@Override
	public java.util.@NotNull UUID uuid() {
		return uniqueId;
	}
}
