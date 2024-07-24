package xyz.goldendupe.command.defaults;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import bet.astral.cloudplusplus.annotations.Cloud;
import xyz.goldendupe.GoldenDupeCommandRegister;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.messenger.Translations;
import xyz.goldendupe.utils.MemberType;

@Cloud
public class ClearOwnChatCommand extends GDCloudCommand {

	public ClearOwnChatCommand(GoldenDupeCommandRegister register, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
		super(register, commandManager);

		commandManager.command(
				commandManager.commandBuilder(
								"clearmychat",
								Description.of("Clears the player's chat."),
								"bleach"
						)
						.permission(MemberType.DEFAULT.permissionOf("clear-my-chat"))
						.senderType(Player.class)
						.handler(context -> {
							Player sender = context.sender();
							Component component = Component.empty();
							for (int i = 0; i < 275; i++) {
								sender.sendMessage(component);
							}
							messenger.message(sender, Translations.COMMAND_CHAT_CLEAR_SELF);
						})
		);
	}
}