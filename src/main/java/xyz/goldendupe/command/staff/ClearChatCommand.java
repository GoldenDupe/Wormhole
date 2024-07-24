package xyz.goldendupe.command.staff;

import bet.astral.messenger.v2.placeholder.Placeholder;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import bet.astral.cloudplusplus.annotations.Cloud;
import xyz.goldendupe.GoldenDupeBootstrap;
import xyz.goldendupe.GoldenDupeCommandRegister;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.messenger.Translations;

@Cloud
public class ClearChatCommand extends GDCloudCommand {

	public ClearChatCommand(GoldenDupeCommandRegister register, PaperCommandManager<CommandSender> commandManager) {
		super(register, commandManager);
		commandManager.command(
				commandManager.commandBuilder(
								"clearchat",
								Description.of("Clears the chat globally."),
								"chatclear"
						)
						.permission("goldendupe.staff.clearchat")
						.senderType(Player.class)
						.handler(context -> {
							Player sender = context.sender();
							Component component = Component.text("");
							for (int i = 0; i < 275; i++){
								// Might just make a random string infront of the message to make it really clear
//								Bukkit.broadcast(Component.text("CC#"+i), "goldendupe.staff.clearchat.receive");
								Bukkit.broadcast(component, "goldendupe.staff.clearchat.receive");
							}
							messenger.broadcast(Translations.COMMAND_CLEAR_CHAT,
									Placeholder.legacy("who", sender instanceof ConsoleCommandSender ? "Server-Console" : sender.getName()));
						})
		);
	}

}
