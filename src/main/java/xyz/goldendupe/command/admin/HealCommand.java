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
public class HealCommand extends GDCloudCommand {

    public HealCommand(GoldenDupe goldenDupe, PaperCommandManager<CommandSender> commandManager) {
        super(goldenDupe, commandManager);
        commandManager.command(
                commandManager.commandBuilder(
                                "heal",
                                Description.of("Heals the player.")
                        )
                        .permission(MemberType.ADMINISTRATOR.cloudOf("heal"))
                        .optional(StringParser.stringComponent(StringParser.StringMode.SINGLE).name("heal-player"))
                        .senderType(Player.class)
                        .handler(context -> {

                            Player healed = Bukkit.getPlayer(
                                    context.optional("heal-player")
                                            .orElse(context.sender().getName()).toString()
                            );

                            if (healed == null) {
                                commandMessenger.message(context.sender(), "heal.player-not-found",
                                        new Placeholder("player", context.sender().getName()));
                                return;
                            }

                            goldenDupe.getServer().getScheduler().runTaskAsynchronously(goldenDupe, ()->{
                                healed.setHealth(healed.getMaxHealth());
                                commandMessenger.message(healed, "heal.player-healed",
                                        new Placeholder("player", healed.getName()));
                            });

                        })
        );
    }

}
