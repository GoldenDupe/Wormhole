package xyz.goldendupe.command.donator;

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
public class ToggleItemDropCommand extends GDCloudCommand {
	public ToggleItemDropCommand(GoldenDupe goldenDupe, PaperCommandManager<CommandSender> commandManager) {
		super(goldenDupe, commandManager);

		commandManager.command(
				commandManager.commandBuilder(
						"toggledrop",
						Description.of("Allows a player to toggle dropping items to the ground."),
						"toggledrops"
				)
						.permission(MemberType.DONATOR.cloudOf("toggle-drop"))
						.senderType(Player.class)
						.handler(context->{
							Player sender = context.sender();
							GDPlayer player = goldenDupe.playerDatabase().fromPlayer(sender);
							boolean toggle = player.isToggleDropItem();
							player.setToggleDropItem(!toggle);

							if (!toggle){
								commandMessenger.message(sender, "toggle-drop.message-enabled");
							} else {
								commandMessenger.message(sender, "toggle-drop.message-disabled");
							}
						})
		);
	}
}
