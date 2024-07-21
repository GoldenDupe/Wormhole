package xyz.goldendupe.command.admin;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.v2.placeholder.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.bukkit.parser.PlayerParser;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import xyz.goldendupe.GoldenDupeBootstrap;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.messenger.Translations;
import xyz.goldendupe.utils.MemberType;

@Cloud
public class FeedCommand extends GDCloudCommand {

    public FeedCommand(GoldenDupeBootstrap bootstrap, PaperCommandManager<CommandSender> commandManager) {
        super(bootstrap, commandManager);
        commandManager.command(
                commandManager.commandBuilder(
                                "feed",
                                Description.of("Feeds the player.")
                        )
                        .permission(MemberType.ADMINISTRATOR.cloudOf("feed"))
                        .optional(PlayerParser.playerComponent().name("feed-player"))
                        .senderType(Player.class)
                        .handler(context -> {
                            Player healed = (Player) context.optional("feed-player")
                                            .orElse(context.sender());

                            if (context.optional("feed-player").isEmpty() && !healed.equals(context.sender())){
                                messenger.message(context.sender(), Translations.COMMAND_FEED_OTHER, Placeholder.of("player", healed.name()));
                            } else {
                                messenger.message(context.sender(), Translations.COMMAND_FEED_SELF);
                            }
                            goldenDupe().getServer().getScheduler().runTaskAsynchronously(goldenDupe(), ()->{
                                healed.setFoodLevel(20);
                            });

                        })
        );
    }

}
