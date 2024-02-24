package xyz.goldendupe.command.admin;

import bet.astral.messenger.placeholder.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.StringParser;
import xyz.goldendupe.GoldenDupe;
import bet.astral.cloudplusplus.annotations.Cloud;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.utils.MemberType;

@Cloud
public class SkullCommand extends GDCloudCommand {

    public SkullCommand(GoldenDupe goldenDupe, PaperCommandManager<CommandSender> commandManager) {
        super(goldenDupe, commandManager);
        commandManager.command(
                commandManager.commandBuilder(
                                "skull",
                                Description.of("Gives the player a head of the specified player."),
                                "head"
                        )
                        .permission(MemberType.ADMINISTRATOR.cloudOf("skull"))
                        .argument(StringParser.stringComponent(StringParser.StringMode.SINGLE).name("skull-player"))
                        .senderType(Player.class)
                        .handler(context -> {

                            Player sender = context.sender();
                            String name = context.get("skull-player");

                            ItemStack stack = new ItemStack(Material.PLAYER_HEAD, 1);
                            SkullMeta meta = (SkullMeta) stack.getItemMeta();
                            meta.setOwningPlayer(Bukkit.getOfflinePlayer(name));
                            stack.setItemMeta(meta);

                            sender.getInventory().addItem(stack);
                            commandMessenger.message(sender, "skull.skull-given", new Placeholder("player", name));

                        })
        );
    }

}
