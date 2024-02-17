package xyz.goldendupe.models;

import org.bukkit.entity.Player;

public class GDPlayer {
	private final Player player;
	private boolean isToggled = true;

	private GDSpawn teleportingSpawn;
	private long teleportSpawnCooldown;
	private GDChat chat;
	private boolean autoConfirmClearInv;

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
}
