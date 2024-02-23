package xyz.goldendupe.models;

import org.bukkit.entity.Player;
import xyz.goldendupe.models.chatcolor.GDChatColor;

public class GDPlayer {
	private final Player player;

	private GDSpawn teleportingSpawn;
	private long teleportSpawnCooldown;
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

	public GDPlayer(Player player){
		this.player = player;
		this.chat = GDChat.GLOBAL;
		this.teleportingSpawn = null;
		this.teleportSpawnCooldown = 0;
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

	public long teleportSpawnCooldown() {
		return teleportSpawnCooldown;
	}

	public GDPlayer setTeleportSpawnCooldown(long teleportSpawnCooldown) {
		this.teleportSpawnCooldown = teleportSpawnCooldown;
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
}
