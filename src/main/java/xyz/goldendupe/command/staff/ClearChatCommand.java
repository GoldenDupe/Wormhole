package xyz.goldendupe.command.staff;

import bet.astral.messenger.placeholder.LegacyPlaceholder;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.internal.cloud.Cloud;
import xyz.goldendupe.command.internal.cloud.GDCloudCommand;

@Cloud
public class ClearChatCommand extends GDCloudCommand {

	public ClearChatCommand(GoldenDupe goldenDupe, PaperCommandManager<CommandSender> commandManager) {
		super(goldenDupe, commandManager);
		commandManager.command(
				commandManager.commandBuilder(
								"clearchat",
								Description.of("Clears the chat globally."),
								"chatclear"
						)
						.permission("goldendupe.staff.clearchat.receive")
						.senderType(Player.class)
						.handler(context -> {
							Player sender = context.sender();
							Component component = Component.text("");
							for (int i = 0; i < 275; i++){
								// Might just make a random string infront of the message to make it really clear
//								Bukkit.broadcast(Component.text("CC#"+i), "goldendupe.staff.clearchat.receive");
								Bukkit.broadcast(component, "goldendupe.staff.clearchat.receive");
							}
							commandMessenger.broadcast("clearchat.message-chat-cleared",
									new LegacyPlaceholder("who", sender instanceof ConsoleCommandSender ? "Server-Console" : sender.getName()));
						})
		);
	}

}
