package xyz.goldendupe.listeners;

import bet.astral.unity.event.player.ASyncPlayerChangeChatEvent;
import bet.astral.unity.model.FChat;
import org.bukkit.event.EventHandler;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.models.GDChat;
import xyz.goldendupe.models.GDPlayer;

public class ChatToggleChangeListener implements GDListener {
	public static GDChat convert(FChat chat){
		switch (chat){
			case ALLY -> {
				return GDChat.CLAN_ALLY;
			}
			case FACTION -> {
				return GDChat.CLAN;
			}
			case GLOBAL -> {
				return GDChat.GLOBAL;
			}
		}
		GoldenDupe.instance().getLogger().severe("Couldn't find the equivalent value for "+chat.getClass().getName()+"$"+chat.name()+ " from golden dupe! This requires fixing, returning "+ GDChat.UNKNOWN.getClass()+"$"+GDChat.UNKNOWN);
		return GDChat.UNKNOWN;
	}
	private final GoldenDupe goldenDupe;

	public ChatToggleChangeListener(GoldenDupe goldenDupe) {
		this.goldenDupe = goldenDupe;
	}

	@SuppressWarnings("OverrideOnly")
	@EventHandler
	public void onChatChange(ASyncPlayerChangeChatEvent event) {
		FChat from = event.getFrom();
		FChat to = event.getTo();
		GDChat toGD = convert(to);

		GDPlayer player = goldenDupe.playerDatabase().fromPlayer(event.getPlayer());

		player.setChat(toGD);
	}

	@Override
	public GoldenDupe goldenDupe() {
		return goldenDupe;
	}
}
