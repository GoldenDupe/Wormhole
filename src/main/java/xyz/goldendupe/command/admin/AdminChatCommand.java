package xyz.goldendupe.command.admin;

import bet.astral.messenger.placeholder.LegacyPlaceholder;
import bet.astral.messenger.placeholder.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.StringParser;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.internal.cloud.Cloud;
import xyz.goldendupe.command.internal.cloud.GDCloudCommand;
import xyz.goldendupe.messenger.GoldenMessenger;
import xyz.goldendupe.models.GDChat;
import xyz.goldendupe.models.GDPlayer;

import java.util.LinkedList;
import java.util.List;

@Cloud
public class AdminChatCommand extends GDCloudCommand {

	public AdminChatCommand(GoldenDupe goldenDupe, PaperCommandManager<CommandSender> commandManager) {
		super(goldenDupe, commandManager);
		commandManager.command(
				commandManager.commandBuilder(
								"adminchat",
								Description.of("Lets admins send messages only other admins can see."),
								"achat"
						)
						.optional(StringParser.stringComponent(StringParser.StringMode.SINGLE).name("achat-text"))
						.permission("goldendupe.admin.adminchat")
						.handler(context -> {

							CommandSender sender = context.sender();
							String message = null;
							if (context.optional("achat-text").isPresent())
								message = (String) context.optional("achat-text").get();

							boolean hasArgs = true;
							if (message == null) {
								hasArgs = false;
								message = "";
							}

							if (sender instanceof Player player) {
								if (!hasArgs) {
									GDPlayer gdPlayer = goldenDupe.playerDatabase().fromPlayer(player);
									GDChat chat = gdPlayer.chat();
									if (chat == GDChat.ADMIN) {
										commandMessenger.message(sender, "admin.message-toggle-off");
										gdPlayer.setChat(GDChat.GLOBAL);
									} else {
										commandMessenger.message(sender, "admin.message-toggle-on");
										gdPlayer.setChat(GDChat.ADMIN);
									}
								} else {
									List<Placeholder> placeholders = new LinkedList<>(commandMessenger.createPlaceholders(player));
									placeholders.add(new LegacyPlaceholder("message", message));
									commandMessenger
											.broadcast(GoldenMessenger.MessageChannel.ADMIN,
													"adminchat.message-chat", placeholders);
								}
							} else if (hasArgs && sender instanceof ConsoleCommandSender) {
								commandMessenger
											.broadcast(GoldenMessenger.MessageChannel.ADMIN,
													"adminchat.message-chat-console",
													new LegacyPlaceholder("message", message));
							}

						})
		);
	}
}
