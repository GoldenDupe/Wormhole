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
public class ToggleSpeedCommand extends GDCloudCommand {
	public ToggleSpeedCommand(GoldenDupe goldenDupe, PaperCommandManager<CommandSender> commandManager) {
		super(goldenDupe, commandManager);
		commandManager.command(
				commandManager.commandBuilder(
								"togglespeed",
								Description.of("Allows a player to toggle night vision."),
						"speed"
						)
						.permission(MemberType.DEFAULT.cloudOf("toggle-speed"))
						.senderType(Player.class)
						.handler(context -> {
							Player sender = context.sender();
							GDPlayer player = goldenDupe.playerDatabase().fromPlayer(sender);
							boolean toggle = player.isToggleSpeed();
							player.setToggleSpeed(!toggle);

							if (!toggle) {
								commandMessenger.message(sender, "toggle-speed.message-enabled");
							} else {
								commandMessenger.message(sender, "toggle-speed.message-disabled");
							}
						})
		);
	}
}
