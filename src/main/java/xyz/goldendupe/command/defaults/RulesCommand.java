package xyz.goldendupe.command.defaults;

import org.bukkit.command.CommandSender;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import xyz.goldendupe.GoldenDupe;
import bet.astral.cloudplusplus.annotations.Cloud;
import xyz.goldendupe.command.cloud.GDCloudCommand;


@Cloud
public class RulesCommand extends GDCloudCommand {

	public RulesCommand(GoldenDupe goldenDupe, PaperCommandManager<CommandSender> commandManager) {
		super(goldenDupe, commandManager);
		commandManager.command(commandManager.commandBuilder(
				"rules",
				Description.of("Shows the rules of the server."))
				.handler(context->{
					commandMessenger.message(context.sender(), "rules.message.rules");
				})
		);
	}
}
