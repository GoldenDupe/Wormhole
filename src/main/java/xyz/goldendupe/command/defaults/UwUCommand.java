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

@GDCommandInfo.Command(
		name = "uwu",
		senderType = GDCommandInfo.SenderType.ALL
)
public class UwUCommand extends GDCommand {
	protected UwUCommand(GoldenDupe goldenDupe, GDCommandInfo commandInfo) {
		super(goldenDupe, commandInfo);
	}

	@Override
	public void execute(@NotNull CommandSender sender, @NotNull String[] args, boolean hasArgs) {
		if (sender instanceof Player player) {
			player.chat("I weally weally love GowdenDupe~");
		} else {
			broadcastCommandMessage(sender, Component.text("I weally weally love GowdenDupe~"));
		}
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
