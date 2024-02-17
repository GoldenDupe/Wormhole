package xyz.goldendupe.command.defaults;

import bet.astral.messagemanager.placeholder.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.GDCommand;
import xyz.goldendupe.command.GDCommandInfo;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@GDCommandInfo.Command(
		name = "ship",
		senderType = GDCommandInfo.SenderType.PLAYER,
		aliases = "fuck",
		minArgs = 2
)
public class ShipCommand extends GDCommand {
	private final Random random = new Random(System.currentTimeMillis());

	protected ShipCommand(GoldenDupe goldenDupe, GDCommandInfo commandInfo) {
		super(goldenDupe, commandInfo);
	}

	@Override
	public void execute(@NotNull CommandSender sender, @NotNull String[] args, boolean hasArgs) {
	}

	@Override
	public void execute(@NotNull Player sender, @NotNull String[] args, boolean hasArgs) {
		String first = args[0];
		String second = args[1];
		if (first.equalsIgnoreCase(second)) {
			commandMessenger.message(sender, "ship.message-ship-2-same-things");
			return;
		}

		List<Placeholder> placeholders = new LinkedList<>();
		placeholders.add(new Placeholder("who", first));
		placeholders.add(new Placeholder("who-2", second));
		placeholders.add(new Placeholder("personality_match", createMatch()));
		placeholders.add(new Placeholder("passion_match", createMatch()));
		placeholders.add(new Placeholder("friendship_match", createMatch()));
		placeholders.add(new Placeholder("chemistry_match", createMatch()));
		commandMessenger.message(sender, "ship.message-shipped");
	}

	private String createMatch(){
		int random = this.random.nextInt(0, 100);
		String bar = "|".repeat(100);
		if (random != 0) {
			StringBuilder builder = new StringBuilder();
			builder.append("<rainbow>");
			for (int i = 1; i < 100; i++) {
				if (i == random) {
					builder.append("</rainbow>");
				}
				builder.append("|");
			}
			bar = builder.toString();
		}
		bar = "<gray>"+bar;
		return bar;
	}

	@Override
	public List<String> tab(@NotNull CommandSender sender, @NotNull String[] args, boolean hasArgs) {
		return null;
	}

	@Override
	public List<String> tab(@NotNull Player sender, @NotNull String[] args, boolean hasArgs) {
		return null;
	}
}
