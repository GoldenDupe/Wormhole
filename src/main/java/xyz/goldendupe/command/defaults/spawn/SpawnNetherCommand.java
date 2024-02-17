package xyz.goldendupe.command.defaults.spawn;

import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.GDCommandInfo;
import xyz.goldendupe.command.Permission;
import xyz.goldendupe.command.Permissions;

@Permissions(
		{
				@Permission("goldendupe.spawn.nether"),
		}
)
@GDCommandInfo.Command(
		name = "nether",
		senderType = GDCommandInfo.SenderType.PLAYER)
public class SpawnNetherCommand extends AbstractSpawnCommand{
	protected SpawnNetherCommand(GoldenDupe goldenDupe, GDCommandInfo commandInfo) {
		super(goldenDupe, commandInfo, "nether");
	}
}
