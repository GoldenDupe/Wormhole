package xyz.goldendupe.command.defaults;

import bet.astral.messenger.placeholder.Placeholder;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.internal.cloud.GDCloudCommand;
import xyz.goldendupe.command.internal.legacy.GDCommand;
import xyz.goldendupe.command.internal.legacy.GDCommandInfo;

import java.util.ArrayList;
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
		placeholders.addAll(createMatch("personality"));
		placeholders.addAll(createMatch("passion"));
		placeholders.addAll(createMatch("friendship"));
		placeholders.addAll(createMatch("chemistry"));
		commandMessenger.message(sender, "ship.message-shipped", placeholders);
	}

	private List<Placeholder> createMatch(String name){
		int random = this.random.nextInt(0, 100);
		String bar = "|".repeat(100);
		if (random != 0) {
			StringBuilder builder = new StringBuilder();
			builder.append("<rainbow:");
			if (this.random.nextInt(1, 2) == 1)
				builder.append("!");
			builder.append(this.random.nextInt(1, 18)).append(">");
			for (int i = 1; i < 100; i++) {
				if (i == random) {
					builder.append("</rainbow>");
				}
				builder.append("|");
			}
			bar = builder.toString();
		}
		bar = "<gray>"+bar;
		List<Placeholder> placeholders = new ArrayList<>();
		placeholders.add(new Placeholder(name+"_match", MiniMessage.miniMessage().deserialize(bar)));
		placeholders.add(new Placeholder(name+"_int", random));
		return placeholders;
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
