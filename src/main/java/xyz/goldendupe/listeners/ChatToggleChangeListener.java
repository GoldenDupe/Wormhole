package xyz.goldendupe.listeners;

import bet.astral.unity.event.player.ASyncPlayerChangeChatEvent;
import bet.astral.unity.model.FChat;
import bet.astral.unity.model.FPlayer;
import org.bukkit.event.EventHandler;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.events.GDChatChangeEvent;
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
			case CUSTOM -> {
				return GDChat.UNKNOWN;
			}
		}
		GoldenDupe.instance().getLogger().severe("Couldn't find the equivalent value for "+chat.getClass().getName()+"$"+chat.name()+ " from golden dupe! This requires fixing, returning "+ GDChat.UNKNOWN.getClass()+"$"+GDChat.UNKNOWN);
		return GDChat.UNKNOWN;
	}

	public static FChat convert(GDChat chat){
		switch (chat){
			case CLAN_ALLY -> {
				return FChat.ALLY;
			}
			case CLAN -> {
				return FChat.FACTION;
			}
			case GLOBAL -> {
				return FChat.GLOBAL;
			}
			default -> {
				return FChat.CUSTOM;
			}
		}
	}
	private final GoldenDupe goldenDupe;

	public ChatToggleChangeListener(GoldenDupe goldenDupe) {
		this.goldenDupe = goldenDupe;
	}

	@SuppressWarnings("OverrideOnly")
	@EventHandler
	public void onChatChange(ASyncPlayerChangeChatEvent event) {
		GDPlayer player = goldenDupe.playerDatabase().fromPlayer(event.getPlayer());


		FChat from = event.getFrom();
		FChat to = event.getTo();

		GDChat toGD = convert(to);
		GDChat beforeGD = convert(event.getFrom());

		if (toGD == beforeGD){
			return;
		}


		player.setChat(toGD);
	}

	@EventHandler
	public void onGDChatChange(GDChatChangeEvent event){
		if (event.getFrom()==event.getTo()){
			return;
		}
		FChat fChat = convert(event.getTo());
		GDPlayer player = goldenDupe.playerDatabase().fromPlayer(event.getPlayer());
		FPlayer fPlayer = player.asFactionPlayer();
		fPlayer.setChatType(fChat);
	}

	@Override
	public GoldenDupe goldenDupe() {
		return goldenDupe;
	}
}
