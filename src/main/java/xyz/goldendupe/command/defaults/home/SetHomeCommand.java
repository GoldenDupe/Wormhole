package xyz.goldendupe.command.defaults.home;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.placeholder.Placeholder;
import bet.astral.messenger.placeholder.PlaceholderList;
import org.bukkit.Location;
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
import xyz.goldendupe.utils.Position;

@Cloud
public class SetHomeCommand extends GDCloudCommand {

    public SetHomeCommand(GoldenDupe goldenDupe, PaperCommandManager<CommandSender> commandManager) {
        super(goldenDupe, commandManager);

        commandManager.command(
                commandManager.commandBuilder(
                                "sethome",
                                Description.of("Sets a new home that you can teleport to."),
                                "addhome"
                        )
                        .senderType(Player.class)
                        .argument(StringParser.stringComponent(StringParser.StringMode.SINGLE).name("sethome-name"))
                        .handler(context -> {

                            Player sender = context.sender();
                            Location location = sender.getLocation();
                            String homeName = context.get("sethome-name").toString().toLowerCase();

                            GDPlayer player = goldenDupe.playerDatabase().fromPlayer(sender);

                            if (goldenDupe.getHomes(player).containsKey(homeName)){
                                commandMessenger.message(sender, "sethome.message-already-exists",
                                        new Placeholder("home", homeName));
                                return;
                            } else if (goldenDupe.getHomes(player).size() >= player.getMaxHomes()) {
                                commandMessenger.message(sender, "sethome.message-too-many-homes",
                                        new Placeholder("max-homes", player.getMaxHomes()),
                                        new Placeholder("homes", goldenDupe.getHomes(player).size()));
                                return;
                            }

                            goldenDupe.requestSaveHome(player,
                                    new GDHome(
                                            homeName,
                                            location.getX(),
                                            location.getY(),
                                            location.getZ(),
                                            location.getYaw(),
                                            location.getWorld()
                                    )
                            );

                            GDHome home = goldenDupe.getHomes(player).get(homeName.toLowerCase());
                            PlaceholderList placeholders = new PlaceholderList(home.asPlaceholder("home"));
                            placeholders.add(new Placeholder("home", homeName));
                            placeholders.add(new Placeholder("xyz", new Vector3d(location.getX(), location.getY(), location.getZ())));
                            placeholders.add(new Placeholder("world", location.getWorld().getName()));

                            commandMessenger.message(sender, "sethome.message-set",
                                    placeholders);
                        })
        );

    }

}
