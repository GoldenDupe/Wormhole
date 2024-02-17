package xyz.goldendupe.command.donator;

import bet.astral.goldenmessenger.GoldenMessenger;
import bet.astral.messagemanager.placeholder.LegacyPlaceholder;
import bet.astral.messagemanager.placeholder.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.GDCommand;
import xyz.goldendupe.command.GDCommandInfo;
import xyz.goldendupe.models.GDChat;
import xyz.goldendupe.models.GDPlayer;

import java.util.LinkedList;
import java.util.List;

@GDCommandInfo.Command(
		name = "donatorchat",
		senderType = GDCommandInfo.SenderType.ALL,
		memberType = GDCommandInfo.MemberType.DONATOR,
		aliases = {"donorchat", "dchat", "dc"}
)
public class DonatorChatCommand extends GDCommand {
	protected DonatorChatCommand(GoldenDupe goldenDupe, GDCommandInfo commandInfo) {
		super(goldenDupe, commandInfo);
	}

	@Override
	public void execute(@NotNull CommandSender sender, @NotNull String[] args, boolean hasArgs) {
		if (!hasArgs){
			if (sender instanceof Player player){
				GDPlayer gdPlayer = goldenDupe.playerDatabase().fromPlayer(player);
				GDChat chat = gdPlayer.chat();
				if (chat == GDChat.DONATOR){
					commandMessenger.message(sender, commandInfo.name()+".message-toggle-off");
					gdPlayer.setChat(GDChat.GLOBAL);
				} else {
					commandMessenger.message(sender, commandInfo.name()+".message-toggle-on");
					gdPlayer.setChat(GDChat.DONATOR);
				}
			} else {
				commandMessenger.message(sender, commandInfo.cannotUseMessageKey());
			}
		} else {
			String message = asString(args, 0);
			if (sender instanceof Player player){
				List<Placeholder> placeholders = new LinkedList<>(commandMessenger.createPlaceholders(player));
				placeholders.add(new LegacyPlaceholder("message", message));
				goldenDupe
						.messenger()
						.broadcast(GoldenMessenger.MessageChannel.DONATOR, "donator-chat", placeholders);
			} else if (sender instanceof ConsoleCommandSender console){
				goldenDupe
						.messenger()
						.broadcast(GoldenMessenger.MessageChannel.DONATOR, "donator-chat-console", new LegacyPlaceholder("message", message));
			} else {
				commandMessenger.message(sender, commandInfo.cannotUseMessageKey());
			}
		}
	}

	@Override
	public void execute(@NotNull Player sender, @NotNull String[] args, boolean hasArgs) { execute((CommandSender) sender, args, hasArgs); }

	@Override
	public List<String> tab(@NotNull CommandSender sender, @NotNull String[] args, boolean hasArgs) {
		return null;
	}

	@Override
	public List<String> tab(@NotNull Player sender, @NotNull String[] args, boolean hasArgs) { return tab((CommandSender) sender, args, hasArgs); }
}
