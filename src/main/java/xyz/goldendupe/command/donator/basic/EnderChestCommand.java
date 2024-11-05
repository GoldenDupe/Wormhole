package xyz.goldendupe.command.donator.basic;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import bet.astral.cloudplusplus.annotations.Cloud;
import xyz.goldendupe.GoldenDupeCommandRegister;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.utils.MemberType;

@Cloud
public class EnderChestCommand extends GDCloudCommand {
    public EnderChestCommand(GoldenDupeCommandRegister register, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
        super(register, commandManager);
        commandManager.command(
                commandManager.commandBuilder(
                                "enderchest",
                                Description.of("Allows a player to open their ender chest remotely."),
                                "ec"
                        )
                        .permission(MemberType.DONATOR.cloudOf("ender-chest"))
                        .senderType(Player.class)
                        .handler(context -> {
                            Player sender = context.sender();
                            goldenDupe().getServer().getScheduler().runTask(goldenDupe(), ()->{
                                sender.openInventory(sender.getEnderChest());
                            });
                        })
        );
    }
}
