package xyz.goldendupe.command.defaults;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.internal.cloud.Cloud;
import xyz.goldendupe.command.internal.cloud.GDCloudCommand;
import xyz.goldendupe.command.internal.legacy.GDCommandInfo;
import xyz.goldendupe.models.GDPlayer;

@Cloud
public class ToggleItemPickupCommand extends GDCloudCommand {
	public ToggleItemPickupCommand(GoldenDupe goldenDupe, PaperCommandManager<CommandSender> commandManager) {
		super(goldenDupe, commandManager);
		commandManager.command(
				commandManager.commandBuilder(
								"togglepickup",
								Description.of("Allows a player to toggle picking up items from the ground."),
								"toggleitempickup"
						)
						.permission(GDCommandInfo.MemberType.DEFAULT.cloudOf("toggle-pickup"))
						.senderType(Player.class)
						.handler(context -> {
							Player sender = context.sender();
							GDPlayer player = goldenDupe.playerDatabase().fromPlayer(sender);
							boolean toggle = player.isTogglePickupItem();
							player.setTogglePickupItem(!toggle);

							if (!toggle) {
								commandMessenger.message(sender, "toggle-pickup.message-enabled");
							} else {
								commandMessenger.message(sender, "toggle-pickup.message-disabled");
							}
						})
		);
	}
}
