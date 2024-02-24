package xyz.goldendupe.command.admin;

import bet.astral.messenger.placeholder.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.StringParser;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.internal.cloud.Cloud;
import xyz.goldendupe.command.internal.cloud.GDCloudCommand;

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

                            for (String str : goldenDupe.getSpawnsAsNames()) {
                                if (spawnName.equalsIgnoreCase(str)) {
                                    goldenDupe.removeSpawn(spawnName);
                                    commandMessenger.message(sender, "removespawn.message-remove",
                                            new Placeholder("spawn", spawnName));
                                    break;
                                }
                            }

                        })
        );
    }

}
