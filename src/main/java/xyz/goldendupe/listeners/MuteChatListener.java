package xyz.goldendupe.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.messenger.Translations;

public class MuteChatListener implements GDListener{
	private final GoldenDupe goldenDupe;

	public MuteChatListener(GoldenDupe goldenDupe) {
		this.goldenDupe = goldenDupe;
	}

	@Override
	public GoldenDupe goldenDupe() {
		return goldenDupe;
	}

	@EventHandler(ignoreCancelled = true)
	public void onAsyncChat(AsyncChatEvent event) {
		if (goldenDupe.getGlobalData().isGlobalChatMute() && !event.getPlayer().hasPermission(goldenDupe.getGlobalData().getGlobalChatMuteAllowedUsers().getBypass())){
			event.setCancelled(true);
			goldenDupe.messenger().message(event.getPlayer(), Translations.LISTENER_MUTECHAT_MUTED);
		}
	}
}
