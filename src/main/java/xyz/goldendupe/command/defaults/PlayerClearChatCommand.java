package xyz.goldendupe.command.defaults;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.GDCommand;
import xyz.goldendupe.command.GDCommandInfo;

import java.util.Collections;
import java.util.List;

@GDCommandInfo.Command(name = "clearmychat",
		senderType = GDCommandInfo.SenderType.ALL,
		memberType = GDCommandInfo.MemberType.MODERATOR,
		aliases = {"mychatbanish!", "bleach"})
public class PlayerClearChatCommand extends GDCommand {
	protected PlayerClearChatCommand(GoldenDupe goldenDupe, GDCommandInfo commandInfo) {
		super(goldenDupe, commandInfo);
	}

	@Override
	public void execute(@NotNull CommandSender sender, @NotNull String[] args, boolean hasArgs) {
		Component component = Component.empty();
		for (int i = 0; i < 275; i++){
			// Might just make a random string infront of the message to make it really clear


			sender.sendMessage(Component.text("CC#"+i));
			//Bukkit.broadcast(component, "goldendupe.staff.clearchat.receive");
		}
		commandMessenger.message(sender, "clearmychat.chat-cleared");
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
