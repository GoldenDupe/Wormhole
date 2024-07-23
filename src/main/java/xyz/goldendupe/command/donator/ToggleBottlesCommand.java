package xyz.goldendupe.command.donator;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import xyz.goldendupe.GoldenDupe;
import bet.astral.cloudplusplus.annotations.Cloud;
import xyz.goldendupe.GoldenDupeBootstrap;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.messenger.Translations;
import xyz.goldendupe.models.GDPlayer;
import xyz.goldendupe.utils.MemberType;

@Cloud
public class ToggleBottlesCommand extends GDCloudCommand {
	public ToggleBottlesCommand(GoldenDupeBootstrap bootstrap, PaperCommandManager<CommandSender> commandManager) {
		super(bootstrap, commandManager);
		commandManager.command(
				commandManager.commandBuilder(
								"togglebottles",
								Description.of("Allows a player to toggle between receiving and not receiving potion bottles."),
										"togglebottle"
						)
						.permission(MemberType.DONATOR.cloudOf("toggle-bottles"))
						.senderType(Player.class)
						.handler(context -> {
							Player sender = context.sender();
							GDPlayer player = goldenDupe().playerDatabase().fromPlayer(sender);
							boolean toggle = player.isToggleNightVision();
							player.setToggleNightVision(!toggle);

							if (!toggle){
								messenger.message(sender, Translations.COMMAND_TOGGLE_POTION_BOTTLES_TRUE);
							} else {
								messenger.message(sender, Translations.COMMAND_TOGGLE_POTION_BOTTLES_FALSE);
							}
						})
		);
	}
}
