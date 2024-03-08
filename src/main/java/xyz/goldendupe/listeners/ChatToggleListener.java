package xyz.goldendupe.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.audience.Audience;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.models.GDChat;
import xyz.goldendupe.models.GDPlayer;

import java.util.Set;

public class ChatToggleListener implements GDListener{
	private final GoldenDupe goldenDupe;

	public ChatToggleListener(GoldenDupe goldenDupe) {
		this.goldenDupe = goldenDupe;
	}

	@EventHandler
	public void onChat(AsyncChatEvent event){
		Player player = event.getPlayer();
		GDPlayer gdPlayer = goldenDupe.playerDatabase().fromPlayer(player);

		GDChat chat = gdPlayer.chat();
		if (chat== GDChat.GLOBAL || chat==GDChat.CLAN){
			return;
		}

		if (chat.asMessageChannel() == null){
			return;
		}

		Set<Audience> audiences = event.viewers();

		audiences.removeIf(aud->{
			if (aud instanceof Player p){
				String permission = chat.asMessageChannel().permission();
				return !p.hasPermission(permission);
			}
			return false;
		});
	}

	@Override
	public GoldenDupe goldenDupe() {
		return goldenDupe;
	}
}
