package xyz.goldendupe.command.defaults;

import org.bukkit.command.CommandSender;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import xyz.goldendupe.GoldenDupe;
import bet.astral.cloudplusplus.annotations.Cloud;
import xyz.goldendupe.GoldenDupeBootstrap;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.messenger.Translations;
import xyz.goldendupe.utils.MemberType;


@Cloud
public class RulesCommand extends GDCloudCommand {

	public RulesCommand(GoldenDupeBootstrap bootstrap, PaperCommandManager<CommandSender> commandManager) {
		super(bootstrap, commandManager);
		commandManager.command(commandManager.commandBuilder(
				"rules",
				Description.of("Shows the rules of the server."))
						.permission(MemberType.DEFAULT.permissionOf("rules"))
				.handler(context->{
					messenger.message(context.sender(), Translations.COMMAND_RULES);
				})
		);
	}
}
