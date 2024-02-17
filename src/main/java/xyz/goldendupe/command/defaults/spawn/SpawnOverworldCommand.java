package xyz.goldendupe.command.defaults.spawn;

import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.Season;
import xyz.goldendupe.command.GDCommandInfo;
import xyz.goldendupe.command.Permission;
import xyz.goldendupe.command.Permissions;


@Permissions(
		{
				@Permission("goldendupe.spawn.overworld"),
		}
)
@GDCommandInfo.Command(
		name = "spawn",
		senderType = GDCommandInfo.SenderType.PLAYER, aliases = "overworld")
public class SpawnOverworldCommand extends AbstractSpawnCommand{
	protected SpawnOverworldCommand(GoldenDupe goldenDupe, GDCommandInfo commandInfo) {
		super(goldenDupe, commandInfo, "overworld");
	}
}
