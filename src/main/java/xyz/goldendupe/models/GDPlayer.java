package xyz.goldendupe.models;

import it.unimi.dsi.fastutil.chars.CharHeapSemiIndirectPriorityQueue;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.models.chatcolor.GDChatColor;
import xyz.goldendupe.utils.annotations.temporal.RequireSave;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@RequireSave
@SuppressWarnings({"UnusedReturnValue", "unused"})
public class GDPlayer {
	@NotNull private final Player player;

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

	public GDPlayer(@NotNull Player player){
		this.player = player;
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

	public Player player() {
		return player;
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
				.getUser(player.getUniqueId())
				.getCachedData()
				.getMetaData()
				.getMetaValue("homes", Integer::parseInt)
				.orElse(3);
	}

}
