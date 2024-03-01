package xyz.goldendupe.command.admin.spawn;

import bet.astral.messenger.placeholder.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.StringParser;
import xyz.goldendupe.GoldenDupe;
import bet.astral.cloudplusplus.annotations.Cloud;
import xyz.goldendupe.command.cloud.GDCloudCommand;

@Cloud
public class RemoveSpawnCommand extends GDCloudCommand {

    public RemoveSpawnCommand(GoldenDupe goldenDupe, PaperCommandManager<CommandSender> commandManager) {
        super(goldenDupe, commandManager);
        commandManager.command(
                commandManager.commandBuilder(
                                "removespawn",
                                Description.of("Removes an existing spawn from the spawn database.")
                        )
                        .senderType(Player.class)
                        .permission("goldendupe.admin.removespawn")
                        .argument(StringParser.stringComponent(StringParser.StringMode.SINGLE).name("removespawn-name"))
                        .handler(context -> {

                            Player sender = context.sender();
                            String spawnName = context.get("removespawn-name");

                            if (goldenDupe.getGlobalData().getSpawns().containsKey(spawnName.toLowerCase())) {
                                goldenDupe.getGlobalData().removeSpawn(spawnName);
                                commandMessenger.message(sender, "removespawn.message-already-removed",
                                        new Placeholder("spawn", spawnName));
                                return;
                            }
                            commandMessenger.message(sender, "setspawn.message-remove",
                                    new Placeholder("spawn", spawnName));
                        })
        );
    }

}
