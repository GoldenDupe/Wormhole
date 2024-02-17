package xyz.goldendupe.command.defaults.spawn;

import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.Season;
import xyz.goldendupe.command.GDCommandInfo;
import xyz.goldendupe.command.Permission;
import xyz.goldendupe.command.Permissions;


@Permissions(
		{
				@Permission("goldendupe.spawn.end"),
		}
)
@GDCommandInfo.Command(
		name = "end",
		senderType = GDCommandInfo.SenderType.PLAYER)
public class SpawnEndCommand extends AbstractSpawnCommand{
	protected SpawnEndCommand(GoldenDupe goldenDupe, GDCommandInfo commandInfo) {
		super(goldenDupe, commandInfo, "end");
	}
}
