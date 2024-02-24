package xyz.goldendupe.listeners;

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
import xyz.goldendupe.models.GDPlayer;

public class ChatFormatListener implements GDListener {
	private static GoldenDupe goldenDupe;

	public static @NotNull Component format(Player player, Component component){
		return format(player, player, component);
	}
	public static @NotNull Component format(Player player, Audience whoSees, Component message) {
		GDPlayer gdPlayer = goldenDupe.playerDatabase().fromPlayer(player);
		Component name = GoldenMessenger.prefixNameSuffix(player);
		Component component = Component.empty();
		if (whoSees instanceof Player oPlayer){
			if (gdPlayer.vanished() && oPlayer.canSee(player)){
				component = component.append(Component.text("V", NamedTextColor.DARK_RED).decoration(TextDecoration.BOLD, true).appendSpace());
			}
		}
		//noinspection UnnecessaryUnicodeEscape
		component = component.append(name)
				.appendSpace()
				.append(Component.text("\u00BB", NamedTextColor.GRAY))
				.appendSpace()
				.append(message);
		return component;
	}

	private ChatFormatListener(GoldenDupe goldenDupe){
		ChatFormatListener.goldenDupe = goldenDupe;
	}

	@EventHandler
	private void onChat(@NotNull AsyncChatEvent event){
		Player p = event.getPlayer();
		event.renderer(new ChatRenderer() {
			@Override
			public @NotNull Component render(@NotNull Player player, @NotNull Component component, @NotNull Component message, @NotNull Audience audience) {
				return format(player, audience, message);
			}
		});
	}


	@Override
	public GoldenDupe goldenDupe() {
		return goldenDupe;
	}
}
