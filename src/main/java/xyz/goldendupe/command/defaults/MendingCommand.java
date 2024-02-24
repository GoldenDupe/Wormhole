package xyz.goldendupe.command.defaults;

import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.incendo.cloud.Command;
import org.incendo.cloud.paper.PaperCommandManager;
import xyz.goldendupe.GoldenDupe;
import bet.astral.cloudplusplus.annotations.Cloud;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.utils.MemberType;

@Cloud
public class MendingCommand extends GDCloudCommand {
	public MendingCommand(GoldenDupe goldenDupe, PaperCommandManager<CommandSender> commandManager) {
		super(goldenDupe, commandManager);

		Command.Builder<Player> builder =
				commandManager.commandBuilder("mending",
						"parsing")
						.permission(MemberType.DEFAULT.cloudOf("mending"))
						.senderType(Player.class)
						.handler(context->{
							Player sender = context.sender();
							if (sender.getInventory().getItemInMainHand().isEmpty()){
								commandMessenger.message(sender, "mending.message-cannot-something-air");
								return;
							}
							if (!Enchantment.MENDING.canEnchantItem(sender.getInventory().getItemInMainHand())){
								commandMessenger.message(sender, "mending.message-cannot-enchant");
								return;
							}
							sender.getInventory().getItemInMainHand().addEnchantment(Enchantment.MENDING, 1);
							commandMessenger.message(sender, "mending.message-enchanted");
						})
				;
		commandManager.command(builder);
	}
}
