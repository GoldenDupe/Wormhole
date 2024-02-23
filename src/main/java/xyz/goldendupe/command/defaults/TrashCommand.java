package xyz.goldendupe.command.defaults;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.internal.cloud.Cloud;
import xyz.goldendupe.command.internal.cloud.GDCloudCommand;
import xyz.goldendupe.command.internal.legacy.GDCommandInfo;

@Cloud
public class TrashCommand extends GDCloudCommand {
	public TrashCommand(GoldenDupe goldenDupe, PaperCommandManager<CommandSender> commandManager) {
		super(goldenDupe, commandManager);
		commandManager.command(commandManager.commandBuilder("trash",
				Description.of("Allows players to delete"),
				"garbage", "josh", "disposal", "rubbish", "british")
				.permission(GDCommandInfo.MemberType.DEFAULT.cloudOf("trash"))
				.senderType(Player.class)
				.handler(context->{
					context.sender().openInventory(Bukkit.createInventory(null, 54, Component.text("Trash")));
				})
		);
	}
}
