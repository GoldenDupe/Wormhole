package xyz.goldendupe.command.defaults.home;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.placeholder.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.StringParser;
import org.joml.Vector3d;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.models.GDPlayer;
import xyz.goldendupe.models.impl.GDHome;

@Cloud
public class DelHomeCommand extends GDCloudCommand {

    public DelHomeCommand(GoldenDupe goldenDupe, PaperCommandManager<CommandSender> commandManager) {
        super(goldenDupe, commandManager);

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

                            GDPlayer player = goldenDupe.playerDatabase().fromPlayer(sender);

                            if (!goldenDupe.getHomes(player).containsKey(homeName)){
                                commandMessenger.message(sender, "delhome.message-doesnt-exist",
                                        new Placeholder("home", homeName));
                                return;
                            }

                            GDHome home = goldenDupe.getHomes(player).get(homeName.toLowerCase());

                            goldenDupe.requestDeleteHome(player, homeName);

                            commandMessenger.message(sender, "delhome.message-del",
                                    new Placeholder("home", homeName),
                                    new Placeholder("xyz", new Vector3d(home.getX(), home.getY(), home.getZ())),
                                    new Placeholder("world", sender.getWorld().getName()));
                        })
        );

    }

}
