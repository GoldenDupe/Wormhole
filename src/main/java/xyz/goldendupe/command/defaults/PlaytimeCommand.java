package xyz.goldendupe.command.defaults;

import bet.astral.messagemanager.placeholder.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.GDCommand;
import xyz.goldendupe.command.GDCommandInfo;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;


@GDCommandInfo.Command(name = "playtime", senderType = GDCommandInfo.SenderType.PLAYER, aliases = "pt")
public class PlaytimeCommand extends GDCommand {
	protected PlaytimeCommand(GoldenDupe goldenDupe, GDCommandInfo commandInfo) {
		super(goldenDupe, commandInfo);
	}

	@Override
	public void execute(@NotNull CommandSender sender, @NotNull String[] args, boolean hasArgs) { }

	@Override
	public void execute(@NotNull Player sender, @NotNull String[] args, boolean hasArgs) {
		int playtime = sender.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20;
		boolean self = true;

		if (hasArgs){
			String other = args[0];
			OfflinePlayer player = Bukkit.getPlayer(other);
			//noinspection ConstantValue
			if (player == null && !sender.hasPermission("goldendupe.playtime.offline")){
				commandMessenger.message(sender, "playtime.message-unknown-online-player", new Placeholder("%player%", other));
				return;
			} else if ((player instanceof Player p && !sender.canSee(p)) && !sender.hasPermission("goldendupe.vanish.others")) {
				commandMessenger.message(sender, "playtime.message-unknown-online-player", new Placeholder("%player%", other));
				return;
			}else {
				player = Bukkit.getOfflinePlayer(other);
				if (!player.isOnline() && !player.hasPlayedBefore()){
					commandMessenger.message(sender, "playtime.message-unknown-offline-player", new Placeholder("%player%", other));
					return;
				}
			}

			self = false;
			playtime = player.getStatistic(Statistic.PLAY_ONE_MINUTE)/20;
		}

		long HH = TimeUnit.SECONDS.toHours(playtime) % 24;
		long MM = TimeUnit.SECONDS.toMinutes(playtime) % 60;
		long SS = TimeUnit.SECONDS.toSeconds(playtime) % 60;
		List<Placeholder> placeholders = new LinkedList<>(List.of(new Placeholder("hours", HH), new Placeholder("minutes", MM), new Placeholder("seconds", SS)));
		if (self) {
			commandMessenger.message(sender, "playtime.message-playtime-self", new Placeholder("hours", HH), new Placeholder("minutes", MM), new Placeholder("seconds", SS));
		} else {
			commandMessenger.message(sender, "playtime.message-playtime-other", placeholders);
		}
	}

		@Override
	public List<String> tab(@NotNull CommandSender sender, @NotNull String[] args, boolean hasArgs) { return null; }

	@Override
	public List<String> tab(@NotNull Player sender, @NotNull String[] args, boolean hasArgs) {
		return null;
	}
}
