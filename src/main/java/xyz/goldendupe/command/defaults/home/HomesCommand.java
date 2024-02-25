package xyz.goldendupe.command.defaults.home;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.placeholder.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import org.joml.Vector3d;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.models.GDPlayer;

@Cloud
public class HomesCommand extends GDCloudCommand {

    public HomesCommand(GoldenDupe goldenDupe, PaperCommandManager<CommandSender> commandManager) {
        super(goldenDupe, commandManager);

        commandManager.command(
                commandManager.commandBuilder(
                                "homes",
                                Description.of("Lists your existing homes and their locations.")
                        )
                        .senderType(Player.class)
                        .handler(context -> {

                            Player sender = context.sender();
                            GDPlayer player = goldenDupe.playerDatabase().fromPlayer(sender);

                            player.getHomes().forEach((name, home) ->
                                    commandMessenger.message(sender, "homes.message-list",
                                        new Placeholder("home", name),
                                        new Placeholder("xyz", new Vector3d(home.x(), home.y(), home.z())),
                                        new Placeholder("world", home.world()))
                            );

                        })
        );

    }

}
