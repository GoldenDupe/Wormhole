package xyz.goldendupe.command.staff;

import bet.astral.messenger.placeholder.LegacyPlaceholder;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.internal.legacy.GDCommand;
import xyz.goldendupe.command.internal.legacy.GDCommandInfo;
import xyz.goldendupe.command.internal.Permission;
import xyz.goldendupe.command.internal.Permissions;

import java.util.Collections;
import java.util.List;

@Permissions(
		{
				@Permission("goldendupe.staff.clearchat.receive"),
		}
)
@GDCommandInfo.Command(name = "clearchat",
senderType = GDCommandInfo.SenderType.ALL,
memberType = GDCommandInfo.MemberType.MODERATOR,
aliases = "chatclear")
public class ClearChatCommand extends GDCommand {
	protected ClearChatCommand(GoldenDupe goldenDupe, GDCommandInfo commandInfo) {
		super(goldenDupe, commandInfo);
	}

	@Override
	public void execute(@NotNull CommandSender sender, @NotNull String[] args, boolean hasArgs) {
		Component component = Component.text("");
		for (int i = 0; i < 275; i++){
			// Might just make a random string infront of the message to make it really clear
//			Bukkit.broadcast(Component.text("CC#"+i), "goldendupe.staff.clearchat.receive");
			Bukkit.broadcast(component, "goldendupe.staff.clearchat.receive");
		}
		commandMessenger.broadcast("clearchat.message-chat-cleared", new LegacyPlaceholder("who", sender instanceof ConsoleCommandSender ? "Server-Console" : sender.getName()));
	}

	@Override
	public void execute(@NotNull Player sender, @NotNull String[] args, boolean hasArgs) {

	}

	@Override
	public List<String> tab(@NotNull CommandSender sender, @NotNull String[] args, boolean hasArgs) {
		return Collections.emptyList();
	}

	@Override
	public List<String> tab(@NotNull Player sender, @NotNull String[] args, boolean hasArgs) {
		return Collections.emptyList();
	}
}
