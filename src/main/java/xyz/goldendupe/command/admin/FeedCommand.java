package xyz.goldendupe.command.admin;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.placeholder.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.StringParser;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.utils.MemberType;

@Cloud
public class FeedCommand extends GDCloudCommand {

    public FeedCommand(GoldenDupe goldenDupe, PaperCommandManager<CommandSender> commandManager) {
        super(goldenDupe, commandManager);
        commandManager.command(
                commandManager.commandBuilder(
                                "feed",
                                Description.of("Feeds the player.")
                        )
                        .permission(MemberType.ADMINISTRATOR.cloudOf("feed"))
                        .optional(StringParser.stringComponent(StringParser.StringMode.SINGLE).name("feed-player"))
                        .senderType(Player.class)
                        .handler(context -> {

                            Player healed = Bukkit.getPlayer(
                                    context.optional("feed-player")
                                            .orElse(context.sender().getName()).toString()
                            );

                            if (healed == null) {
                                commandMessenger.message(context.sender(), "feed.player-not-found",
                                        new Placeholder("player", context.sender().getName()));
                                return;
                            }

                            goldenDupe.getServer().getScheduler().runTaskAsynchronously(goldenDupe, ()->{
                                healed.setFoodLevel(20);
                                commandMessenger.message(healed, "feed.player-fed",
                                        new Placeholder("player", healed.getName()));
                            });

                        })
        );
    }

}
