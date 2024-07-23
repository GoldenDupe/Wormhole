package xyz.goldendupe.command.defaults;

import bet.astral.messenger.v2.placeholder.Placeholder;
import bet.astral.messenger.v2.placeholder.PlaceholderList;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.Command;
import org.incendo.cloud.bukkit.parser.PlayerParser;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import bet.astral.cloudplusplus.annotations.Cloud;
import xyz.goldendupe.GoldenDupeBootstrap;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.messenger.Translations;
import xyz.goldendupe.utils.MemberType;
import xyz.goldendupe.utils.Ping;

@Cloud
public class PingCommand extends GDCloudCommand {

	private PingCommand(GoldenDupeBootstrap bootstrap, PaperCommandManager<CommandSender> commandManager) {
		super(bootstrap, commandManager);
		Command.Builder<Player> pingBuilder = commandManager.commandBuilder("ping", Description.of("Shows the ping of a player."), "ms")
				.permission(MemberType.DEFAULT.cloudOf("ping"))
				.senderType(Player.class)
				.handler(context -> {
							Player sender = context.sender();
							PlaceholderList placeholders = new PlaceholderList();
							placeholders.add(Placeholder.of("player", sender.name()));
							int ping = sender.getPing();
							Component coloredPing = Ping.defaultPing.format(ping);
							placeholders.add(Placeholder.of("ping", ping));
							placeholders.add(Placeholder.of("ping_colored", coloredPing));

							messenger.message(sender, Translations.COMMAND_PING_SELF, placeholders);
						}
				);
		commandManager.command(pingBuilder);
		commandManager.command(pingBuilder
				.argument(PlayerParser.playerComponent().name("player"))
				.commandDescription(Description.of("Allows checking ping of other players."))
				.handler(context -> {
							Player argument = context.get("player");
							Player sender = context.sender();
							PlaceholderList placeholders = new PlaceholderList();
							int ping = argument.getPing();
							Component coloredPing = Ping.defaultPing.format(ping);
							placeholders.add(Placeholder.of("ping", ping));
							placeholders.add(Placeholder.of("ping_colored", coloredPing));

							if (sender.equals(argument)) {
								messenger.message(sender, Translations.COMMAND_PING_SELF, placeholders);
							} else {
								messenger.message(sender, Translations.COMMAND_PING_OTHER, placeholders);
							}
						}
				)
		);
	}
}