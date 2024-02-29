package xyz.goldendupe.listeners;

import bet.astral.messenger.Message;
import bet.astral.messenger.placeholder.PlaceholderList;
import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.messenger.GoldenMessenger;
import xyz.goldendupe.models.GDChat;
import xyz.goldendupe.models.GDPlayer;

public class ChatFormatListener implements GDListener {
	private static GoldenDupe goldenDupe;

	public static @NotNull Component format(Player player, Component component){
		return format(player, player, GDChat.GLOBAL, component);
	}
	public static @NotNull Component format(Player player, Audience whoSees, GDChat chat, Component message) {
		Component name = GoldenMessenger.prefixNameSuffix(player);

		Component format = Component.empty();
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
		}
		GDPlayer gdPlayer = goldenDupe.playerDatabase().fromPlayer(player);
		if (whoSees instanceof Player oPlayer) {
			if (gdPlayer.vanished() && oPlayer.canSee(player)) {
				format = format.append(Component.text("V", NamedTextColor.DARK_RED).decoration(TextDecoration.BOLD, true).appendSpace());
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
