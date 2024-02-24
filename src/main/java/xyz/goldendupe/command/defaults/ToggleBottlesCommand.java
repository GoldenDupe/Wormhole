package xyz.goldendupe.command.defaults;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import xyz.goldendupe.GoldenDupe;
import bet.astral.cloudplusplus.annotations.Cloud;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.models.GDPlayer;
import xyz.goldendupe.utils.MemberType;

@Cloud
public class ToggleBottlesCommand extends GDCloudCommand {
	public ToggleBottlesCommand(GoldenDupe goldenDupe, PaperCommandManager<CommandSender> commandManager) {
		super(goldenDupe, commandManager);
		commandManager.command(
				commandManager.commandBuilder(
								"togglebottles",
								Description.of("Allows a player to toggle between receiving and not receiving potion bottles."),
										"togglebottle"
						)
						.permission(MemberType.DEFAULT.cloudOf("toggle-bottles"))
						.senderType(Player.class)
						.handler(context -> {
							Player sender = context.sender();
							GDPlayer player = goldenDupe.playerDatabase().fromPlayer(sender);
							boolean toggle = player.isToggleNightVision();
							player.setToggleNightVision(!toggle);

							if (!toggle) {
								commandMessenger.message(sender, "toggle-bottles.message-enabled");
							} else {
								commandMessenger.message(sender, "toggle-bottles.message-disabled");
							}
						})
		);
	}
}
