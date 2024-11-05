package xyz.goldendupe.command.defaults;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.v2.placeholder.Placeholder;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderList;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.Command;
import org.incendo.cloud.bukkit.parser.OfflinePlayerParser;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import xyz.goldendupe.GoldenDupeCommandRegister;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.messenger.Translations;
import xyz.goldendupe.utils.MemberType;

import java.util.List;
import java.util.concurrent.TimeUnit;


@Cloud
public class PlaytimeCommand extends GDCloudCommand {

	public PlaytimeCommand(GoldenDupeCommandRegister register, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
		super(register, commandManager);


		Command.Builder<Player> builder = commandManager.commandBuilder(
				"playtime",
				Description.of("Allows player to see their own playtime."),
				"pt")
				.senderType(Player.class)
				.permission(MemberType.DEFAULT.permissionOf("playtime"))
				.handler(context->{
					Player sender = context.sender();

					int playtime = sender.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20;
					long HH = TimeUnit.SECONDS.toHours(playtime) % 24;
					long MM = TimeUnit.SECONDS.toMinutes(playtime) % 60;
					long SS = TimeUnit.SECONDS.toSeconds(playtime) % 60;
					PlaceholderList placeholders = new PlaceholderList(List.of(Placeholder.of("hours", HH), Placeholder.of("minutes", MM), Placeholder.of("seconds", SS)));
					placeholders.add(Placeholder.of("player", sender.name()));

					messenger.message(sender, Translations.COMMAND_PLAYTIME_SELF, placeholders);
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
					PlaceholderList placeholders = new PlaceholderList(List.of(Placeholder.of("hours", HH), Placeholder.of("minutes", MM), Placeholder.of("seconds", SS)));
					placeholders.add(Placeholder.of("player", who.getName()));
					if (sender.equals(who)) {
						messenger.message(sender, Translations.COMMAND_PLAYTIME_SELF, placeholders);
					} else {
						messenger.message(sender, Translations.COMMAND_PLAYTIME_OTHER, placeholders);
					}
				})
		);
	}
}
