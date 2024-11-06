package xyz.goldendupe.command.defaults.home;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.v2.placeholder.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import org.joml.Vector3d;
import xyz.goldendupe.GoldenDupeCommandRegister;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.messenger.GoldenMessenger;
import xyz.goldendupe.messenger.Translations;
import xyz.goldendupe.models.GDPlayer;

@Cloud
public class HomesCommand extends GDCloudCommand {

    public HomesCommand(GoldenDupeCommandRegister register, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
        super(register, commandManager);


        commandManager.command(
                commandManager.commandBuilder(
                                "homes",
                                Description.of("Lists your existing homes and their locations.")
                        )
                        .senderType(Player.class)
                        .handler(context -> {

                            Player sender = context.sender();
                            GDPlayer player = goldenDupe().playerDatabase().fromPlayer(sender);

                            goldenDupe().getHomes(player).forEach((name, home) ->
                                    messenger.message(sender, Translations.COMMAND_HOMES_LIST,
                                        Placeholder.of("home", name),
                                        Placeholder.of("xyz", new Vector3d(home.getX(), home.getY(), home.getZ()).toString()),
                                            Placeholder.of("x", GoldenMessenger.format(home.getX())),
                                            Placeholder.of("y", GoldenMessenger.format(home.getY())),
                                            Placeholder.of("z", GoldenMessenger.format(home.getZ())),
                                            Placeholder.of("world", home.getWorldName()))
                            );

                        })
        );

    }

}
