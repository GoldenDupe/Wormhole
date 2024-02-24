package xyz.goldendupe.command.defaults;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.internal.cloud.Cloud;
import xyz.goldendupe.command.internal.cloud.GDCloudCommand;

@Cloud
public class PlayerClearChatCommand extends GDCloudCommand {

	public PlayerClearChatCommand(GoldenDupe goldenDupe, PaperCommandManager<CommandSender> commandManager) {
		super(goldenDupe, commandManager);
		commandManager.command(
				commandManager.commandBuilder(
								"clearmychat",
								Description.of("Clears the player's chat."),
								"mychatbanish!", "bleach"
						)
						.permission("goldendupe.all.clearchat")
						.senderType(Player.class)
						.handler(context -> {
							Player sender = context.sender();
							Component component = Component.empty();
							for (int i = 0; i < 275; i++){
								sender.sendMessage(component);
							}
							commandMessenger.message(sender, "clearmychat.message.chat-cleared");
						})
		);
	}

}