package xyz.goldendupe.command.defaults.home;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.v2.placeholder.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.StringParser;
import org.joml.Vector3d;
import xyz.goldendupe.GoldenDupeBootstrap;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.messenger.GoldenMessenger;
import xyz.goldendupe.messenger.Translations;
import xyz.goldendupe.models.GDPlayer;
import xyz.goldendupe.models.impl.GDHome;

@Cloud
public class DelHomeCommand extends GDCloudCommand {

    public DelHomeCommand(GoldenDupeBootstrap bootstrap, PaperCommandManager<CommandSender> commandManager) {
        super(bootstrap, commandManager);

        commandManager.command(
                commandManager.commandBuilder(
                                "delhome",
                                Description.of("Deletes one of your existing homes."),
                                "dh", "deletehome"
                        )
                        .senderType(Player.class)
                        .argument(StringParser.stringComponent(StringParser.StringMode.SINGLE).name("delhome-name"))
                        .handler(context -> {

                            Player sender = context.sender();
                            String homeName = context.get("delhome-name").toString().toLowerCase();

                            GDPlayer player = goldenDupe().playerDatabase().fromPlayer(sender);

                            if (!goldenDupe().getHomes(player).containsKey(homeName)){
                                messenger.message(sender, Translations.COMMAND_DELETE_HOME_DOESNT_EXIST,
                                        Placeholder.of("home", homeName));
                                return;
                            }

                            GDHome home = goldenDupe().getHomes(player).get(homeName.toLowerCase());

                            goldenDupe().requestDeleteHome(player, homeName);

                            messenger.message(sender, Translations.COMMAND_DELETE_HOME_REMOVED,
                                    Placeholder.of("home", homeName),
                                    Placeholder.of("xyz", new Vector3d(home.getX(), home.getY(), home.getZ()).toString()),
                                    Placeholder.of("x", GoldenMessenger.format(home.getX())),
                                    Placeholder.of("y", GoldenMessenger.format(home.getY())),
                                    Placeholder.of("z", GoldenMessenger.format(home.getZ())),
                                    Placeholder.of("world", sender.getWorld().getName()));
                        })
        );

    }

}
