package xyz.goldendupe.command.defaults;

import bet.astral.messenger.placeholder.Placeholder;
import bet.astral.messenger.utils.PlaceholderUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.Command;
import org.incendo.cloud.bukkit.parser.OfflinePlayerParser;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import xyz.goldendupe.GoldenDupe;
import bet.astral.cloudplusplus.annotations.Cloud;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.messenger.GoldenMessenger;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Cloud
public class PlaytimeCommand extends GDCloudCommand {

	public PlaytimeCommand(GoldenDupe goldenDupe, PaperCommandManager<CommandSender> commandManager) {
		super(goldenDupe, commandManager);

		Command.Builder<Player> builder = commandManager.commandBuilder(
				"playtime",
				Description.of("Allows player to see their own playtime."),
				"pt")
				.senderType(Player.class)
				.handler(context->{
					Player sender = context.sender();

					int playtime = sender.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20;
					long HH = TimeUnit.SECONDS.toHours(playtime) % 24;
					long MM = TimeUnit.SECONDS.toMinutes(playtime) % 60;
					long SS = TimeUnit.SECONDS.toSeconds(playtime) % 60;
					List<Placeholder> placeholders = new LinkedList<>(List.of(new Placeholder("hours", HH), new Placeholder("minutes", MM), new Placeholder("seconds", SS)));
					placeholders.addAll(GoldenMessenger.playerPlaceholders("who", sender));
					commandMessenger.message(sender, "playtime.message-playtime-self", placeholders);
		});
		commandManager.command(builder);
		commandManager.command(builder
				.argument(OfflinePlayerParser.offlinePlayerComponent().name("who"))
				.handler(context->{
					Player sender = context.sender();
					OfflinePlayer who = context.get("who");

					int playtime = who.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20;
					long HH = TimeUnit.SECONDS.toHours(playtime) % 24;
					long MM = TimeUnit.SECONDS.toMinutes(playtime) % 60;
					long SS = TimeUnit.SECONDS.toSeconds(playtime) % 60;
					List<Placeholder> placeholders = new LinkedList<>(List.of(new Placeholder("hours", HH), new Placeholder("minutes", MM), new Placeholder("seconds", SS)));
					placeholders.addAll(PlaceholderUtils.createPlaceholders("who", who));
					if (sender.equals(who)) {
						commandMessenger.message(sender, "playtime.message-playtime-self", placeholders);
					} else {
						commandMessenger.message(sender, "playtime.message-playtime-other", placeholders);
					}
				})
		);
	}
}
