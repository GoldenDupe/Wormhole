package xyz.goldendupe.datagen.defaults;

import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.models.GDChat;
import xyz.goldendupe.models.GDPlayer;
import xyz.goldendupe.models.chatcolor.GDChatColor;

import java.util.UUID;

public class PlayerDefault extends GDPlayer {
	public static final PlayerDefault INSTANCE = new PlayerDefault(UUID.randomUUID());
	public PlayerDefault(UUID uniqueId) {
		super(GoldenDupe.instance(), uniqueId);
	}

	@Override
	public GDChat getChat() {
		return GDChat.GLOBAL;
	}

	@Override
	public boolean isToggleAutoConfirmClearInventory() {
		return false;
	}

	@Override
	public GDChatColor getColor() {
		return GDChatColor.DEFAULT;
	}

	@Override
	public boolean isToggleRandomItems() {
		return true;
	}

	@Override
	public boolean isToggleDropItem() {
		return false;
	}

	@Override
	public boolean isTogglePickupItem() {
		return false;
	}

	@Override
	public boolean isToggleNightVision() {
		return true;
	}

	@Override
	public boolean isTogglePotionBottles() {
		return false;
	}

	@Override
	public boolean isToggleSpeed() {
		return false;
	}

	@Override
	public long getTimesDuped() {
		return 0;
	}

	@Override
	public long getItemsDuped() {
		return 0;
	}

	@Override
	public long getGeneratedRandomItems() {
		return 0;
	}

	@Override
	public GDChat chat() {
		return GDChat.GLOBAL;
	}

	@Override
	public GDChatColor color() {
		return super.color();
	}
}
