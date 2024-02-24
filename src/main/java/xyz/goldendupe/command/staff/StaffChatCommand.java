package xyz.goldendupe.command.staff;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.placeholder.LegacyPlaceholder;
import bet.astral.messenger.placeholder.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.StringParser;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.messenger.GoldenMessenger;
import xyz.goldendupe.models.GDChat;
import xyz.goldendupe.models.GDPlayer;
import xyz.goldendupe.utils.MemberType;

import java.util.LinkedList;
import java.util.List;

@Cloud
public class StaffChatCommand extends GDCloudCommand {

	//TODO: also add console support
	//TODO: maybe add ChatCommand Abstraction
	public StaffChatCommand(GoldenDupe goldenDupe, PaperCommandManager<CommandSender> commandManager) {
		super(goldenDupe, commandManager);
		commandManager.command(
				commandManager.commandBuilder(
								"staffchat",
								Description.of("Lets staff send messages only other staff can see."),
								"sc", "schat"
						)
						.optional(StringParser.stringComponent(StringParser.StringMode.SINGLE).name("schat-text"))
						.permission(MemberType.MODERATOR.cloudOf("staffchat"))
						.senderType(Player.class)
						.handler(context -> {

							Player sender = context.sender();
							String message = null;
							if (context.optional("schat-text").isPresent())
								message = (String) context.optional("schat-text").get();

							boolean hasArgs = true;
							if (message == null) {
								hasArgs = false;
								message = "";
							}

							if (!hasArgs){
								GDPlayer gdPlayer = goldenDupe.playerDatabase().fromPlayer(sender);
								GDChat chat = gdPlayer.chat();
								if (chat == GDChat.STAFF){
									commandMessenger.message(sender, "staffchat.message-toggle-off");
									gdPlayer.setChat(GDChat.GLOBAL);
								} else {
									commandMessenger.message(sender, "staffchat.message-toggle-on");
									gdPlayer.setChat(GDChat.STAFF);
								}
							} else {
								List<Placeholder> placeholders = new LinkedList<>(commandMessenger.createPlaceholders(sender));
								placeholders.add(new LegacyPlaceholder("message", message));
								commandMessenger.broadcast(GoldenMessenger.MessageChannel.STAFF,
										"staffchat.message-chat", placeholders);
//								} else if (sender instanceof ConsoleCommandSender){
//									commandMessenger.broadcast(GoldenMessenger.MessageChannel.STAFF, "staffchat.message-chat-console", new LegacyPlaceholder("message", message));
							}

						})
		);
	}
}
