package xyz.goldendupe.listeners;

import bet.astral.messenger.v2.placeholder.Placeholder;
import bet.astral.messenger.v2.translation.TranslationKey;
import bet.astral.unity.model.FPrefix;
import bet.astral.unity.model.FRole;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.refrence.FactionReferenceImpl;
import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.admin.MarkAsOGCommand;
import xyz.goldendupe.messenger.GoldenPlaceholderManager;
import xyz.goldendupe.models.GDChat;
import xyz.goldendupe.models.GDPlayer;
import xyz.goldendupe.models.chatcolor.Color;
import xyz.goldendupe.utils.OriginalMemberType;

import java.util.Optional;

public class ChatFormatListener implements GDListener {
	private static GoldenDupe goldenDupe;

	public static @NotNull Component format(Player player, Component component){
		return format(player, player, GDChat.GLOBAL, component);
	}
	public static @NotNull Component format(Player player, Audience whoSees, GDChat chat, Component message) {
		Component name = GoldenPlaceholderManager.prefixNameSuffix(player);

		LuckPerms luckPerms = goldenDupe.luckPerms();
		User user = luckPerms.getPlayerAdapter(Player.class).getUser(player);
		CachedMetaData originalRoleNode = user.getCachedData().getMetaData();
		Optional<OriginalMemberType> roleOptional = originalRoleNode.getMetaValue(MarkAsOGCommand.OG_ROLE_KEY, OriginalMemberType::valueOf);
		if (roleOptional.isPresent()) {
			OriginalMemberType originalMemberType = roleOptional.get();
			name = name.hoverEvent(HoverEvent.showText(Component
					.text("OG Player", NamedTextColor.GOLD, TextDecoration.BOLD)
					.appendSpace()
					.append(Component.text("|", Color.DARK_GRAY, TextDecoration.BOLD))
					.appendSpace()
					.append(Component
							.text("Previous rank ", Color.WHITE)
							.append(Component.text(":", Color.GRAY))
							.append(originalMemberType.displayname(player))
					).decoration(TextDecoration.ITALIC, false)));
		}

		Component format = Component.empty();
		GDPlayer gdPlayer = goldenDupe.playerDatabase().fromPlayer(player);
		if (whoSees instanceof Player oPlayer) {
			if (gdPlayer.vanished() && oPlayer.canSee(player)) {
				format = format.append(Component.text("V", NamedTextColor.DARK_RED).decoration(TextDecoration.BOLD, true).appendSpace());
			}
		}

		if (chat.asMessageChannel() != null && chat.asMemberType() != null) {
			return format;
		}else {
			Faction faction = gdPlayer.getFaction();
			if (faction != null) {
				FRole role = faction.getRole(player);
				FPrefix prefix = faction.getPublicPrefix(role);
				format = format.append(prefix.format(faction)).appendSpace();
//              format = format.append(faction.getDisplayname()).appendSpace();
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
		if (chat.asMemberType() != null && chat.asMessageChannel() != null){
			event.viewers().removeIf(viewer->viewer instanceof Player player && !player.hasPermission(chat.asMessageChannel().permission()));
			goldenDupe.messenger().message(event.viewers(), TranslationKey.of("commands."+chat.name()+"-chat.chat"), Placeholder.of("player", p.name()), Placeholder.of("message", event.message()));
			event.setCancelled(true);
			return;
		}
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
