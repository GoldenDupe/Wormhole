package xyz.goldendupe.command.donator;

import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.StringParser;
import xyz.goldendupe.command.internal.cloud.GDCloudCommand;
import xyz.goldendupe.messenger.GoldenMessenger;
import bet.astral.messenger.placeholder.LegacyPlaceholder;
import bet.astral.messenger.placeholder.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.internal.legacy.GDCommandInfo;
import xyz.goldendupe.models.GDChat;
import xyz.goldendupe.models.GDPlayer;

import java.util.LinkedList;
import java.util.List;

public class DonorChatCommand extends GDCloudCommand {

	//TODO make this work with console (im running out of time)
	public DonorChatCommand(GoldenDupe goldenDupe, PaperCommandManager<CommandSender> commandManager) {
		super(goldenDupe, commandManager);
		commandManager.command(
				commandManager.commandBuilder(
								"donorchat",
								Description.of("Lets donors send messages only other donors can see."),
								"donorchat", "dchat"
						)
						.permission(GDCommandInfo.MemberType.DONATOR.cloudOf("donatorchat"))
						.optional(StringParser.stringComponent(StringParser.StringMode.SINGLE).name("dchat-text"))
						.senderType(Player.class)
						.handler(context -> {

							Player sender = context.sender();

							String message = null;
							if (context.optional("dchat-text").isPresent())
								message = (String) context.optional("dchat-text").get();

							boolean hasArgs = true;
							if (message == null) {
								hasArgs = false;
								message = "";
							}

							if (!hasArgs){
								GDPlayer gdPlayer = goldenDupe.playerDatabase().fromPlayer(sender);
								GDChat chat = gdPlayer.chat();
								if (chat == GDChat.DONATOR){
									commandMessenger.message(sender, "donorchat.message-toggle-off");
									gdPlayer.setChat(GDChat.GLOBAL);
								} else {
									commandMessenger.message(sender, "donorchat.message-toggle-on");
									gdPlayer.setChat(GDChat.DONATOR);
								}
							} else {
								List<Placeholder> placeholders = new LinkedList<>(commandMessenger.createPlaceholders(sender));
								placeholders.add(new LegacyPlaceholder("message", message));
								commandMessenger
											.broadcast(GoldenMessenger.MessageChannel.DONATOR,
													"donorchat.message-chat", placeholders);
//								} else if (sender instanceof ConsoleCommandSender){
//									commandMessenger
//											.broadcast(GoldenMessenger.MessageChannel.DONATOR, "donatorchat.message-chat-console", new LegacyPlaceholder("message", message));
							}
						})
		);
	}
}
