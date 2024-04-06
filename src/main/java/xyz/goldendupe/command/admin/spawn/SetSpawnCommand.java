package xyz.goldendupe.command.admin.spawn;

import bet.astral.messenger.placeholder.Placeholder;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.flag.CommandFlag;
import org.incendo.cloud.parser.standard.StringParser;
import xyz.goldendupe.GoldenDupe;
import bet.astral.cloudplusplus.annotations.Cloud;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.models.impl.GDSpawn;

@Cloud
public class SetSpawnCommand extends GDCloudCommand {

	//Not sure if I broke this or not but I changed some of the structure
	public SetSpawnCommand(GoldenDupe goldenDupe, PaperCommandManager<CommandSender> commandManager) {
		super(goldenDupe, commandManager);
		commandManager.command(
				commandManager.commandBuilder(
								"setspawn",
								Description.of("Adds a new spawn to the spawn database.")
						)
						.senderType(Player.class)
						.permission("goldendupe.admin.setspawn")
						.argument(StringParser.stringComponent(StringParser.StringMode.SINGLE).name("setspawn-name"))
						.flag(CommandFlag.builder("permission"))
						.handler(context -> {

							Player sender = context.sender();
							Location location = sender.getLocation();
							String spawnName = context.get("setspawn-name");
							boolean hasPermission = context.flags().hasFlag("permission");
							String permission = "goldendupe.spawn." + context.flags().get("permission");
							if (!hasPermission) permission = "";


							if (goldenDupe.getSpawnDatabase().exists(spawnName)){
								commandMessenger.message(sender, "setspawn.message-already-set",
										new Placeholder("spawn", spawnName));
								return;
							}

							goldenDupe.getSpawnDatabase().create(
									new GDSpawn(
											spawnName.toLowerCase(),
											permission,
											location.getX(),
											location.getY(),
											location.getZ(),
											location.getYaw(),
											location.getWorld()
									)
							);

							commandMessenger.message(sender, "setspawn.message-set",
									new Placeholder("spawn", spawnName));
						})
		);
	}

}
