package xyz.goldendupe.command.defaults;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.internal.cloud.GDCloudCommand;
import xyz.goldendupe.command.internal.legacy.GDCommandInfo;
import xyz.goldendupe.models.GDPlayer;

public class ToggleItemsCommand extends GDCloudCommand {
	public ToggleItemsCommand(GoldenDupe goldenDupe, PaperCommandManager<CommandSender> commandManager) {
		super(goldenDupe, commandManager);
		commandManager.command(
				commandManager.commandBuilder(
								"toggle",
								Description.of("Allows a player to toggle dropping items to the ground"),
								"toggleitems", "random", "randomitems", "togglerandomitems")
						.permission(GDCommandInfo.MemberType.DEFAULT.cloudOf("toggle-drop"))
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
