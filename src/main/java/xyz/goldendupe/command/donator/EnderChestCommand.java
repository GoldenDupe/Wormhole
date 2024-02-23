package xyz.goldendupe.command.donator;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.internal.cloud.Cloud;
import xyz.goldendupe.command.internal.cloud.GDCloudCommand;
import xyz.goldendupe.command.internal.legacy.GDCommandInfo;

@Cloud
public class EnderChestCommand extends GDCloudCommand {
    public EnderChestCommand(GoldenDupe goldenDupe, PaperCommandManager<CommandSender> commandManager) {
        super(goldenDupe, commandManager);
        commandManager.command(
                commandManager.commandBuilder(
                                "enderchest",
                                Description.of("Allows a player to open their ender chest remotely."),
                                "ec"
                        )
                        .permission(GDCommandInfo.MemberType.DONATOR.cloudOf("ender-chest"))
                        .senderType(Player.class)
                        .handler(context -> {
                            Player sender = context.sender();
                            sender.openInventory(sender.getEnderChest());
                        })
        );
    }
}
