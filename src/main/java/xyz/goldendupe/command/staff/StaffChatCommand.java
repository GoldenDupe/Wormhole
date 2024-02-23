package xyz.goldendupe.command.staff;

import xyz.goldendupe.messenger.GoldenMessenger;
import bet.astral.messenger.placeholder.LegacyPlaceholder;
import bet.astral.messenger.placeholder.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.internal.legacy.GDCommand;
import xyz.goldendupe.command.internal.legacy.GDCommandInfo;
import xyz.goldendupe.models.GDChat;
import xyz.goldendupe.models.GDPlayer;

import java.util.LinkedList;
import java.util.List;


@GDCommandInfo.Command(
		name = "staffchat",
		senderType = GDCommandInfo.SenderType.ALL,
		memberType = GDCommandInfo.MemberType.MODERATOR,
		aliases = {"sc", "schat"}
)
public class StaffChatCommand extends GDCommand {
	protected StaffChatCommand(GoldenDupe goldenDupe, GDCommandInfo commandInfo) {
		super(goldenDupe, commandInfo);
	}

	@Override
	public void execute(@NotNull CommandSender sender, @NotNull String[] args, boolean hasArgs) {
		if (!hasArgs){
			if (sender instanceof Player player){
				GDPlayer gdPlayer = goldenDupe.playerDatabase().fromPlayer(player);
				GDChat chat = gdPlayer.chat();
				if (chat == GDChat.STAFF){
					commandMessenger.message(sender, commandInfo.name()+".message-toggle-off");
					gdPlayer.setChat(GDChat.GLOBAL);
				} else {
					commandMessenger.message(sender, commandInfo.name()+".message-toggle-on");
					gdPlayer.setChat(GDChat.STAFF);
				}
			} else {
				commandMessenger.message(sender, commandInfo.cannotUseMessageKey());
			}
		} else {
			String message = asString(args, 0);
			if (sender instanceof Player player){
				List<Placeholder> placeholders = new LinkedList<>(commandMessenger.createPlaceholders(player));
				placeholders.add(new LegacyPlaceholder("message", message));
				commandMessenger.broadcast(GoldenMessenger.MessageChannel.STAFF, "staffchat.message-chat", placeholders);
			} else if (sender instanceof ConsoleCommandSender){
				commandMessenger.broadcast(GoldenMessenger.MessageChannel.STAFF, "staffchat.message-chat-console", new LegacyPlaceholder("message", message));
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
