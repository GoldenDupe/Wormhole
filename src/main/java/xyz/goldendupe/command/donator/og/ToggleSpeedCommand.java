package xyz.goldendupe.command.donator.og;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import bet.astral.cloudplusplus.annotations.Cloud;
import xyz.goldendupe.GoldenDupeBootstrap;
import xyz.goldendupe.GoldenDupeCommandRegister;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.messenger.Translations;
import xyz.goldendupe.models.GDPlayer;
import xyz.goldendupe.utils.MemberType;

@Cloud
public class ToggleSpeedCommand extends GDCloudCommand {
	public ToggleSpeedCommand(GoldenDupeCommandRegister register, PaperCommandManager<CommandSender> commandManager) {
		super(register, commandManager);
		commandManager.command(
				commandManager.commandBuilder(
								"togglespeed",
								Description.of("Allows a player to toggle speed."),
						"speed"
						)
						.permission(MemberType.OG.cloudOf("toggle-speed"))
						.senderType(Player.class)
						.handler(context -> {
							Player sender = context.sender();
							GDPlayer player = goldenDupe().playerDatabase().fromPlayer(sender);
							boolean toggle = player.isToggleSpeed();
							player.setToggleSpeed(!toggle);

							if (!toggle) {
								messenger.message(sender, Translations.COMMAND_TOGGLE_SPEED_TRUE);
							} else {
								messenger.message(sender, Translations.COMMAND_TOGGLE_SPEED_FALSE);
							}
						})
		);
	}
}
