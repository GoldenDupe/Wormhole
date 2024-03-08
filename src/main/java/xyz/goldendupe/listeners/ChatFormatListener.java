package xyz.goldendupe.listeners;

import bet.astral.messenger.Message;
import bet.astral.messenger.placeholder.PlaceholderList;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.refrence.FactionReferenceImpl;
import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.messenger.GoldenMessenger;
import xyz.goldendupe.models.GDChat;
import xyz.goldendupe.models.GDPlayer;
import xyz.goldendupe.models.chatcolor.Color;

public class ChatFormatListener implements GDListener {
	private static GoldenDupe goldenDupe;

	public static @NotNull Component format(Player player, Component component){
		return format(player, player, GDChat.GLOBAL, component);
	}
	public static @NotNull Component format(Player player, Audience whoSees, GDChat chat, Component message) {
		Component name = GoldenMessenger.prefixNameSuffix(player);

		Component format = Component.empty();
		GDPlayer gdPlayer = goldenDupe.playerDatabase().fromPlayer(player);
		if (whoSees instanceof Player oPlayer) {
			if (gdPlayer.vanished() && oPlayer.canSee(player)) {
				format = format.append(Component.text("V", NamedTextColor.DARK_RED).decoration(TextDecoration.BOLD, true).appendSpace());
			}
		}

		if (chat.asMessageChannel() != null) {
			Message msg = goldenDupe.messenger().getMessage(chat.name().toLowerCase() + "chat.chat-message");
			if (msg != null) {
				PlaceholderList placeholders = new PlaceholderList(goldenDupe.messenger().createPlaceholders(player));
				placeholders.add("message", message);

				format = goldenDupe.messenger().parse(
						msg,
						Message.Type.CHAT,
						placeholders);
				return format;
			}
		} else if (chat == GDChat.CLAN || chat == GDChat.CLAN_ALLY){
			format = Component.empty();
			if (!(whoSees instanceof Player)){
				try {
					throw new IllegalAccessException("Couldn't show chat message for CLAN and CLAN_ALLY to "+ whoSees.getClass().getName() + " as it's not instance of a player!");
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}
			GDPlayer sender = goldenDupe.playerDatabase().fromPlayer(player);
			GDPlayer receiver = goldenDupe.playerDatabase().fromPlayer((Player) whoSees);
			switch (chat){
				case CLAN -> {
					if (!receiver.getFactionID().equals(sender.getFactionID())){
						throw new RuntimeException("Couldn't message "+ receiver.offlinePlayer().getName() + " ("+receiver.uuid()+") as they are not in the same clan as "+ sender.offlinePlayer().getName() + " ("+sender.offlinePlayer().getUniqueId()+"). The GDChat type is set to CLAN and not CLAN_ALLY!");
					}
					format = format.append(Component.text("CLAN ", Color.CLAN_RED, TextDecoration.UNDERLINED).hoverEvent(HoverEvent.showText(Component.text("This message is from your clan.", Color.WHITE).appendNewline().append(Component.text("This message is displayed only displayed to your clan.", Color.CLAN_RED)))));
				}
				case CLAN_ALLY -> {
					if (!receiver.getFactionID().equals(sender.getFactionID())){
						format = format.append(Component.text("ALLY ", Color.CLAN_ALLY_BLUE, TextDecoration.UNDERLINED).hoverEvent(HoverEvent.showText(Component.text("This message is from your clan's ally.", Color.WHITE).appendNewline().append(Component.text("This message is displayed from an ally of your faction.", Color.CLAN_ALLY_BLUE)))));
					} else {
						format = format.append(Component.text("CLAN ", Color.CLAN_ALLY_BLUE, TextDecoration.UNDERLINED).hoverEvent(HoverEvent.showText(Component.text("This message is from your clan.", Color.WHITE).appendNewline().append(Component.text("This message is displayed to all allies of the faction.", Color.CLAN_ALLY_BLUE)))));
					}
				}
			}
			format = format.appendSpace();
		} else {
			Faction faction = gdPlayer.getFaction();
			if (faction != null) {
				format = format.append(faction.getDisplayname()).appendSpace();
			}
		}
		//noinspection UnnecessaryUnicodeEscape
		format = format.append(name)
				.appendSpace()
				.append(Component.text("\u00BB", NamedTextColor.GRAY))
				.appendSpace()
				.append(message);
		return format;
	}

	private ChatFormatListener(GoldenDupe goldenDupe){
		ChatFormatListener.goldenDupe = goldenDupe;
		goldenDupe.getFactions().registerChatHandler(
				(fPlayer, faction, audience, message, type) -> {
					FactionReferenceImpl reference = (FactionReferenceImpl) audience;
					Faction fac = reference.getFaction();
					if (fac == null){
						return null;
					}
					switch (type){
						case ALLY, FACTION, GLOBAL ->{
							return format(fPlayer.player(), audience.player(), ChatToggleChangeListener.convert(type.getChat()), message);
						}
						default -> {
							throw new IllegalStateException("Couldn't modify the chat for plugin "+ goldenDupe.getFactions().getName() + " ("+goldenDupe.getFactions().getClass().getName()+")");
						}
					}
				}
		);
	}

	@EventHandler
	private void onChat(@NotNull AsyncChatEvent event){
		Player p = event.getPlayer();
		GDChat chat = goldenDupe().playerDatabase().fromPlayer(p).chat();
		event.renderer(new ChatRenderer() {
			@Override
			public @NotNull Component render(@NotNull Player player, @NotNull Component component, @NotNull Component message, @NotNull Audience audience) {
				return format(player, audience, chat, message);
			}
		});
	}


	@Override
	public GoldenDupe goldenDupe() {
		return goldenDupe;
	}
}
