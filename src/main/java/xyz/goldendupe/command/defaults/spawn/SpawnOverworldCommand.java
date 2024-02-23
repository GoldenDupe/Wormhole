package xyz.goldendupe.command.defaults.spawn;

import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.internal.legacy.GDCommandInfo;
import xyz.goldendupe.command.internal.Permission;
import xyz.goldendupe.command.internal.Permissions;


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
