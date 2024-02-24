package xyz.goldendupe.command.defaults.spawn;

import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.internal.legacy.GDCommandInfo;
import xyz.goldendupe.command.internal.Permission;
import xyz.goldendupe.command.internal.Permissions;


@Permissions(
		{
				@Permission("goldendupe.spawn.end"),
		}
)
@GDCommandInfo.Command(
		name = "end",
		senderType = GDCommandInfo.SenderType.PLAYER)
public class SpawnEndCommand extends AbstractSpawnCommand {
	protected SpawnEndCommand(GoldenDupe goldenDupe, GDCommandInfo commandInfo) {
		super(goldenDupe, commandInfo, "end");
	}
}
