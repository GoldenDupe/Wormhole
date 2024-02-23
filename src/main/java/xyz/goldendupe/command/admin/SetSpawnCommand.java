package xyz.goldendupe.command.admin;

import bet.astral.messenger.placeholder.Placeholder;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.internal.legacy.GDCommand;
import xyz.goldendupe.command.internal.legacy.GDCommandInfo;
import xyz.goldendupe.command.defaults.spawn.AbstractSpawnCommand;
import xyz.goldendupe.models.GDSpawn;

import java.util.Collections;
import java.util.List;

@GDCommandInfo.Command(name = "setspawn",
		senderType = GDCommandInfo.SenderType.PLAYER,
		minArgs = 1,
		memberType = GDCommandInfo.MemberType.ADMINISTRATOR
)
public class SetSpawnCommand extends GDCommand {
	protected SetSpawnCommand(GoldenDupe goldenDupe, GDCommandInfo commandInfo) {
		super(goldenDupe, commandInfo);
	}

	@Override
	public void execute(@NotNull CommandSender sender, @NotNull String[] args, boolean hasArgs) { }

	@Override
	public void execute(@NotNull Player sender, @NotNull String[] args, boolean hasArgs) {
		if (is(args, 0, AbstractSpawnCommand.spawns.toArray(String[]::new))){
			String spawn = args[0];
			Location location = sender.getLocation();
			String permission = "";
			if (args.length>1){
				if (args[1].equalsIgnoreCase("-permission")){
					permission = "goldendupe.spawn."+commandInfo.name();
				}
			}
			GDSpawn gdSpawn = new GDSpawn(spawn.toLowerCase(), location.getWorld().getName(), permission, location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
			goldenDupe.spawns().put(spawn.toLowerCase(), gdSpawn);
			goldenDupe.saveSpawns();
			commandMessenger.message(sender, "setspawn.message-set", new Placeholder("spawn", args[0]));
		} else {
			commandMessenger.message(sender, "setspawn.message-unknown-spawn", new Placeholder("spawn", args[0]));
		}
	}

	@Override
	public List<String> tab(@NotNull CommandSender sender, @NotNull String[] args, boolean hasArgs) { return null; }

	@Override
	public List<String> tab(@NotNull Player sender, @NotNull String[] args, boolean hasArgs) {
		if (args.length == 0){
			return AbstractSpawnCommand.spawns;
		} else if (args.length == 1){
			return List.of("-permission");
		} else {
			return Collections.emptyList();
		}
	}
}
