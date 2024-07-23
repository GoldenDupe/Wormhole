package xyz.goldendupe.command.defaults.home;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.v2.placeholder.Placeholder;
import bet.astral.messenger.v2.placeholder.PlaceholderList;
import org.bukkit.Location;
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
public class SetHomeCommand extends GDCloudCommand {

    public SetHomeCommand(GoldenDupeBootstrap bootstrap, PaperCommandManager<CommandSender> commandManager) {
        super(bootstrap, commandManager);

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

                            GDPlayer player = goldenDupe().playerDatabase().fromPlayer(sender);

                            if (goldenDupe().getHomes(player).containsKey(homeName)){
                                messenger.message(sender, Translations.COMMAND_SET_HOME_ALREADY_EXISTS,
                                        Placeholder.of("home", homeName));
                                return;
                            } else if (goldenDupe().getHomes(player).size() >= player.getMaxHomes()) {
                                messenger.message(sender, Translations.COMMAND_SET_HOME_MAX_HOMES,
                                        Placeholder.of("max-homes", player.getMaxHomes()),
                                        Placeholder.of("homes", goldenDupe().getHomes(player).size()));
                                return;
                            }

                            goldenDupe().requestSaveHome(player,
                                    new GDHome(
                                            homeName,
                                            location.getX(),
                                            location.getY(),
                                            location.getZ(),
                                            location.getYaw(),
                                            location.getWorld()
                                    )
                            );

                            GDHome home = goldenDupe().getHomes(player).get(homeName.toLowerCase());
                            PlaceholderList placeholders = new PlaceholderList(home.toPlaceholders("home"));
                            placeholders.add(Placeholder.of("home", homeName));
                            placeholders.add(Placeholder.of("xyz", new Vector3d(location.getX(), location.getY(), location.getZ()).toString()));
                            placeholders.add(Placeholder.of("world", location.getWorld().getName()));
                            placeholders.add(Placeholder.of("x", GoldenMessenger.format(home.getX())));
                            placeholders.add(Placeholder.of("y", GoldenMessenger.format(home.getY())));
                            placeholders.add(Placeholder.of("z", GoldenMessenger.format(home.getZ())));
                            messenger.message(sender, Translations.COMMAND_SET_HOME_SUCCESS,
                                    placeholders);
                        })
        );

    }

}
