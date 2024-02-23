package xyz.goldendupe.command.defaults;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.internal.legacy.GDCommand;
import xyz.goldendupe.command.internal.legacy.GDCommandInfo;

import java.util.Collections;
import java.util.List;

@GDCommandInfo.Command(
		name = "rules",
		senderType = GDCommandInfo.SenderType.ALL
)
public class RulesCommand extends GDCommand {
	protected RulesCommand(GoldenDupe goldenDupe, GDCommandInfo commandInfo) {
		super(goldenDupe, commandInfo);
	}

	@Override
	public void execute(@NotNull CommandSender sender, @NotNull String[] args, boolean hasArgs) {
		commandMessenger.message(sender, "rules.message-rules");
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
		return null;
	}
}
