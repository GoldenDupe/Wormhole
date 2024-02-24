package xyz.goldendupe.listeners;

import io.papermc.paper.event.player.AsyncChatDecorateEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.models.chatcolor.Color;
import xyz.goldendupe.models.chatcolor.GDChatColor;
import xyz.goldendupe.models.GDPlayer;
import xyz.goldendupe.utils.MemberType;

import java.util.HashMap;
import java.util.Map;

public class ChatColorListener implements GDListener {
	private static final Map<String, String> legacyToMini = new HashMap<>();
	private final GoldenDupe goldenDupe;
	@SuppressWarnings("UnnecessaryUnicodeEscape")
	private ChatColorListener(GoldenDupe goldenDupe){
		this.goldenDupe = goldenDupe;
		legacyToMini.put("&0", "<black>");
		legacyToMini.put("&1", "<dark_blue>");
		legacyToMini.put("&2", "<dark_aqua>");
		legacyToMini.put("&3", "<dark_green>");
		legacyToMini.put("&4", "<dark_red>");
		legacyToMini.put("&5", "<dark_purple>");
		legacyToMini.put("&6", "<gold>");
		legacyToMini.put("&7", "<gray>");
		legacyToMini.put("&8", "<dark_gray>");
		legacyToMini.put("&9", "<blue>");
		legacyToMini.put("&a", "<green>");
		legacyToMini.put("&b", "<aqua>");
		legacyToMini.put("&c", "<red>");
		legacyToMini.put("&d", "<light_purple>");
		legacyToMini.put("&f", "<white>");
		legacyToMini.put("&e", "<yellow>");
		legacyToMini.put("&r", "<reset>");
		legacyToMini.put("&m", "<strikethrough>");
		legacyToMini.put("&n", "<underlined>");
		legacyToMini.put("&o", "<italic>");
		legacyToMini.put("&l", "<bold>");
	}

	public static Component displayColorString(GDPlayer gdPlayer, Player player, String message){
		GDChatColor chatColor = gdPlayer.color();

		String permission = "chatcolor-codes";
		if (player.hasPermission(MemberType.ADMINISTRATOR.permissionOf(permission))) {
			String[] replacement = new String[]{message};
			legacyToMini.forEach((key, val) -> replacement[0] = replacement[0].replaceAll("(?i)" + key, val));
			message = replacement[0];
		}
		String format =
				(chatColor.italic() ? "<italic>" : "") +
				(chatColor.underlined() ? "<underlined>" : "") +
				(chatColor.strikethrough() ? "<strikethrough>" : "") +
				(chatColor.bold() ? "<bold>" : "")
				;
		switch (chatColor.mode()) {
			case SINGLE -> {
				String reset = "<color:" + chatColor.colors().get(0).asHex() + ">"+format;
				if (player.hasPermission(MemberType.ADMINISTRATOR.permissionOf(permission))) {
					message = message.replace("<reset>", "<reset>" + reset);
				}
				return MiniMessage.miniMessage().deserialize(reset + message);
			}
			case RAINBOW -> {
				String reset = "<rainbow:" + (chatColor.rainbowReversed() ? "!" : "") + (chatColor.rainbowMode() != -1 ? chatColor.rainbowMode() : "") + ">"+format;
				if (player.hasPermission(MemberType.ADMINISTRATOR.permissionOf(permission))) {
					message = message.replace("<reset>", "<reset>" + reset);
				}
				return MiniMessage.miniMessage().deserialize(reset + message);
			}

			case GRADIENT -> {
				StringBuilder reset = new StringBuilder("<gradient:");
				if (chatColor.colors().size()>1) {
					for (Color color : chatColor.colors().values()) {
						if (color == null) {
							continue;
						}
						if (!reset.toString().equalsIgnoreCase("<gradient:")) {
							reset.append(":");
						}
						reset.append(color.asHex());
					}
					if (chatColor.colors().size() == 1) {
						reset.append(":").append(reset.toString().split(":")[1]);
					}
					reset.append(">").append(format);
				} else {
					reset = new StringBuilder("<color:#"+chatColor.colors().get(0).asHex()+">").append(format);
				}
				if (player.hasPermission(MemberType.ADMINISTRATOR.permissionOf(permission))) {
					message = message.replace("<reset>", "<reset>" + reset);
				}
				return MiniMessage.miniMessage().deserialize(reset+message);
			}
			case NONE -> {
				String reset = "<color:" + GDChatColor.DEFAULT.colors().get(0).asHex() + ">"+format;
				if (player.hasPermission(MemberType.ADMINISTRATOR.permissionOf(permission))) {
					message = message.replace("<reset>", "<reset>" + reset);
				}
				return MiniMessage.miniMessage().deserialize(reset + message);
			}
		}
		return MiniMessage.miniMessage().deserialize(message);
	}

	@EventHandler
	public void onChat(AsyncChatDecorateEvent event) {
		Component message = event.result();

		MiniMessage miniMessage = MiniMessage.miniMessage();
		String serialized = miniMessage.serialize(message);
		serialized = miniMessage.stripTags(serialized);


		Player player = event.player();
		GDPlayer goldenPlayer = goldenDupe.playerDatabase().fromPlayer(event.player());

		event.result(displayColorString(goldenPlayer, player, serialized));
	}

	@Override
	public GoldenDupe goldenDupe() {
		return goldenDupe;
	}
}
