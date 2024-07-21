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
public class HealCommand extends GDCloudCommand {

    public HealCommand(GoldenDupeBootstrap bootstrap, PaperCommandManager<CommandSender> commandManager) {
        super(bootstrap, commandManager);
        commandManager.command(
                commandManager.commandBuilder(
                                "heal",
                                Description.of("Heals the player.")
                        )
                        .permission(MemberType.ADMINISTRATOR.cloudOf("heal"))
                        .optional(PlayerParser.playerComponent().name("heal-player"))
                        .senderType(Player.class)
                        .handler(context -> {
                            Player healed = (Player) context.optional("heal-player").orElse(context.sender());

                            if (healed.equals(context.sender())) {
                                messenger.message(context.sender(), Translations.COMMAND_HEAL_SELF);
                            } else {
                                messenger.message(context.sender(), Translations.COMMAND_HEAL_OTHER, Placeholder.of("player", healed.name()));
                            }
                            goldenDupe().getServer().getScheduler().runTask(goldenDupe(), ()->{
                                healed.heal(100000);
                            });

                        })
        );
    }

}
