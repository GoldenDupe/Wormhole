package xyz.goldendupe.command.defaults.home;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.v2.placeholder.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.StringParser;
import xyz.goldendupe.GoldenDupeBootstrap;
import xyz.goldendupe.GoldenDupeCommandRegister;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.messenger.Translations;
import xyz.goldendupe.models.GDPlayer;
import xyz.goldendupe.utils.Position;
import xyz.goldendupe.utils.TimedTeleport;

@Cloud
public class HomeCommand extends GDCloudCommand {

    public HomeCommand(GoldenDupeCommandRegister register, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
        super(register, commandManager);

        commandManager.command(
                commandManager.commandBuilder(
                                "home",
                                Description.of("Teleports the player to the home provided."),
                                "h"
                        )
                        .senderType(Player.class)
                        .argument(StringParser.stringComponent(StringParser.StringMode.SINGLE).name("home-name"))
                        .handler(context -> {

                            Player sender = context.sender();
                            GDPlayer player = goldenDupe().playerDatabase().fromPlayer(sender);
                            String homeName = context.get("home-name").toString().toLowerCase();

                            if (!goldenDupe().getHomes(player).containsKey(homeName)){
                                messenger.message(sender, Translations.COMMAND_HOME_DOESNT_EXIST,
                                        Placeholder.of("home", homeName));
                                return;
                            }

                            Position home = goldenDupe().getHomes(player).get(homeName);

                            messenger.message(sender, Translations.COMMAND_HOME_TELEPORTING,
                                    Placeholder.of("home", homeName));
                            new TimedTeleport(messenger, "commands.home",
                                    sender, home.asLocation(),
                                    false, 100)
                                    .setTeleportCause(PlayerTeleportEvent.TeleportCause.COMMAND)
                                    .accept();
                        })
        );
    }
}
